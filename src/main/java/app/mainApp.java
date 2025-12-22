package app;
import java.util.*;
import logic.loginDatabase.*;
import logic.welcomeAndSummary.*;

public class mainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        LoginPage loginPage = new LoginPage();
        UserSession session = loginPage.run(sc);

        if (session.username == null) return;

        WelcomeLogicMain welcomeLogic = new WelcomeLogicMain(session.username);
        welcomeLogic.run(session, sc);

        sc.close();
    }
}