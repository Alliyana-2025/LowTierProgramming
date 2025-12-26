package app;
import java.util.*;
import logic.loginDatabase.*;
import logic.welcomeAndSummary.*;
import UI.LoginFrame;

public class mainApp {
    public static void main(String[] args) {
        new LoginFrame();
        Scanner sc = new Scanner(System.in);

        LoginPage loginPage = new LoginPage();
        UserSession session = loginPage.run(sc);

        if (session.username == null) return;

        WelcomeLogicMain welcomeLogic = new WelcomeLogicMain();
        welcomeLogic.run(session, sc);

        sc.close();
    }
}