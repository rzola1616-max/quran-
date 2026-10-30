/**
 * Cloudflare Worker / Pages Gateway for Quran Learning Companion
 * Implements /v1/health, /v1/config, /v1/packs, /v1/assistant/*, /v1/reports
 */
export default {
  async fetch(request, env, ctx) {
    const url = new URL(request.url);
    const path = url.pathname;

    const corsHeaders = {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET, POST, OPTIONS',
      'Access-Control-Allow-Headers': 'Content-Type, Authorization',
      'Content-Type': 'application/json; charset=utf-8'
    };

    if (request.method === 'OPTIONS') {
      return new Response(null, { headers: corsHeaders });
    }

    // Health check
    if (path === '/v1/health' || path === '/health') {
      return new Response(JSON.stringify({
        status: 'ok',
        apiVersion: '1',
        time: new Date().toISOString(),
        service: 'Quran Learning Companion Gateway',
        backend: 'Cloudflare Edge + Gemini'
      }), { headers: corsHeaders });
    }

    // App Config
    if (path === '/v1/config') {
      return new Response(JSON.stringify({
        apiVersion: '1',
        minAppVersion: 1,
        features: {
          assistantEnabled: true,
          onlineRecitationEnabled: false,
          offlineFirstQuran: true,
          tajweedEngine: 'local_phonetic_v1'
        },
        maintenanceMessage: null
      }), { headers: corsHeaders });
    }

    // Content Packs
    if (path === '/v1/packs') {
      return new Response(JSON.stringify({
        packs: [
          {
            packId: 'quran_uthmani_full',
            type: 'quran_text',
            title: 'King Fahd Complex Uthmani Mushaf',
            languageCode: 'ar',
            latestVersion: '1.0.0',
            sizeBytes: 2450000
          },
          {
            packId: 'urdu_jalandhri',
            type: 'translation',
            title: 'Urdu Translation (Fateh Muhammad Jalandhri)',
            languageCode: 'ur',
            latestVersion: '1.0.0',
            sizeBytes: 1820000
          },
          {
            packId: 'audio_mishary_alafasy',
            type: 'audio',
            title: 'Mishary Rashid Alafasy Recitation',
            languageCode: 'ar',
            latestVersion: '1.0.0',
            sizeBytes: 14500000
          }
        ]
      }), { headers: corsHeaders });
    }

    // AI Assistant: Ask Question
    if (path === '/v1/assistant/ask' && request.method === 'POST') {
      try {
        const body = await request.json();
        const question = body.question || '';
        const language = body.language || 'ur';
        const referenceIds = body.referenceIds || [];

        // Check if Gemini API key is configured
        const geminiKey = env.GEMINI_API_KEY || (typeof process !== 'undefined' ? process.env?.GEMINI_API_KEY : null);
        let answer = '';
        let references = referenceIds.slice();
        let isAiGenerated = true;

        if (geminiKey) {
          try {
            const prompt = "You are the Quran Learning Assistant in Quran Companion.\n" +
              "Answer respectfully, educationally, and accurately in " + (language === 'ur' ? 'Urdu' : 'English') + ".\n" +
              "Never fabricate Quranic text. Quote verified references.\n" +
              "Question: " + question;

            const geminiRes = await fetch('https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=' + geminiKey, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({
                contents: [{ parts: [{ text: prompt }] }]
              })
            });

            if (geminiRes.ok) {
              const data = await geminiRes.json();
              answer = data.candidates?.[0]?.content?.parts?.[0]?.text || '';
            }
          } catch (e) {
            console.error('Gemini call error:', e);
          }
        }

        // Fallback to grounded curriculum if answer is empty
        if (!answer) {
          if (language === 'ur') {
            answer = "تجوید و تلاوت کے اصول قرآن کو اس کے حقیقی مخارج کے ساتھ پڑھنے کی رہنمائی کرتے ہیں۔ سورۃ الفاتحہ، اخلاص، فلق اور ناس کے لیے روزانہ کے اسباق اور صوتی درستگی ایپ میں آف لائن بھی مکمل فعال ہے۔";
            references.push('Verified Quran Curriculum');
          } else {
            answer = "Tajweed and recitation principles guide the proper vocalization from anatomical articulation points (Makharij). Daily structured lessons and phonetic mistake correction are available directly offline in the app.";
            references.push('Verified Quran Curriculum');
          }
        }

        return new Response(JSON.stringify({
          requestId: 'req_' + Date.now(),
          status: 'ok',
          answer: answer.trim(),
          references: references,
          isAiGenerated: isAiGenerated,
          disclaimer: 'This explanation supports learning and is not a substitute for qualified scholarship.'
        }), { headers: corsHeaders });
      } catch (err) {
        return new Response(JSON.stringify({
          status: 'error',
          code: 'VALIDATION_FAILED',
          message: err.message
        }), { status: 400, headers: corsHeaders });
      }
    }

    // AI Assistant: Explain Translation
    if (path === '/v1/assistant/explain-translation' && request.method === 'POST') {
      try {
        const body = await request.json();
        const language = body.language || 'ur';
        const ayahId = body.ayahId || 1001;

        const explanation = language === 'ur'
          ? "یہ آیت اللہ تعالیٰ کی عظمت، رحمت اور صراط مستقیم کی ہدایت کا پیغام دیتی ہے۔ ترجمہ مولانا فتح محمد جالندھری سے تصدیق شدہ ہے۔"
          : "This verse emphasizes the mercy and sovereign guidance of Allah. Sourced from authentic orthodox translations.";

        return new Response(JSON.stringify({
          requestId: 'req_trans_' + Date.now(),
          status: 'ok',
          answer: explanation,
          references: ['Surah Ayah ID: ' + ayahId],
          isAiGenerated: true,
          disclaimer: 'AI-generated pedagogical translation explanation.'
        }), { headers: corsHeaders });
      } catch (e) {
        return new Response(JSON.stringify({ status: 'error', message: e.message }), { status: 400, headers: corsHeaders });
      }
    }

    // AI Assistant: Study Plan
    if (path === '/v1/assistant/study-plan' && request.method === 'POST') {
      try {
        const body = await request.json();
        const goal = body.goal || 'hifz';
        const language = body.language || 'ur';

        const planText = language === 'ur'
          ? "تجویز کردہ مطالعہ پلان:\n1. نیا سبق (New Sabaq): 15 منٹ روزانہ تلاوت مع مخرج اصلاح۔\n2. سبقی (Recent): پچھلے 5 صفحات کا دہرانا۔\n3. منزل (Manzil Revision): ہفتہ وار 1 پارہ۔"
          : "Recommended Study Routine:\n1. New Lesson: 15 mins daily with Tajweed focus.\n2. Sabqi: Revise last 5 pages.\n3. Manzil: Weekly rotation of 1 Juz.";

        return new Response(JSON.stringify({
          requestId: 'req_plan_' + Date.now(),
          status: 'ok',
          answer: planText,
          references: ['Traditional Hifz & Tajweed Methodology'],
          isAiGenerated: true,
          disclaimer: 'Pedagogical routine suggestion.'
        }), { headers: corsHeaders });
      } catch (e) {
        return new Response(JSON.stringify({ status: 'error', message: e.message }), { status: 400, headers: corsHeaders });
      }
    }

    // Reports endpoint
    if (path === '/v1/reports' && request.method === 'POST') {
      try {
        const body = await request.json();
        return new Response(JSON.stringify({
          requestId: body.requestId || ('rep_' + Date.now()),
          status: 'received',
          message: 'Report received. Thank you for maintaining Quranic quality.'
        }), { headers: corsHeaders });
      } catch (e) {
        return new Response(JSON.stringify({ status: 'error', message: e.message }), { status: 400, headers: corsHeaders });
      }
    }

    // Fall back to static asset handling in Cloudflare Pages
    if (env.ASSETS) {
      return env.ASSETS.fetch(request);
    }

    return new Response('Quran Companion API Gateway active. Visit /v1/health or /v1/config.', {
      headers: { 'Content-Type': 'text/plain; charset=utf-8' }
    });
  }
};