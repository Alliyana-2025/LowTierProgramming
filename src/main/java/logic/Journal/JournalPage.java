package logic.Journal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;
import logic.welcomeAndSummary.*;
import logic.loginDatabase.*;
import API.GeminiAPI;

public class JournalPage {
    private static final String JOURNAL_FILE = "data" + File.separator + "journals.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static Map<LocalDate, JournalEntry> journals = new TreeMap<>();

    // Inner class to store both journal content and AI summary
    private static class JournalEntry {
        String content;
        String aiSummary;

        JournalEntry(String content, String aiSummary) {
            this.content = content;
            this.aiSummary = aiSummary;
        }
    }

    public void run(UserSession session, Scanner scanner) {
        loadJournals(scanner);
        //showJournalsPage(session, scanner);
    }

    private static void loadJournals(Scanner scanner) {
        File file = new File(JOURNAL_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            LocalDate currentDate = null;
            StringBuilder content = new StringBuilder();
            StringBuilder aiSummary = new StringBuilder();
            boolean readingAISummary = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("DATE:")) {
                    // Save previous entry if exists
                    if (currentDate != null) {
                        journals.put(currentDate, new JournalEntry(
                            content.toString().trim(), 
                            aiSummary.toString().trim()
                        ));
                        content = new StringBuilder();
                        aiSummary = new StringBuilder();
                    }
                    currentDate = LocalDate.parse(line.substring(5).trim(), DATE_FORMATTER);
                    readingAISummary = false;
                } else if (line.startsWith("AI_SUMMARY:")) {
                    readingAISummary = true;
                } else if (line.equals("---")) {
                    if (currentDate != null) {
                        journals.put(currentDate, new JournalEntry(
                            content.toString().trim(), 
                            aiSummary.toString().trim()
                        ));
                        content = new StringBuilder();
                        aiSummary = new StringBuilder();
                        currentDate = null;
                        readingAISummary = false;
                    }
                } else {
                    if (readingAISummary) {
                        if (aiSummary.length() > 0) {
                            aiSummary.append("\n");
                        }
                        aiSummary.append(line);
                    } else {
                        if (content.length() > 0) {
                            content.append("\n");
                        }
                        content.append(line);
                    }
                }
            }

