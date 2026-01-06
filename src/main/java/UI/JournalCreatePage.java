package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.Cursor;
import javafx.stage.Stage;

import logic.Journal.JournalManager;
import java.time.LocalDate;

public class JournalCreatePage {

    private Scene scene;

    public JournalCreatePage(Stage stage) {

        /* ================= ROOT ================= */
        VBox root = new VBox(30);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #FCEAFF, #E9E8FF);"
        );

        /* ================= HEADER (SAMA DENGAN JOURNAL DATES) ================= */
        StackPane header = new StackPane();
        header.setMaxWidth(900);
        header.setPadding(new Insets(35));
        header.setStyle(
            "-fx-background-color: linear-gradient(to right, #FF5C8D, #8B72FF);" +
            "-fx-background-radius: 24;"
        );
        header.setEffect(new DropShadow(15, Color.rgb(0,0,0,0.25)));

        // ✨ Sparkles
        Label starLeft = new Label("✨");
        Label starRight = new Label("✨");
        starLeft.setFont(Font.font(22));
        starRight.setFont(Font.font(22));

        StackPane.setAlignment(starLeft, Pos.TOP_LEFT);
        StackPane.setAlignment(starRight, Pos.TOP_RIGHT);
        StackPane.setMargin(starLeft, new Insets(10));
        StackPane.setMargin(starRight, new Insets(10));

        // === Header Content ===
        VBox headerContent = new VBox(6);
        headerContent.setAlignment(Pos.CENTER_LEFT);

        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        ImageView bookIcon = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/daily-book.png"))
        );
        bookIcon.setFitWidth(26);
        bookIcon.setFitHeight(26);

        Label headerTitle = new Label("Daily Journal");
        headerTitle.setFont(Font.font("Segoe UI", 28));
        headerTitle.setTextFill(Color.WHITE);

        titleRow.getChildren().addAll(bookIcon, headerTitle);

        Label headerSub = new Label("Capture your thoughts, memories, and moments ✨");
        headerSub.setFont(Font.font(14));
        headerSub.setTextFill(Color.web("rgba(255,255,255,0.9)"));

        headerContent.getChildren().addAll(titleRow, headerSub);

        // === Back Button ===
        Button backBtn = new Button("Back");
        backBtn.setCursor(Cursor.HAND);
        backBtn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.25);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 16;" +
            "-fx-padding: 8 20;"
        );
        backBtn.setOnAction(e -> SceneNavigator.goToJournalDates());

        StackPane.setAlignment(backBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(backBtn, new Insets(20));

        header.getChildren().addAll(
            headerContent,
            starLeft,
            starRight,
            backBtn
        );

        /* ================= MAIN CARD ================= */
        VBox card = new VBox(18);
        card.setPadding(new Insets(30));
        card.setMaxWidth(900);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 24;"
        );
        card.setEffect(new DropShadow(10, Color.rgb(200,200,200,0.3)));

        /* ===== TITLE ROW ===== */
        HBox titleRowForm = new HBox(10);
        titleRowForm.setAlignment(Pos.CENTER_LEFT);

        ImageView pencilIcon = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/pencil.png"))
        );
        pencilIcon.setFitWidth(20);
        pencilIcon.setFitHeight(20);

        Label titleLabel = new Label("Create Journal Entry");
        titleLabel.setFont(Font.font("Segoe UI", 18));
        titleLabel.setTextFill(Color.web("#581C87"));

        titleRowForm.getChildren().addAll(pencilIcon, titleLabel);

        Label dateLabel = new Label("📅 " + LocalDate.now());
        dateLabel.setTextFill(Color.GRAY);

        /* ===== TITLE FIELD ===== */
        TextField titleField = new TextField();
        titleField.setPromptText("Journal title...");
        titleField.setStyle(
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: #E0D7FF;" +
            "-fx-padding: 10;"
        );

        /* ===== CONTENT ===== */
        TextArea journalArea = new TextArea();
        journalArea.setPromptText("Start writing your thoughts...");
        journalArea.setWrapText(true);
        journalArea.setPrefHeight(220);
        journalArea.setStyle(
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: #E0D7FF;"
        );

        /* ===== SAVE BUTTON ===== */
        Button saveBtn = new Button("💾 Save Journal");
        saveBtn.setCursor(Cursor.HAND);
        saveBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #FF5C8D, #8B72FF);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 10 22;"
        );

        saveBtn.setOnAction(e -> {
            if (titleField.getText().isEmpty() || journalArea.getText().isEmpty()) {
                new Alert(Alert.AlertType.WARNING,
                    "Please fill in both journal title and content."
                ).show();
                return;
            }

            JournalManager.saveJournal(
                UserSession.getUsername(),
                titleField.getText(),
                journalArea.getText()
            );

            new Alert(Alert.AlertType.INFORMATION,
                "Journal saved successfully.\nSentiment analyzed by Gemini ✨"
            ).show();

            SceneNavigator.goToJournalDates();
        });

        saveBtn.disableProperty().bind(
            titleField.textProperty().isEmpty()
                .or(journalArea.textProperty().isEmpty())
        );

        /* ===== ASSEMBLE ===== */
        card.getChildren().addAll(
            titleRowForm,
            dateLabel,
            titleField,
            journalArea,
            saveBtn
        );

        root.getChildren().addAll(header, card);

        scene = new Scene(root, stage.getWidth(), stage.getHeight());
    }

    public Scene getScene() {
        return scene;
    }
}
