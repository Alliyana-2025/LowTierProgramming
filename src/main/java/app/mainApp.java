package app;
import java.util.*;
import API.API;
import logic.loginDatabase.*;
import logic.welcomeAndSummary.*;
import logic.welcomelogic.*;
import UI.*;


public class mainApp {
    public static void main(String[] args) {

        LoginPage loginPage = new LoginPage();
        String username = loginPage.run();

        if (username == null) return;

        WelcomeLogicMain welcomeLogic = new WelcomeLogicMain(username);
        welcomeLogic.run();
    }
}
