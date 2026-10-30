export async function onRequestGet() {
  return new Response(JSON.stringify({
    apiVersion: "1",
    minAppVersion: 1,
    features: {
      assistantEnabled: true,
      onlineRecitationEnabled: false,
      offlineFirstQuran: true,
      phoneticCorrectionEngine: "active"
    },
    maintenanceMessage: null
  }), {
    headers: {
      "Content-Type": "application/json; charset=utf-8",
      "Access-Control-Allow-Origin": "*"
    }
  });
}
