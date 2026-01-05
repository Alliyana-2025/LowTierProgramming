package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.Cursor;
import javafx.stage.Stage;

public class JournalDatesPage {

    private Scene scene;

    public JournalDatesPage(Stage stage) {

        /* ================= ROOT ================= */
        VBox root = new VBox(30);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #FCEAFF, #E9E8FF);");

        /* ================= HEADER ================= */
        StackPane header = new StackPane();
        header.setMaxWidth(900);
        header.setPadding(new Insets(35));
        header.setStyle(
            "-fx-background-color: linear-gradient(to right, #FF5C8D, #8B72FF);" +
            "-fx-background-radius: 24;"
        );
        header.setEffect(new DropShadow(15, Color.rgb(0,0,0,0.25)));

        Label starLeft = new Label("✨");
        Label starRight = new Label("✨");
        starLeft.setFont(Font.font(22));
        starRight.setFont(Font.font(22));

        StackPane.setAlignment(starLeft, Pos.TOP_LEFT);
        StackPane.setAlignment(starRight, Pos.TOP_RIGHT);
        StackPane.setMargin(starLeft, new Insets(10));
        StackPane.setMargin(starRight, new Insets(10));

        VBox headerContent = new VBox(6);
        headerContent.setAlignment(Pos.CENTER_LEFT);

        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        ImageView bookIcon = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/daily-book.png"))
        );
        bookIcon.setFitWidth(26);
        bookIcon.setFitHeight(26);

        Label title = new Label("Daily Journal");
        title.setFont(Font.font("Segoe UI", 28));
        title.setTextFill(Color.WHITE);

        titleRow.getChildren().addAll(bookIcon, title);


        Label subtitle = new Label("Capture your thoughts, memories, and moments ✏️");
        subtitle.setTextFill(Color.web("rgba(255,255,255,0.9)"));
        subtitle.setFont(Font.font(14));

        headerContent.getChildren().addAll(titleRow, subtitle);

