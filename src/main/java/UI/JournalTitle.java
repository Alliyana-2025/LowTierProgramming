package UI;

import java.time.LocalDate;
import java.util.*;

public class JournalTitle {

    /* ===== MODEL ===== */
    public static class Entry {
        public String title;
        public LocalDate date;

        public Entry(String title) {
            this.title = title;
            this.date = LocalDate.now();
        }
    }

    /* ===== STORAGE (MOCK, NO DB) ===== */
    private static final Map<String, List<Entry>> userJournals = new HashMap<>();

    /* ===== SAVE TITLE ===== */
    public static void saveTitle(String username, String title) {
        userJournals.putIfAbsent(username, new ArrayList<>());
        userJournals.get(username).add(new Entry(title));
    }

    /* ===== GET ALL TITLES ===== */
    public static List<Entry> getTitles(String username) {
        return userJournals.getOrDefault(username, new ArrayList<>());
    }

    /* ===== GET LATEST ===== */
    public static Entry getLatestTitle(String username) {
        List<Entry> list = userJournals.get(username);
        if (list == null || list.isEmpty()) return null;
        return list.get(list.size() - 1);
    }
}
