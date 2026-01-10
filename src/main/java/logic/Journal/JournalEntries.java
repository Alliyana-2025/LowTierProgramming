package logic.Journal; 

public class JournalEntries {
    private String date;
    private String title;
    private String journalEntry;
    private String sentiment;
    private String rating;

    public JournalEntries(String date, String title, String journalEntry, String sentiment, String rating) {
        this.date = date;
        this.title = title;
        this.journalEntry = journalEntry;   
        this.sentiment = sentiment;
        this.rating = rating;
    }

    public String toString() {
        return "Date: " + date + ", Title: " + title + ", Entry: " + journalEntry;
    }

    public String getDate() { return date; }
    public String getTitle() { return title; }
    public String getJournalEntry() { return journalEntry; }
    public String getSentiment() { return sentiment; }
    public String getRating() { return rating; }

    public void setDate(String date) { this.date = date; }
    public void setTitle(String title) { this.title = title; }
    public void setJournalEntry(String journalEntry) { this.journalEntry = journalEntry; }
    public void setSentiment(String sentiment) { this.sentiment = sentiment; }
    public void setRating(String rating) { this.rating = rating; }

}
