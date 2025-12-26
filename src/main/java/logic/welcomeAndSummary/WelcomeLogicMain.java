package logic.welcomeAndSummary;

import logic.Journal.*;
import logic.summaryLogic.SummaryLogic;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Scanner;
import API.WeatherAPI;
import logic.loginDatabase.*;

public class WelcomeLogicMain {

    public void run(UserSession session, Scanner sc) {

        LocalTime timeNow = LocalTime.now(ZoneId.of("GMT+8"));

        String greeting;

        if (timeNow.isBefore(LocalTime.NOON)) {
            greeting = "Good Morning";
        } else if (timeNow.isBefore(LocalTime.of(17, 0))) {
            greeting = "Good Afternoon";
        } else {
            greeting = "Good Evening";
        }

        System.out.println("\n" + greeting + "\n");

        WeatherAPI api = new WeatherAPI();
        api.displayWeather(session);

        JournalPage journalPage = new JournalPage();
        SummaryLogic summaryPage = new SummaryLogic();

        System.out.println("Choose an option:\n1. Journal Page\n2. Weekly Summary\n3. Exit App");
        boolean running = true;

        while (running) {
            System.out.print("Enter choice: ");
            String input = sc.nextLine();
            int option = Integer.parseInt(input);
            switch (option) {
                case 1: {
                    running = false;
                    journalPage.run(session, sc);
                    break;
                }
                case 2: {
                    running = false;
                    summaryPage.run(session, sc);
                    break;
                    
                }
                case 3: {
                    System.out.println("See you again next time!");
                    System.exit(0);
                }
                default: {
                    System.out.println("Invalid input");
                    break;
                }
            }
        }
    }
}