            // Save last entry if exists
            if (currentDate != null) {
                journals.put(currentDate, new JournalEntry(
                    content.toString().trim(), 
                    aiSummary.toString().trim()
                ));
            }
        } catch (IOException e) {
            System.out.println("Error loading journals: " + e.getMessage());
        }
    }

    public static String saveJournals(String title, String journalText, UserSession session) {
        File file = new File(JOURNAL_FILE);
        file.getParentFile().mkdirs();
        try {
            GeminiAPI api = new GeminiAPI();

            String prompt = "Analyze the given entry and give a short insight on how the writer feels, and how their day can be improved. "
                        + "Take into account the user's data, such as AGE (given the DoB) and where user lives (given Latitude and Longitude), and also the weather that are also given if it helps with better response. "
                        + "Do not include the user's data EXCEPT the name, inside the response text "
                        + "Write in less than 50 words. \n"
                        + journalText + "\n"
                        + session;
            String sentiment = api.geminiResponse(prompt);

            File dir = new File("data");
            if (!dir.exists()) dir.mkdirs();

            FileWriter fw = new FileWriter(JOURNAL_FILE, true);
            fw.write("TITLE: " + title + "\n");
            fw.write("DATE: " + LocalDate.now() + "\n");
            fw.write("Journal:\n" + journalText + "\n");
            fw.write("Sentiment:\n" + sentiment + "\n");
            fw.write("---------------------\n");
            fw.close();

            return sentiment;
        } catch (Exception e) {
            e.printStackTrace();
        } return null;
    }

    // private static void showJournalsPage(UserSession session, Scanner scanner) {
    //     while (true) {
    //         System.out.println("\nJournals Page");
    //         System.out.println("===");
    //         System.out.println("Journal Dates");
    //         System.out.println("===");

    //         LocalDate today = LocalDate.now();
    //         List<LocalDate> dates = new ArrayList<>(journals.keySet());
            
    //         if (!dates.contains(today)) {
    //             dates.add(today);
    //         }
            
    //         Collections.sort(dates);

    //         for (int i = 0; i < dates.size(); i++) {
    //             LocalDate date = dates.get(i);
    //             String dateStr = date.format(DATE_FORMATTER);
    //             if (date.equals(today)) {
    //                 System.out.println((i + 1) + ". " + dateStr + " (Today)");
    //             } else {
    //                 System.out.println((i + 1) + ". " + dateStr);
    //             }
    //         }

    //         boolean todayHasJournal = journals.containsKey(today);
    //         if (todayHasJournal) {
    //             System.out.print("\nSelect a date to view journal, edit the journal for today, or enter '-1' to exit the program :\n> ");
    //         } else {
    //             System.out.print("\nSelect a date to view journal, create a new journal for today, or enter '-1' to exit the program :\n> ");
    //         }

    //         String input = scanner.nextLine().trim();
            
    //         if (input.isEmpty()) {
    //             continue;
    //         }

    //         try {
    //             int choice = Integer.parseInt(input);

    //             if (choice == -1){
    //                 WelcomeLogicMain welcomePage = new WelcomeLogicMain();
    //                 welcomePage.run(session, scanner);
    //             }

    //             if ((choice != -1 && choice < 1)  || choice > dates.size()) {
    //                 System.out.println("Invalid selection. Please try again.");
    //                 continue;
    //             }

    //             LocalDate selectedDate = dates.get(choice - 1);
                
    //             if (selectedDate.equals(today)) {
    //                 handleTodayJournal(today, scanner);
    //             } else {
    //                 viewJournal(selectedDate, scanner);
    //             }
    //         } catch (NumberFormatException e) {
    //             System.out.println("Invalid input. Please enter a number.");
    //         }
    //     }
    // }

    // private static void handleTodayJournal(LocalDate today, Scanner scanner) {
    //     if (!journals.containsKey(today)) {
    //         createJournal(today, scanner);
    //     } else {
    //         showTodayOptions(today, scanner);
    //     }
    // }

    // private static void createJournal(LocalDate date, Scanner scanner) {
    //     System.out.println("\nEnter your journal entry for " + date.format(DATE_FORMATTER) + ":");
    //     System.out.print("> ");
    //     String entry = scanner.nextLine();
        
        // Ask if user wants AI summary
        // System.out.print("\nDo you want an AI summary of your current Entry? (yes/no): ");
        // String wantSummary = scanner.nextLine().trim().toLowerCase();
        
        // String aiSummary = "";
        // if (wantSummary.equals("yes") || wantSummary.equals("y")) {
        //     aiSummary = generateAISummary(entry);
        //     if (aiSummary != null && !aiSummary.isEmpty()) {
        //         System.out.println("\n" + aiSummary);
        //     }
        // }
        
        // journals.put(date, new JournalEntry(entry, aiSummary));
        // saveJournals();
        
    //     System.out.println("\nJournal saved successfully!");
    //     System.out.println("Would you like to:");
    //     System.out.println("1. View Journal");
    //     System.out.println("2. Edit Journal");
    //     System.out.println("3. Back to Dates");
    //     System.out.print("> ");
        
    //     String choice = scanner.nextLine().trim();
        
    //     switch (choice) {
    //         case "1":
    //             viewJournal(date, scanner);
    //             break;
    //         case "2":
    //             editJournal(date, scanner);
    //             break;
    //         case "3":
    //         default:
    //             break;
    //     }
    // }

    // private static void editJournal(LocalDate date, Scanner scanner) {
    //     System.out.println("\nEdit your journal entry for " + date.format(DATE_FORMATTER) + ":");
    //     System.out.print("> ");
    //     String entry = scanner.nextLine();
        
    //     // Ask if user wants AI summary
    //     System.out.print("\nDo you want an AI summary of your updated Entry? (yes/no): ");
    //     String wantSummary = scanner.nextLine().trim().toLowerCase();
        
    //     String aiSummary = "";
    //     if (wantSummary.equals("yes") || wantSummary.equals("y")) {
    //         aiSummary = generateAISummary(entry);
    //         if (aiSummary != null && !aiSummary.isEmpty()) {
    //             System.out.println("\n" + aiSummary);
    //         }
    //     } else {
    //         // Keep existing AI summary if user doesn't want a new one
    //         JournalEntry existingEntry = journals.get(date);
    //         if (existingEntry != null) {
    //             aiSummary = existingEntry.aiSummary;
    //         }
    //     }
        
    //     journals.put(date, new JournalEntry(entry, aiSummary));
    //     saveJournals();
        
    //     System.out.println("\nJournal updated successfully!");
    // }

    // private static void showTodayOptions(LocalDate today, Scanner scanner) {
    //     System.out.println("\nWould you like to:");
    //     System.out.println("1. View Journal");
    //     System.out.println("2. Edit Journal");
    //     System.out.println("3. Back to Dates");
    //     System.out.print("> ");
        
    //     String choice = scanner.nextLine().trim();
        
    //     switch (choice) {
    //         case "1":
    //             viewJournal(today, scanner);
    //             break;
    //         case "2":
    //             editJournal(today, scanner);
    //             break;
    //         case "3":
    //         default:
    //             break;
    //     }
    // }

    private static void viewJournal(LocalDate date, Scanner scanner) {
        System.out.println("\n=== Journal Entry for " + date.format(DATE_FORMATTER) + " ===");
        
        JournalEntry entry = journals.get(date);
        if (entry != null && entry.content != null && !entry.content.isEmpty()) {
            System.out.println(entry.content);
            
            // Display AI summary if it exists
            if (entry.aiSummary != null && !entry.aiSummary.isEmpty()) {
                System.out.println("\n--- AI Summary ---");
                System.out.println(entry.aiSummary);
            }
        } else {
            System.out.println("No journal entry for this date.");
        }
        
        System.out.println("\nPress Enter to go back.");
        System.out.print("> ");
        scanner.nextLine();
    }

    private static String generateAISummary(String journalContent) {
        System.out.println("\nGenerating AI summary...");
        
        try {
            GeminiAPI api = new GeminiAPI();
            String prompt = "Please provide a brief summary of the following journal entry in 2-3 sentences:\n\n" + journalContent;
            String response = api.geminiResponse(prompt);
            
            if (response != null && !response.isEmpty()) {
                return response;
            } else {
                System.out.println("Failed to generate AI summary.");
                return "";
            }
        } catch (Exception e) {
            System.out.println("Error generating AI summary: " + e.getMessage());
            return "";
        }
    }
}