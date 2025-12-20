package logic.summaryLogic;

import java.io.*;
import java.util.*;
import API.geminiAPI;

public class SummaryPage {

    public static void main(String[] args) {
        List<String> lastSevenEntries = readLastSevenEntries("data/journal.txt");
        if (lastSevenEntries.isEmpty()) {
            System.out.println("No journal entries found.");
            return;
        }

        StringBuilder combinedEntries = new StringBuilder();
        for (String entry : lastSevenEntries) {
            combinedEntries.append(entry).append("\n---\n");
        }

        String prompt = "Create a weekly summary page from the following journal entries:\n" + combinedEntries;

        geminiAPI api = new geminiAPI();
        String response = api.geminiRespones(prompt, combinedEntries.toString());

        System.out.println("=== Weekly Summary Page ===");
        System.out.println(response);
    }

    private static List<String> readLastSevenEntries(String filePath) {
        List<String> entries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder currentEntry = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("DATE: ")) {
                    if (currentEntry.length() > 0) {
                        entries.add(currentEntry.toString().trim());
                        currentEntry.setLength(0);
                    }
                }
                currentEntry.append(line).append("\n");
            }
            if (currentEntry.length() > 0) entries.add(currentEntry.toString().trim());
        } catch (IOException e) {
            System.err.println("Error reading journal file: " + e.getMessage());
        }

        int size = entries.size();
        return entries.subList(Math.max(0, size - 7), size);
    }
}
