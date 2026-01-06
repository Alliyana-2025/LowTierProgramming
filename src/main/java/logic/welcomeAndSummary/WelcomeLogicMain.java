package logic.welcomeAndSummary;

import java.io.*;
import java.util.*;
import API.geminiAPI;
import logic.loginDatabase.UserSession;

public class WelcomeLogicMain {

    public void run(UserSession session, Scanner sc) {
        String summary = generate(session);
        System.out.println("\n=== Weekly Summary ===");
        System.out.println(summary);
    }

    // === METHOD YANG DIPAKAI BACKEND & CONSOLE ===
    public static String generate(UserSession session) {
        List<String> entries = readLastSevenEntries("data" + File.separator + "journals.txt");

        if (entries.isEmpty()) {
            return "No journal entries found.";
        }

        StringBuilder combined = new StringBuilder();
        for (String e : entries) {
            combined.append(e).append("\n---\n");
        }

        String prompt =
                "Write a short weekly emotional summary for user named "
                + session.username + ". "
                + "Suggest improvements. Max 100 words.\n\n"
                + combined;

        geminiAPI api = new geminiAPI();
        return api.geminiResponse(prompt, combined.toString());
    }

    private static List<String> readLastSevenEntries(String filePath) {
        List<String> entries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder entry = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("DATE:")) {
                    if (entry.length() > 0) {
                        entries.add(entry.toString());
                        entry.setLength(0);
                    }
                }
                entry.append(line).append("\n");
            }
            if (entry.length() > 0) entries.add(entry.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entries.subList(Math.max(0, entries.size() - 7), entries.size());
    }
}
