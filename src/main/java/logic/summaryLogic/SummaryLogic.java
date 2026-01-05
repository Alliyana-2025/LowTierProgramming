package logic.summaryLogic;

import java.io.*;
import java.util.*;
import API.geminiAPI;
import logic.welcomeAndSummary.WelcomeLogicMain;
import logic.loginDatabase.UserSession;

public class SummaryLogic {

    public void run(UserSession session, Scanner sc) {
        List<String> lastSevenEntries = readLastSevenEntries("data"+File.separator+"journals.txt");
        if (lastSevenEntries.isEmpty()) {
            System.out.println("No journal entries found.");
        }

        StringBuilder combinedEntries = new StringBuilder();
        for (String entry : lastSevenEntries) {
            combinedEntries.append(entry).append("\n---\n");
        }


        String prompt = "Write a short summary of the user's mood for the week using these entries, "
                        + "take into account the user's data, such as AGE (given the DoB) and where user lives (given Latitude and Longitude) that is also given if it helps with better response. "
                        + "Do not include the user's data EXCEPT the name, inside the response text "
                        + "Suggest improvements that the user can implement to improve their mood and coming weeks based on their data/profile. "
                        + "Write in less than 100 words. \n"
                        + combinedEntries + "\n"
                        + session;

        geminiAPI api = new geminiAPI();
        String response = api.geminiResponse(prompt, combinedEntries.toString());

        System.out.println("=== Weekly Summary Page ===");
        System.out.println(response);
        System.out.print("Type -1 to return to Main Page: ");
        int ret = Integer.parseInt(sc.nextLine());
        if (ret == -1) {
            WelcomeLogicMain welcomePage = new WelcomeLogicMain();
            welcomePage.run(session, sc);
        }
        else throw new RuntimeException("Invalid input!!!");
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