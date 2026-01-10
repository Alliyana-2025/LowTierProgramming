package logic.Journal;

import java.time.LocalDate;
import java.util.*;
import java.io.*;
import logic.loginDatabase.*;
import API.GeminiAPI;

public class JournalPage {
    private static final String JOURNAL_FILE = "data" + File.separator + "journals.txt";

    public static String saveJournals(String title, String journalText, UserSession session) {
        List<JournalEntries> entries = JournalList.getJournalEntries();
        String sentiment = "";
        String rating = "";
        try {
            GeminiAPI api = new GeminiAPI();

            String prompt1 = "Analyze the given entry and give a short insight on how the writer feels, and how their day can be improved. "
                        + "Take into account the user's data, such as AGE (given the DoB) and where user lives (given Latitude and Longitude), and also the weather that are also given if it helps with better response. "
                        + "Do not include the user's data EXCEPT the name, inside the response text "
                        + "Write in less than 50 words. \n"
                        + journalText + "\n"
                        + session;
            sentiment = api.geminiResponse(prompt1);

            String prompt2 = "Analyze the given entry and give a rating on a scale of 0-10 of how positive the user's mood is."
                        + "Take into account the user's data, such as AGE (given the DoB) and where user lives (given Latitude and Longitude), and also the weather that are also given if it helps with better response. "
                        + "Reply with ONLY a SINGLE number. No other words/sentences. \n"
                        + journalText + "\n"
                        + session;
            rating = api.geminiResponse(prompt2);

            LocalDate today = LocalDate.now();

            entries.removeIf(e -> e.getDate().equals(today.toString()));

            Collections.reverse(entries);
            entries.add(new JournalEntries(today.toString(), title, journalText, sentiment, rating));
                
            FileWriter fw = new FileWriter(JOURNAL_FILE, false);
            for (JournalEntries e : entries) {
                fw.write("TITLE: " + e.getTitle() + "\n");
                fw.write("DATE: " + e.getDate() + "\n");
                fw.write("JOURNAL: " + e.getJournalEntry() + "\n");
                fw.write("SENTIMENT: " + e.getSentiment() + "\n");
                fw.write("RATING: " + e.getRating() + "\n");
                fw.write("---------------------\n");
            }
            fw.close();

            return sentiment;
        } catch (Exception e) {
            e.printStackTrace();
        } return null;
    }
}