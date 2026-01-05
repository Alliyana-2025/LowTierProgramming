package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class JournalIntroPage {

    private Scene scene;

    public JournalIntroPage(Stage stage) {

        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #FCEAFF, #E9E8FF);"
        );

        // === ICON ===
        ImageView icon = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/book-open.png"))
        );
        icon.setFitWidth(64);
        icon.setFitHeight(64);
        icon.setPreserveRatio(true);


        // === TITLE ===
        Label title = new Label("Welcome to Your Journal");
        title.setFont(Font.font("Segoe UI", 26));
        title.setTextFill(Color.web("#7C3AED"));

        ImageView starIcon = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/star.png"))
        );
        starIcon.setFitWidth(14);
        starIcon.setFitHeight(14);
        starIcon.setPreserveRatio(true);

        Label subtitleText = new Label(
            "Your safe space to write, reflect, and grow"
        );
        subtitleText.setFont(Font.font(15));
        subtitleText.setTextFill(Color.GRAY);

        HBox subtitle = new HBox(6, subtitleText, starIcon);
        subtitle.setAlignment(Pos.CENTER);


        // === INFO CARD ===
        VBox infoCard = new VBox(12);
        infoCard.setAlignment(Pos.CENTER_LEFT);
        infoCard.setPadding(new Insets(25));
        infoCard.setMaxWidth(420);
        infoCard.setStyle(
            "-fx-background-color: rgba(255,255,255,0.85);" +
            "-fx-background-radius: 20;"
        );
        infoCard.setEffect(new DropShadow(8, Color.rgb(0,0,0,0.15)));

        infoCard.getChildren().addAll(
            createInfoRow("assets/icons/love.png", "Express your thoughts freely"),
            createInfoRow("assets/icons/star.png", "Track your beautiful journey"),
            createInfoRow("assets/icons/moon.png", "Cherish your memories")
        );


        // === BUTTON ===
        ImageView heartIcon = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/hearts.png"))
        );
        heartIcon.setFitWidth(16);
        heartIcon.setFitHeight(16);

        Label btnText = new Label("Start Writing");
        btnText.setTextFill(Color.WHITE);

        HBox btnContent = new HBox(8, btnText, heartIcon);
        btnContent.setAlignment(Pos.CENTER);

        Button startBtn = new Button();
        startBtn.setGraphic(btnContent);

        startBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #EC4899, #8B5CF6);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 30;" +
            "-fx-padding: 12 30;"
        );

        startBtn.setOnAction(e ->
            SceneNavigator.goToJournalHub()
        );

        Label footer = new Label(
            "Every day is a new page in your story"
        );
        footer.setTextFill(Color.GRAY);

        root.getChildren().addAll(
            icon, title, subtitle, infoCard, startBtn, footer
        );

        scene = new Scene(root, stage.getWidth(), stage.getHeight());
    }

    private HBox createInfoRow(String iconPath, String text) {
        ImageView icon = new ImageView(
            new Image(getClass().getResourceAsStream(iconPath))
        );
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        icon.setPreserveRatio(true);

        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", 14));
        label.setTextFill(Color.web("#6B7280"));

        HBox row = new HBox(12, icon, label);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }


    public Scene getScene() {
        return scene;
    }
}
