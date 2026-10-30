export async function onRequestGet() {
  return new Response(JSON.stringify({
    status: "ok",
    apiVersion: "1",
    time: new Date().toISOString(),
    service: "Quran Learning Companion Cloudflare Gateway",
    features: {
      offlineQuran: true,
      onlineGeminiAssistant: true,
      dailyLessonEngine: true,
      phoneticCorrection: true
    }
  }), {
    headers: {
      "Content-Type": "application/json; charset=utf-8",
      "Access-Control-Allow-Origin": "*"
    }
  });
}
