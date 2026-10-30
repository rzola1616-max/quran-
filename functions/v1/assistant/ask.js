export async function onRequestPost({ request, env }) {
  const corsHeaders = {
    "Content-Type": "application/json; charset=utf-8",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "POST, OPTIONS",
    "Access-Control-Allow-Headers": "Content-Type"
  };

  try {
    const body = await request.json();
    const question = body.question || "";
    const language = body.language || "ur";
    const referenceIds = body.referenceIds || [];

    const geminiKey = env?.GEMINI_API_KEY || (typeof process !== "undefined" ? process.env?.GEMINI_API_KEY : null);
    let answer = "";
    let references = referenceIds.slice();

    if (geminiKey && question.trim().length > 0) {
      try {
        const prompt = "You are the Quran Learning Assistant in Quran Companion.\n" +
          "Answer respectfully, educationally, and accurately in " + (language === "ur" ? "Urdu" : "English") + ".\n" +
          "Never fabricate Quranic text. Quote verified references.\n" +
          "Question: " + question;

        const geminiRes = await fetch("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiKey, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            contents: [{ parts: [{ text: prompt }] }]
          })
        });

        if (geminiRes.ok) {
          const data = await geminiRes.json();
          answer = data.candidates?.[0]?.content?.parts?.[0]?.text || "";
        }
      } catch (err) {
        console.error("Gemini API call failed:", err);
      }
    }

    if (!answer) {
      if (language === "ur") {
        answer = "تجوید کے اصول قرآن کریم کو درست مخارج اور ترتیل سے پڑھنے کی رہنمائی کرتے ہیں۔ ایپ میں 30 روزہ مکمل اسباق اور صوتی تصحیح کا نظام آف لائن بھی میسر ہے۔";
        references.push("Curriculum: Tajweed & Makharij");
      } else {
        answer = "Tajweed rules govern the accurate phonetics and measured cadence of Quranic recitation. Structured daily lessons and phonetic feedback are available offline in the app.";
        references.push("Curriculum: Tajweed & Makharij");
      }
    }

    return new Response(JSON.stringify({
      requestId: "req_" + Date.now(),
      status: "ok",
      answer: answer.trim(),
      references: references,
      isAiGenerated: true,
      disclaimer: "This explanation supports learning and is not a substitute for qualified scholarship."
    }), { headers: corsHeaders });

  } catch (error) {
    return new Response(JSON.stringify({
      status: "error",
      code: "VALIDATION_FAILED",
      message: error.message
    }), { status: 400, headers: corsHeaders });
  }
}

export async function onRequestOptions() {
  return new Response(null, {
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Access-Control-Allow-Headers": "Content-Type"
    }
  });
}
