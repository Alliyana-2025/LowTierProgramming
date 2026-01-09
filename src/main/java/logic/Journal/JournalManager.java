package logic.Journal;

import API.GeminiAPI;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JournalManager {

    /* ================= SAVE JOURNAL ================= */
    public static void saveJournal(String username, String title, String journalText) {
        try {
            GeminiAPI api = new GeminiAPI();

            String prompt = "Analyze the sentiment of this journal entry and return a short insight.";
            String sentiment = api.geminiResponse(prompt);

            File dir = new File("data");
            if (!dir.exists()) dir.mkdirs();

            FileWriter fw = new FileWriter("data/" + username + ".txt", true);
            fw.write("TITLE: " + title + "\n");
            fw.write("DATE: " + LocalDate.now() + "\n");
            fw.write("Journal:\n" + journalText + "\n");
            fw.write("Sentiment:\n" + sentiment + "\n");
            fw.write("---------------------\n");
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* ================= READ JOURNAL TITLES ================= */
    public static List<JournalEntry> getJournalEntries(String username) {
        List<JournalEntry> entries = new ArrayList<>();

        File file = new File("data/" + username + ".txt");
        if (!file.exists()) return entries;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String title = null;
            LocalDate date = null;

            while ((line = br.readLine()) != null) {

                if (line.startsWith("TITLE: ")) {
                    title = line.substring(7);
                }

                if (line.startsWith("DATE: ")) {
                    date = LocalDate.parse(line.substring(6));
                }

                if (line.startsWith("---------------------")) {
                    if (title != null && date != null) {
                        entries.add(new JournalEntry(title, date));
                    }
                    title = null;
                    date = null;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return entries;
    }

    /* ================= MODEL ================= */
    public static class JournalEntry {
        public String title;
        public LocalDate date;

        public JournalEntry(String title, LocalDate date) {
            this.title = title;
            this.date = date;
        }
    }
}
