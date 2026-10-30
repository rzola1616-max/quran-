export async function onRequestPost({ request }) {
  const corsHeaders = {
    "Content-Type": "application/json; charset=utf-8",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "POST, OPTIONS",
    "Access-Control-Allow-Headers": "Content-Type"
  };

  try {
    const body = await request.json();
    const language = body.language || "ur";
    const minutes = body.minutesPerDay || 20;

    const plan = language === "ur"
      ? `تجویز کردہ روزانہ شیڈول (${minutes} منٹ):\n1. نیا سبق: ${Math.round(minutes * 0.4)} منٹ (3 تا 5 آیات مخرج کے ساتھ)\n2. سبقی: ${Math.round(minutes * 0.35)} منٹ (پچھلے اسباق کا اعادہ)\n3. منزل: ${Math.round(minutes * 0.25)} منٹ (گزشتہ پاروں کا دور)`
      : `Recommended Daily Routine (${minutes} mins):\n1. New Lesson: ${Math.round(minutes * 0.4)}m (3-5 verses with Tajweed)\n2. Sabqi: ${Math.round(minutes * 0.35)}m (Recent revision)\n3. Manzil: ${Math.round(minutes * 0.25)}m (Long-term rotation)`;

    return new Response(JSON.stringify({
      requestId: "req_plan_" + Date.now(),
      status: "ok",
      answer: plan,
      references: ["Traditional 3-Step Hifz & Tajweed Framework"],
      isAiGenerated: true,
      disclaimer: "Pedagogical study framework suggestion."
    }), { headers: corsHeaders });
  } catch (e) {
    return new Response(JSON.stringify({ status: "error", message: e.message }), { status: 400, headers: corsHeaders });
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
