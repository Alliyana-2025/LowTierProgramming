package logic.loginDatabase;

import java.io.File;
import java.io.IOException;

import UI.SceneNavigator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginPage {

    private Scene scene;
    private final Stage stage;
    private final SceneNavigator navigator;
    private final String JOURNAL_FILE = "data"+File.separator+"journals.txt";

    public LoginPage(Stage stage, SceneNavigator navigator) {
        this.stage = stage;
        this.navigator = navigator;

        // ===== ROOT BACKGROUND =====
        StackPane root = new StackPane();
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #e9d5ff, #fbcfe8, #dbeafe);"
        );

        createBlurEffect(root);

        VBox card = createLoginCard();
        StackPane.setAlignment(card, Pos.CENTER);
        root.getChildren().add(card);

        scene = new Scene(root, stage.getWidth(), stage.getHeight());
    }

    public Scene getScene() {
        return scene;
    }

    /* ================= UI BUILDER ================= */

    private VBox createLoginCard() {
        VBox card = new VBox(18);

        card.setMaxWidth(420);
        card.setMinHeight(Region.USE_PREF_SIZE);
        card.setMaxHeight(Region.USE_PREF_SIZE);

        card.setPadding(new Insets(40));
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.85);" +
            "-fx-background-radius: 30;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 0, 10);"
        );

        // ===== PROFILE ICON (IMAGE) =====
        Image profileImg = new Image(
            getClass().getResourceAsStream("/images/profile-icon.png")
        );

        ImageView profileIcon = new ImageView(profileImg);
        profileIcon.setFitWidth(48);
        profileIcon.setFitHeight(48);
        profileIcon.setPreserveRatio(true);

        StackPane iconWrapper = new StackPane(profileIcon);
        iconWrapper.setPadding(new Insets(12));
        iconWrapper.setMaxWidth(Region.USE_PREF_SIZE);
        iconWrapper.setMaxHeight(Region.USE_PREF_SIZE);
        iconWrapper.setStyle(
            "-fx-background-color: rgba(139, 92, 246, 0.15);" +
            "-fx-background-radius: 999;"
        );


        Label title = new Label("Welcome Back");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitle = new Label("Sign in to continue your journey");
        subtitle.setStyle("-fx-text-fill: #6b7280;");

        HBox tabSwitcher = createTabSwitcher();
        VBox form = createForm();

        card.getChildren().addAll(
        iconWrapper,
        title,
        subtitle,
        tabSwitcher,
        form
    );

        return card;
    }

    private HBox createTabSwitcher() {
        HBox tabs = new HBox(10);
        tabs.setAlignment(Pos.CENTER);

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");

        loginBtn.setPrefWidth(180);
        registerBtn.setPrefWidth(180);

        String activeStyle =
            "-fx-background-color: linear-gradient(to right, #a855f7, #ec4899);" +
            "-fx-text-fill: white; -fx-background-radius: 12;";

        String inactiveStyle =
            "-fx-background-color: #f3e8ff;" +
            "-fx-text-fill: #9333ea; -fx-background-radius: 12;";

        loginBtn.setStyle(activeStyle);
        registerBtn.setStyle(inactiveStyle);

        loginBtn.setOnAction(e -> {
            loginBtn.setStyle(activeStyle);
            registerBtn.setStyle(inactiveStyle);
        });

        registerBtn.setOnAction(e -> {
            navigator.goToRegister();
        });

        tabs.getChildren().addAll(loginBtn, registerBtn);
        return tabs;
    }

    private VBox createForm() {
        VBox form = new VBox(15);

        /* ================= EMAIL ================= */
        VBox emailBox = new VBox(5);
        Label emailLabel = new Label("Email Address");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle(
            "-fx-background-color: #f9fafb;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12;"
        );

        emailBox.getChildren().addAll(emailLabel, emailField);

        /* ================= PASSWORD (ICON TOGGLE) ================= */
        VBox passwordBox = new VBox(5);
        Label passwordLabel = new Label("Password");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("••••••••");

        TextField passwordText = new TextField();
        passwordText.setPromptText("••••••••");
        passwordText.setVisible(false);
        passwordText.setManaged(false);

        String inputStyle =
            "-fx-background-color: #f9fafb;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12 44 12 12;";

        passwordField.setStyle(inputStyle);
        passwordText.setStyle(inputStyle);

        // === ICON DARI CANVA (40x40 → DISCALE) ===
        Image eyeOpenImg = new Image(
            getClass().getResourceAsStream("/images/eye-open.png")
        );
        Image eyeClosedImg = new Image(
            getClass().getResourceAsStream("/images/eye-closed.png")
        );

        ImageView eyeIcon = new ImageView(eyeOpenImg);
        eyeIcon.setFitWidth(20);
        eyeIcon.setFitHeight(20);
        eyeIcon.setPreserveRatio(true);
        eyeIcon.setStyle("-fx-cursor: hand;");

        StackPane.setAlignment(eyeIcon, Pos.CENTER_RIGHT);
        StackPane.setMargin(eyeIcon, new Insets(0, 14, 0, 0));

        StackPane passwordContainer = new StackPane(
            passwordField,
            passwordText,
            eyeIcon
        );

        eyeIcon.setOnMouseClicked(e -> {
            if (passwordField.isVisible()) {
                passwordText.setText(passwordField.getText());
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                passwordText.setVisible(true);
                passwordText.setManaged(true);
                eyeIcon.setImage(eyeClosedImg);
            } else {
                passwordField.setText(passwordText.getText());
                passwordText.setVisible(false);
                passwordText.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
                eyeIcon.setImage(eyeOpenImg);
            }
        });

        passwordBox.getChildren().addAll(passwordLabel, passwordContainer);

        /* ================= SUBMIT ================= */
        UserAuthenticator auth = new UserAuthenticator();
        Button submitBtn = new Button("Sign In");
        submitBtn.setPrefWidth(Double.MAX_VALUE);
        submitBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #a855f7, #ec4899);" +
            "-fx-text-fill: white; -fx-background-radius: 12; -fx-padding: 12;"
        );

        submitBtn.setOnAction(e -> {
            String emailOrUsername = emailField.getText();
            String password = passwordField.isVisible()
                ? passwordField.getText()
                : passwordText.getText();
            System.out.println("Get user and pass successful");
            if (emailOrUsername.isEmpty() || password.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please fill all fields").show();
                return;
            }

            if(auth.authenticate(emailOrUsername, password)) {
                System.out.println("Login successful");
                navigator.setSession(emailOrUsername);
                File file = new File(JOURNAL_FILE);
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } navigator.goToWelcome();
            } else new Alert(Alert.AlertType.WARNING, "Wrong username or password! Please try again.").show();
        });

        form.getChildren().addAll(emailBox, passwordBox, submitBtn);
        return form;
    }

    private void createBlurEffect(StackPane root) {
        Region circle1 = new Region();
        circle1.setPrefSize(300, 300);
        circle1.setStyle("-fx-background-color: #c084fc; -fx-background-radius: 150;");
        circle1.setEffect(new GaussianBlur(80));
        circle1.setTranslateX(-200);
        circle1.setTranslateY(-150);

        Region circle2 = new Region();
        circle2.setPrefSize(300, 300);
        circle2.setStyle("-fx-background-color: #f9a8d4; -fx-background-radius: 150;");
        circle2.setEffect(new GaussianBlur(80));
        circle2.setTranslateX(200);
        circle2.setTranslateY(-100);

        root.getChildren().addAll(circle1, circle2);
    }
}
