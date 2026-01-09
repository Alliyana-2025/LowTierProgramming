package API;

import java.util.*;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import API.WeatherAPI.WeatherResponse;
import logic.loginDatabase.UserSession;

public class GeminiAPI {
        
    public String geminiResponse(String prompt) {
        // Load environment variables from .env file (custom loader)
        Map<String, String> env = EnvLoader.loadEnv("data/.env");

        try {
            Client client = Client.builder().apiKey(env.get("GEMINI_TOKEN")).build();
            
            GenerateContentResponse responseGemini = client.models.generateContent("gemini-2.5-flash", prompt, null);
            
            client.close();
            return responseGemini.text();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSummaryForSession(String prompt, UserSession session) {
        if (session.getCachedSummary() != null) {
            return session.getCachedSummary();
        }
        String freshSummary = geminiResponse(prompt);
        session.setCachedSummary(freshSummary);
        return freshSummary;
    } 
}
