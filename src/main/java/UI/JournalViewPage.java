package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.Cursor;
import javafx.stage.Stage;

import java.time.LocalDate;

public class JournalViewPage {

    private Scene scene;

    public JournalViewPage(Stage stage) {

        // === ROOT ===
        VBox root = new VBox(25);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #FCEAFF, #E9E8FF);"
        );

        // === HEADER ===
        Label headerTitle = new Label("Daily Journal");
        headerTitle.setFont(Font.font("Segoe UI", 26));
        headerTitle.setTextFill(Color.WHITE);

        Label headerSub = new Label("Capture your thoughts, memories, and moments ✨");
        headerSub.setFont(Font.font(14));
        headerSub.setTextFill(Color.WHITE);

        VBox headerCard = new VBox(8, headerTitle, headerSub);
        headerCard.setPadding(new Insets(20));
        headerCard.setPrefWidth(650);
        headerCard.setStyle(
            "-fx-background-color: linear-gradient(to right, #FF5C8D, #8B72FF);" +
            "-fx-background-radius: 20;"
        );
        headerCard.setEffect(new DropShadow(10, Color.rgb(200,200,200,0.3)));

        // === BACK BUTTON (TOP) ===
        Button backBtnTop = new Button("← Back to Dates");
        backBtnTop.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #8B72FF;" +
            "-fx-font-size: 13;"
        );
        backBtnTop.setOnAction(e -> SceneNavigator.goToJournalDates());

        // === CONTENT CARD ===
        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setPrefWidth(650);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 20;"
        );
        card.setEffect(new DropShadow(5, Color.rgb(200,200,200,0.3)));

        Label title = new Label("📘 Journal Entry");
        title.setFont(Font.font("Segoe UI", 18));

        Label dateLabel = new Label("📅 " + LocalDate.now());
        dateLabel.setTextFill(Color.GRAY);

        TextArea journalContent = new TextArea();
        journalContent.setText("I'm so happy today (contoh)");
        journalContent.setWrapText(true);
        journalContent.setEditable(false);
        journalContent.setPrefHeight(200);
        journalContent.setStyle(
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: #E0D7FF;" +
            "-fx-control-inner-background: #FAFAFF;"
        );

        // === BUTTONS ===
        Button editBtn = new Button("✏️ Edit Journal");
        editBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #FF5C8D, #8B72FF);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 8 20;"
        );
        editBtn.setOnAction(e -> SceneNavigator.goToJournalCreate());

        Button backBtn = new Button("⬅️ Back to Dates");
        backBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: #8B72FF;" +
            "-fx-padding: 8 20;"
        );
        backBtn.setOnAction(e -> SceneNavigator.goToJournalDates());

        // === CURSOR ===
        editBtn.setCursor(Cursor.HAND);
        backBtn.setCursor(Cursor.HAND);
        backBtnTop.setCursor(Cursor.HAND);

        // === POLISH ===
        UIPolish.polishButton(editBtn);
        UIPolish.polishButton(backBtn);
        UIPolish.polishButton(backBtnTop);
        UIPolish.playEntrance(card);

        HBox buttonBox = new HBox(15, editBtn, backBtn);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(
            title,
            dateLabel,
            journalContent,
            buttonBox
        );

        root.getChildren().addAll(headerCard, backBtnTop, card);

        scene = new Scene(root, stage.getWidth(), stage.getHeight());
    }

    public Scene getScene() {
        return scene;
    }
}
