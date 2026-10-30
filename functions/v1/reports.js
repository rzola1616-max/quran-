export async function onRequestPost({ request }) {
  const corsHeaders = {
    "Content-Type": "application/json; charset=utf-8",
    "Access-Control-Allow-Origin": "*",
    "Access-Control-Allow-Methods": "POST, OPTIONS",
    "Access-Control-Allow-Headers": "Content-Type"
  };

  try {
    const body = await request.json();
    return new Response(JSON.stringify({
      requestId: body.requestId || ("rep_" + Date.now()),
      status: "received",
      message: "Report logged for editorial review. JazakAllah Khair."
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
