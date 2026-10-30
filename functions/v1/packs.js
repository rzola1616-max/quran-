export async function onRequestGet() {
  return new Response(JSON.stringify({
    packs: [
      {
        packId: "quran_uthmani_full",
        type: "quran_text",
        title: "King Fahd Complex Uthmani Mushaf",
        languageCode: "ar",
        latestVersion: "1.0.0",
        sizeBytes: 2450000
      },
      {
        packId: "urdu_jalandhri",
        type: "translation",
        title: "Urdu Translation (Fateh Muhammad Jalandhri)",
        languageCode: "ur",
        latestVersion: "1.0.0",
        sizeBytes: 1820000
      },
      {
        packId: "audio_mishary_alafasy",
        type: "audio",
        title: "Mishary Rashid Alafasy Recitation",
        languageCode: "ar",
        latestVersion: "1.0.0",
        sizeBytes: 14500000
      }
    ]
  }), {
    headers: {
      "Content-Type": "application/json; charset=utf-8",
      "Access-Control-Allow-Origin": "*"
    }
  });
}
