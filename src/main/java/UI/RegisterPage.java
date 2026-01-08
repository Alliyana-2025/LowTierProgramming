package UI;

import java.util.List;

import API.WeatherAPI;
import API.WeatherAPI.GeoLocation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.loginDatabase.UserAuthenticator;

public class RegisterPage {

    private Scene scene;
    private final Stage stage;
    private final SceneNavigator navigator;
    private String activeTab = "register";

    // fields
    private TextField emailField;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField locationField;
    private ComboBox<GeoLocation> locationBox;
    private ComboBox<String> genderBox;
    private DatePicker dobPicker;


    public RegisterPage(Stage stage, SceneNavigator navigator) {
        this.stage = stage;
        this.navigator = navigator;

        /* ================= ROOT ================= */
        StackPane root = new StackPane();
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #E9D5FF, #FBCFE8, #BFDBFE);"
        );

        createBlurEffect(root);

        VBox card = createCard();
        StackPane.setAlignment(card, Pos.CENTER);
        root.getChildren().add(card);

        scene = new Scene(root, stage.getWidth(), stage.getHeight());
    }

    public Scene getScene() {
        return scene;
    }

    /* ================= CARD ================= */

    private VBox createCard() {
        VBox card = new VBox(18);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(40));

        card.setMaxWidth(420);
        card.setMinHeight(Region.USE_PREF_SIZE);
        card.setMaxHeight(Region.USE_PREF_SIZE);

        card.setStyle(
            "-fx-background-color: rgba(255,255,255,0.88);" +
            "-fx-background-radius: 30;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 0, 10);"
        );

        /* ===== ICON WITH BACKGROUND ===== */
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

        Label title = new Label("Start Journaling");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label subtitle = new Label("Create your account to begin");
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

    /* ================= TAB SWITCH ================= */

    private HBox createTabSwitcher() {
        HBox tabs = new HBox(12);
        tabs.setAlignment(Pos.CENTER);

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");

        loginBtn.setPrefWidth(160);
        registerBtn.setPrefWidth(160);

        String activeStyle =
            "-fx-background-color: linear-gradient(to right, #A855F7, #EC4899);" +
            "-fx-text-fill: white; -fx-background-radius: 12;";

        String inactiveStyle =
            "-fx-background-color: #F3E8FF;" +
            "-fx-text-fill: #9333EA; -fx-background-radius: 12;";

        registerBtn.setStyle(activeStyle);
        loginBtn.setStyle(inactiveStyle);

        loginBtn.setOnAction(e -> navigator.goToLogin());

        tabs.getChildren().addAll(loginBtn, registerBtn);
        return tabs;
    }

    /* ================= FORM ================= */

    private VBox createForm() {
        VBox form = new VBox(14);

        emailField = createInput("Email Address", "Email");
        usernameField = createInput("Display Name", "Username");
        /* ===== GENDER ===== */
        genderBox = new ComboBox<>();
        genderBox.getItems().addAll("Male", "Female");
        genderBox.setPromptText("Gender");
        genderBox.setPrefWidth(Double.MAX_VALUE);
        genderBox.setStyle(
            "-fx-background-color: #F9FAFB;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12;"
        );

        /* ===== DATE OF BIRTH ===== */
        dobPicker = new DatePicker();
        dobPicker.setPromptText("YYYY / MM / DD");
        dobPicker.setPrefWidth(Double.MAX_VALUE);
        dobPicker.setStyle(
            "-fx-background-color: #F9FAFB;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12;"
        );
        passwordField = createPassword("Password");
        confirmPasswordField = createPassword("Confirm Password");

        /* ===== LOCATION SEARCH ==== */
        locationField = createInput("Where do you live?", "e.g. Kuala Lumpur");
        ImageView searchIcon = new ImageView(new Image(
            getClass().getResourceAsStream("/images/search.png")
        ));
        searchIcon.setFitWidth(20);
        searchIcon.setFitHeight(20);
        searchIcon.setPreserveRatio(true);
        searchIcon.setStyle("-fx-cursor: hand;");

        StackPane.setAlignment(searchIcon, Pos.CENTER_RIGHT);
        StackPane.setMargin(searchIcon, new Insets(0, 14, 0, 0));

        StackPane locationContainer = new StackPane(
            locationField,
            searchIcon
        );

        searchIcon.setOnMouseClicked(e -> searchAPI());

        Button submitBtn = new Button("Create Account");
        submitBtn.setPrefWidth(Double.MAX_VALUE);
        submitBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #A855F7, #EC4899);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12;"
        );

        submitBtn.setOnAction(e -> handleRegister());

        Button backBtn = new Button("← Back to Login");
        backBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #9333EA;"
        );
        backBtn.setOnAction(e -> navigator.goToLogin());

        form.getChildren().addAll(
            emailField,
            usernameField,
            genderBox,
            dobPicker,
            passwordField,
            confirmPasswordField,
            locationContainer,
            submitBtn,
            backBtn
        );

        return form;
    }

    /* ================= INPUT HELPERS ================= */

    private TextField createInput(String labelText, String placeholder) {
        VBox box = new VBox(6);
        Label label = new Label(labelText);

        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setStyle(
            "-fx-background-color: #F9FAFB;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12;"
        );

        box.getChildren().addAll(label, field);
        return field;
    }

    private PasswordField createPassword(String labelText) {
        VBox box = new VBox(6);
        Label label = new Label(labelText);

        PasswordField field = new PasswordField();
        field.setPromptText("••••••••");
        field.setStyle(
            "-fx-background-color: #F9FAFB;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12;"
        );

        box.getChildren().addAll(label, field);
        return field;
    }

    /* ================= LOGIC ================= */

    private void handleRegister() {
        UserAuthenticator auth = new UserAuthenticator();
        WeatherAPI api = new WeatherAPI();

        if (
            emailField.getText().isEmpty() ||
            usernameField.getText().isEmpty() ||
            genderBox.getValue() == null ||
            dobPicker.getValue() == null ||
            passwordField.getText().isEmpty()
        ) {
            new Alert(Alert.AlertType.WARNING, "Please fill all required fields").show();
            return;
        }

        if (!passwordField.getText().equals(confirmPasswordField.getText())) {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match").show();
            return;
        }

        // Location search service
        GeoLocation loc = locationBox.getValue();
        double lat = loc.lat;
        double lon = loc.lon;

        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String gender = genderBox.getValue();
        String dob = dobPicker.getValue().toString();

        auth.registerUser(username, email, password, gender, dob, lat, lon);

        new Alert(
            Alert.AlertType.INFORMATION,
            "Account created successfully!\nPlease login."
        ).show();

        navigator.goToLogin();
    }

    private void searchAPI() {
        WeatherAPI api = new WeatherAPI();
        String location = locationField.getText();
        List<GeoLocation> locations = api.searchLocations(location);

        if (locations.isEmpty()) {
            new Alert(AlertType.WARNING, "No locations found!").show();
            return;
        }

        locationField.setVisible(false);
        locationField.setManaged(false);

        locationBox = new ComboBox<>();
        locationBox.getItems().addAll(locations);

        locationBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(GeoLocation item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });
        locationBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(GeoLocation item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        });

        locationBox.setPrefWidth(Double.MAX_VALUE);
        locationBox.setStyle(
            "-fx-background-color: #F9FAFB;" +
            "-fx-background-radius: 12;" +
            "-fx-padding: 12;"
        );

        StackPane locationContainer = (StackPane) locationField.getParent();
        locationContainer.getChildren().remove(locationField);
        locationContainer.getChildren().add(locationBox);
    }

    /* ================= BACKGROUND BLUR ================= */

    private void createBlurEffect(StackPane root) {
        Region c1 = new Region();
        c1.setPrefSize(300, 300);
        c1.setStyle("-fx-background-color: #C084FC; -fx-background-radius: 150;");
        c1.setEffect(new GaussianBlur(100));
        c1.setTranslateX(-250);
        c1.setTranslateY(-200);

        Region c2 = new Region();
        c2.setPrefSize(300, 300);
        c2.setStyle("-fx-background-color: #F472B6; -fx-background-radius: 150;");
        c2.setEffect(new GaussianBlur(100));
        c2.setTranslateX(250);
        c2.setTranslateY(-150);

        root.getChildren().addAll(c1, c2);
    }
}
