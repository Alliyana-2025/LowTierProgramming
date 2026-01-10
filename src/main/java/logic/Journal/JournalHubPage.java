package logic.Journal;

import UI.SceneNavigator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class JournalHubPage {

    private Scene scene;
    private final Stage stage;
    private final SceneNavigator navigator;

    public JournalHubPage(Stage stage, SceneNavigator navigator) {
        this.stage = stage;
        this.navigator = navigator;

        VBox root = new VBox(30);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #FCEAFF, #E9E8FF);"
        );

        /* ================= HEADER ================= */
        Label greeting = new Label(
            "Good Afternoon, " + navigator.getSession().username + " ✨"
        );
        greeting.setFont(Font.font("Segoe UI", 22));
        greeting.setTextFill(Color.web("#7C3AED"));

        Label subtitle = new Label(
            "Welcome back to your personal journal space ✨"
        );
        subtitle.setTextFill(Color.GRAY);

        VBox header = new VBox(6, greeting, subtitle);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setMaxWidth(900);

        /* ================= CARDS ================= */

        VBox journalCard = createCard(
            "/images/daily-journal.png",
            "My Journals",
            "Create, edit, and view your journal entries.\nExpress your thoughts and feelings freely.",
            "Open Journals →"
        );
        journalCard.setOnMouseClicked(e ->
            navigator.goToJournalList()
        );

        VBox summaryCard = createCard(
            "/images/summary.png",
            "Weekly Mood Summary",
            "Track your emotional journey and see how your mood changes throughout the week.",
            "View Summary →"
        );
        summaryCard.setOnMouseClicked(e ->
            navigator.goToSummary()
        );

        HBox cardRow = new HBox(30, journalCard, summaryCard);
        cardRow.setAlignment(Pos.CENTER);

        /* ================= QUOTE ================= */
        Label quote = new Label(
            "\"Every day is a new page in your story. Make it a beautiful one.\" 💕"
        );
        quote.setTextFill(Color.web("#8B5CF6"));
        quote.setStyle(
            "-fx-background-color: rgba(255,255,255,0.85);" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 15 30;"
        );

        /* ================= BACK TO HOME (BOX STYLE) ================= */
        Button backBtn = new Button("← Back to Home");
        backBtn.setCursor(javafx.scene.Cursor.HAND);
        backBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #FF5C8D, #8B72FF);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13;" +
            "-fx-background-radius: 18;" +
            "-fx-padding: 8 22;"
        );
        backBtn.setEffect(new DropShadow(6, Color.rgb(0,0,0,0.2)));
        backBtn.setOnAction(e -> navigator.goToWelcome());

        root.getChildren().addAll(header, cardRow, quote, backBtn);

        scene = new Scene(root, stage.getWidth(), stage.getHeight());
    }

    /* ================= CARD BUILDER ================= */
    private VBox createCard(
        String iconPath,
        String titleText,
        String descText,
        String linkText
    ) {
        ImageView icon = new ImageView(
            new Image(getClass().getResourceAsStream(iconPath))
        );
        icon.setFitWidth(42);
        icon.setFitHeight(42);
        icon.setPreserveRatio(true);

        Label title = new Label(titleText);
        title.setFont(Font.font("Segoe UI", 18));

        Label desc = new Label(descText);
        desc.setWrapText(true);
        desc.setTextFill(Color.GRAY);

        Label link = new Label(linkText);
        link.setTextFill(Color.web("#7C3AED"));

        VBox card = new VBox(14, icon, title, desc, link);
        card.setPadding(new Insets(25));
        card.setPrefWidth(380);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 25;"
        );
        card.setEffect(new DropShadow(10, Color.rgb(0,0,0,0.15)));

        return card;
    }

    public Scene getScene() {
        return scene;
    }
}