        Button backBtn = new Button("Back");
        backBtn.setCursor(Cursor.HAND);
        backBtn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.25);" +
            "-fx-text-fill: white; -fx-background-radius: 16;" +
            "-fx-padding: 8 20;"
        );
        backBtn.setOnAction(e -> SceneNavigator.goToJournalHub());

        StackPane.setAlignment(backBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(backBtn, new Insets(20));

        header.getChildren().addAll(headerContent, starLeft, starRight, backBtn);

        /* ================= MAIN CARD ================= */
        VBox mainCard = new VBox(25);
        mainCard.setMaxWidth(900);
        mainCard.setPadding(new Insets(35));
        mainCard.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 24;"
        );
        mainCard.setEffect(new DropShadow(10, Color.rgb(0,0,0,0.2)));

        /* ===== Journal Header ===== */
        HBox journalHeader = new HBox(15);
        journalHeader.setAlignment(Pos.CENTER_LEFT);

        ImageView summaryIcon = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/summary.png"))
        );
        summaryIcon.setFitWidth(24);
        summaryIcon.setFitHeight(24);

        StackPane calIcon = new StackPane(summaryIcon);
        calIcon.setPrefSize(50, 50);
        calIcon.setStyle("-fx-background-color: #F3E8FF; -fx-background-radius: 16;");


        VBox headerText = new VBox(2);
        Label journalTitle = new Label("Journal Dates");
        journalTitle.setFont(Font.font("Segoe UI", 18));
        journalTitle.setTextFill(Color.web("#581C87"));

        Label journalSub = new Label("✨ Your daily memories ✨");
        journalSub.setFont(Font.font(12));
        journalSub.setTextFill(Color.web("#7C3AED"));

        headerText.getChildren().addAll(journalTitle, journalSub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addBtn = new Button("➕ Add Journal");
        addBtn.setCursor(Cursor.HAND);
        addBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #8B5CF6, #7C3AED);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 18;" +
            "-fx-padding: 6 16;"
        );
        addBtn.setOnAction(e -> SceneNavigator.goToJournalCreate());

        ImageView ribbonIcon = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/pita.png"))
        );
        ribbonIcon.setFitWidth(22);
        ribbonIcon.setFitHeight(22);

        journalHeader.getChildren().addAll(
            calIcon, headerText, spacer, addBtn, ribbonIcon
        );


        /* ===== Instruction ===== */
        HBox instruction = new HBox(12);
        instruction.setAlignment(Pos.CENTER_LEFT);
        instruction.setPadding(new Insets(18));
        instruction.setStyle(
            "-fx-background-color: #F3E8FF;" +
            "-fx-background-radius: 16;"
        );

        ImageView rainbowIcon = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/rainbow.png"))
        );
        rainbowIcon.setFitWidth(22);
        rainbowIcon.setFitHeight(22);


        Label instructionText = new Label(
            "Select a date to view journal, or edit the journal for today! 🎨"
        );
        instructionText.setFont(Font.font(13));
        instructionText.setTextFill(Color.web("#7C3AED"));
        instructionText.setWrapText(true);

        instruction.getChildren().addAll(rainbowIcon, instructionText);

        /* ===== JOURNAL LIST ===== */
        VBox journalList = new VBox(15);
        journalList.getChildren().addAll(
            createJournalCard("December 20, 2025", "2026-01-05"),
            createJournalCard("My Second Journal", "2026-01-04")
        );

        ScrollPane scrollPane = new ScrollPane(journalList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(240);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        /* ===== FOOTER ===== */
        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER);

        ImageView sakuraIcon = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/sakura.png"))
        );
        sakuraIcon.setFitWidth(20);
        sakuraIcon.setFitHeight(20);

        Label footerText = new Label("✨ Keep writing, keep growing! ✨");
        footerText.setTextFill(Color.web("#7C3AED"));
        footerText.setFont(Font.font(13));

        footer.getChildren().addAll(sakuraIcon, footerText);

        mainCard.getChildren().addAll(
            journalHeader,
            instruction,
            scrollPane,
            footer
        );

        root.getChildren().addAll(header, mainCard);

        scene = new Scene(root, stage.getWidth(), stage.getHeight());
    }

    /* ================= JOURNAL CARD ================= */
    private HBox createJournalCard(String title, String date) {

        HBox dateCard = new HBox(15);
        dateCard.setAlignment(Pos.CENTER_LEFT);
        dateCard.setPadding(new Insets(22));
        dateCard.setCursor(Cursor.HAND);
        dateCard.setStyle(
            "-fx-border-color: #DDD6FE;" +
            "-fx-border-radius: 16;" +
            "-fx-background-radius: 16;"
        );

        ImageView bookIcon = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/book.png"))
        );
        bookIcon.setFitWidth(26);
        bookIcon.setFitHeight(26);

        StackPane circleIcon = new StackPane(bookIcon);
        circleIcon.setPrefSize(60, 60);
        circleIcon.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #C084FC, #A78BFA);" +
            "-fx-background-radius: 30;"
        );


        VBox dateInfo = new VBox(6);
        Label dateText = new Label(title);
        dateText.setFont(Font.font("Segoe UI", 16));
        dateText.setTextFill(Color.web("#581C87"));

        Label isoDate = new Label(date);
        isoDate.setFont(Font.font(12));
        isoDate.setTextFill(Color.web("#7C3AED"));

        dateInfo.getChildren().addAll(dateText, isoDate);
        HBox.setHgrow(dateInfo, Priority.ALWAYS);

        /* ===== ACTION ICONS (PNG) ===== */
        ImageView editIcon = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/edit.png"))
        );
        editIcon.setFitWidth(18);
        editIcon.setFitHeight(18);

        ImageView deleteIcon = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/delete.png"))
        );
        deleteIcon.setFitWidth(18);
        deleteIcon.setFitHeight(18);

        Button editBtn = new Button("", editIcon);
        Button deleteBtn = new Button("", deleteIcon);

        editBtn.setCursor(Cursor.HAND);
        deleteBtn.setCursor(Cursor.HAND);

        editBtn.setStyle("-fx-background-color: transparent;");
        deleteBtn.setStyle("-fx-background-color: transparent;");

        editBtn.setOnAction(e -> SceneNavigator.goToJournalCreate());

        deleteBtn.setOnAction(e -> {
            dateCard.setVisible(false);
            dateCard.setManaged(false);
        });

        HBox actions = new HBox(10, editBtn, deleteBtn);

        dateCard.getChildren().addAll(circleIcon, dateInfo, actions);

        return dateCard;
    }

    public Scene getScene() {
        return scene;
    }
}
