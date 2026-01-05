package API;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import java.util.Map;

public class GeminiService {

    /**
     * Khusus UI (JavaFX)
     * Aman, tidak trigger DatabaseConfig
     */
    public static String analyzeJournal(String journalText) {

        if (journalText == null || journalText.trim().isEmpty()) {
            return "Journal is empty.";
        }

        try {
            // 🔐 Load .env HANYA DI SINI
            Map<String, String> env = EnvLoader.loadEnv("data/.env");
            String apiKey = env.get("GEMINI_TOKEN");

            if (apiKey == null) {
                return "Gemini API key not found in .env";
            }

            Client client = Client.builder()
                    .apiKey(apiKey)
                    .build();

            String prompt =
                "Analyze the sentiment of the following journal. " +
                "Return POSITIVE, NEGATIVE, or NEUTRAL and give a short insight.\n\n"
                + journalText;

            GenerateContentResponse response =
                client.models.generateContent(
                    "gemini-2.5-flash",
                    prompt,
                    null
                );

            client.close();
            return response.text();

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to connect to Gemini AI.";
        }
    }
}
