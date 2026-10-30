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
    const ayahId = body.ayahId || 1001;

    const explanation = language === "ur"
      ? "یہ آیت مبارکہ اللہ رب العزت کی توحید، اس کی بے پایاں رحمت اور صراط مستقیم کی ابدی رہنمائی کو بیان کرتی ہے۔"
      : "This holy verse establishes the oneness of Allah, His boundless mercy, and divine guidance upon the straight path.";

    return new Response(JSON.stringify({
      requestId: "req_trans_" + Date.now(),
      status: "ok",
      answer: explanation,
      references: ["Ayah Reference: " + ayahId],
      isAiGenerated: true,
      disclaimer: "AI-generated pedagogical translation explanation."
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
