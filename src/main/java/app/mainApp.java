package app;
<<<<<<< HEAD
import java.util.*;
import logic.loginDatabase.*;
import logic.welcomeAndSummary.*;
import logic.welcomelogic.*;
import UI.*;


public class mainApp {
    public static void main(String[] args) {
=======
import java.util.Scanner;
import API.API;

public class mainApp {
    public static void main(String[] args) {
        System.out.println(System.getenv("OPENWEATHER_API_KEY"));

        API api = new API();
        logic.ghazy g = new logic.ghazy();
>>>>>>> ghazy-feature
        Scanner sc = new Scanner(System.in);

        LoginPage loginPage = new LoginPage();
        String username = loginPage.run(sc);

<<<<<<< HEAD
        if (username == null) return;

        WelcomeLogicMain welcomeLogic = new WelcomeLogicMain(username);
        welcomeLogic.run(sc);
=======
        System.out.print("Enter journal: ");
        String journal = sc.nextLine();
        String sentiment = g.analyze(journal);
        System.out.println(sentiment);
>>>>>>> ghazy-feature

        sc.close();
    }
}