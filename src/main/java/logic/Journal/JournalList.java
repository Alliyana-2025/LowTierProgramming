package logic.Journal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import UI.SceneNavigator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

public class JournalList {

    private Scene scene;
    private final Stage stage;
    private final SceneNavigator navigator;
    private List<JournalEntries> entries = getJournalEntries();

    public JournalList(Stage stage, SceneNavigator navigator) {
        this.stage = stage;
        this.navigator = navigator;

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
            new Image(getClass().getResourceAsStream("/images/daily-book.png"))
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
        backBtn.setOnAction(e -> navigator.goToJournalHub());

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
            new Image(getClass().getResourceAsStream("/images/summary.png"))
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

        Button addBtn = new Button("");
        addBtn.setCursor(Cursor.HAND);
        addBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #8B5CF6, #7C3AED);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 18;" +
            "-fx-padding: 6 16;"
        );

        JournalEntries todayEntry = entries.stream()
            .filter(e -> e.getDate().equals(LocalDate.now().toString()))
            .findFirst()
            .orElse(null);

        JournalMode mode = todayEntry == null ? JournalMode.CREATE : JournalMode.EDIT;

        addBtn.setText(todayEntry == null ? "➕ Add Journal" : "✏ Edit Journal");
        addBtn.setOnAction(e -> {
            navigator.setMode(mode);
            navigator.setEntry(todayEntry);
            navigator.goToJournalView();
        });

        ImageView ribbonIcon = new ImageView(
            new Image(getClass().getResourceAsStream("/images/pita.png"))
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
            new Image(getClass().getResourceAsStream("/images/rainbow.png"))
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

        for (JournalEntries entry : entries) {
            journalList.getChildren().add(createJournalCard(entry));
        }

        ScrollPane scrollPane = new ScrollPane(journalList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(240);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        /* ===== FOOTER ===== */
        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER);

        ImageView sakuraIcon = new ImageView(
            new Image(getClass().getResourceAsStream("/images/sakura.png"))
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
    private HBox createJournalCard(JournalEntries entry) {

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
            new Image(getClass().getResourceAsStream("/images/book.png"))
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
        Label dateText = new Label(entry.getTitle());
        dateText.setFont(Font.font("Segoe UI", 16));
        dateText.setTextFill(Color.web("#581C87"));

        Label isoDate = new Label(entry.getDate());
        isoDate.setFont(Font.font(12));
        isoDate.setTextFill(Color.web("#7C3AED"));

        dateInfo.getChildren().addAll(dateText, isoDate);
        HBox.setHgrow(dateInfo, Priority.ALWAYS);

        /* ===== ACTION ICONS (PNG) ===== */
        ImageView viewIcon = new ImageView(
            new Image(getClass().getResourceAsStream("/images/eye-open.png"))
        );
        viewIcon.setFitWidth(24);
        viewIcon.setFitHeight(24);

        LocalDate parsedDate = LocalDate.parse(entry.getDate());

        Button viewBtn = new Button("", viewIcon);
        viewBtn.setCursor(Cursor.HAND);
        viewBtn.setStyle("-fx-background-color: transparent;");
        viewBtn.setOnAction(e -> {
            navigator.setMode(JournalMode.VIEW);
            navigator.setEntry(entry);
            navigator.setDate(parsedDate);
            navigator.goToJournalView();
        });

        HBox actions = new HBox(viewBtn);

        dateCard.getChildren().addAll(circleIcon, dateInfo, actions);

        return dateCard;
    }

    public static List<JournalEntries> getJournalEntries() {
        List<JournalEntries> entries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data" + File.separator + "journals.txt"))) {
            String line;
            String title = null;
            String date = null;
            StringBuilder journal = new StringBuilder();
            StringBuilder sentiment = new StringBuilder();
            String rating = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("TITLE:")) {
                    title = line.replace("TITLE:", "").trim();
                } else if (line.startsWith("DATE:")) {
                    date = line.replace("DATE:", "").trim();
                } else if (line.startsWith("JOURNAL:")) {
                    journal.setLength(0);
                    journal.append(line.replace("JOURNAL:", "").trim());
                } else if (line.startsWith("SENTIMENT:")) {
                    sentiment.setLength(0);
                    sentiment.append(line.replace("SENTIMENT:", "").trim());
                } else if (line.startsWith("RATING:")) {
                    rating = line.replace("RATING:", "").trim();
                } else if (line.startsWith("---------------------")) {
                    if (title != null && date != null) {
                        entries.add(new JournalEntries(
                            date,
                            title,
                            journal.toString().trim(),
                            sentiment.toString().trim(),
                            rating
                        ));
                    }
                    title = null;
                    date = null;
                    journal.setLength(0);
                    sentiment.setLength(0);
                } else {
                    // If multiline journal or sentiment, append the line
                    if (journal.length() > 0 && sentiment.length() == 0) {
                        journal.append("\n").append(line);
                    } else if (sentiment.length() > 0) {
                        sentiment.append("\n").append(line);
                    }
                }
            }
            if (title != null && date != null) {
                entries.add(new JournalEntries(
                    date,
                    title,
                    journal.toString().trim(),
                    sentiment.toString().trim(),
                    rating
                ));
            }
        } catch (IOException e) {
            new Alert(Alert.AlertType.WARNING, "Couldn't fetch journal entries!").show();
        }
        Collections.reverse(entries);
        return entries;
    }


    public Scene getScene() {
        return scene;
    }
}
