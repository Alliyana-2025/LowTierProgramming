package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.Cursor;
import javafx.stage.Stage;

import logic.summaryLogic.SummaryLogic;;

public class SummaryPage {

    private Scene scene;

    public SummaryPage(Stage stage) {

        /* ================= ROOT ================= */
        VBox root = new VBox(30);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #FCEAFF, #E9E8FF);"
        );

        /* ================= HEADER ================= */
        Label headerTitle = new Label("Weekly Summary");
        headerTitle.setFont(Font.font("Segoe UI", 26));
        headerTitle.setTextFill(Color.WHITE);

        ImageView headerStar = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/star.png"))
        );
        headerStar.setFitWidth(14);
        headerStar.setFitHeight(14);

        Label headerSubText = new Label("A reflection of your mood & journaling");
        headerSubText.setFont(Font.font(14));
        headerSubText.setTextFill(Color.WHITE);

        HBox headerSub = new HBox(6, headerSubText, headerStar);
        headerSub.setAlignment(Pos.CENTER_LEFT);

        VBox headerText = new VBox(8, headerTitle, headerSub);

        Button backBtn = new Button("← Back");
        backBtn.setCursor(Cursor.HAND);
        backBtn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.25);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 18;" +
            "-fx-padding: 8 20;"
        );
        backBtn.setOnAction(e -> SceneNavigator.goToJournalHub());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerContent = new HBox(20, headerText, spacer, backBtn);
        headerContent.setAlignment(Pos.CENTER_LEFT);

        VBox headerCard = new VBox(headerContent);
        headerCard.setPadding(new Insets(25));
        headerCard.setMaxWidth(900);
        headerCard.setStyle(
            "-fx-background-color: linear-gradient(to right, #FF5C8D, #8B72FF);" +
            "-fx-background-radius: 28;"
        );
        headerCard.setEffect(new DropShadow(15, Color.rgb(0,0,0,0.25)));

        /* ================= CHART ================= */
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(1, 5, 1);

        LineChart<String, Number> moodChart =
            new LineChart<>(xAxis, yAxis);
        moodChart.setLegendVisible(false);
        moodChart.setAnimated(false);
        moodChart.setPrefHeight(260);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Mon", 3));
        series.getData().add(new XYChart.Data<>("Tue", 4));
        series.getData().add(new XYChart.Data<>("Wed", 2));
        series.getData().add(new XYChart.Data<>("Thu", 5));
        series.getData().add(new XYChart.Data<>("Fri", 4));
        moodChart.getData().add(series);

        ImageView summaryIcon = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/summary.png"))
        );
        summaryIcon.setFitWidth(18);
        summaryIcon.setFitHeight(18);

        Label chartTitleText = new Label("Mood Trend");
        chartTitleText.setFont(Font.font("Segoe UI", 16));

        HBox chartTitle = new HBox(8, summaryIcon, chartTitleText);
        chartTitle.setAlignment(Pos.CENTER_LEFT);

        VBox chartCard = new VBox(18, chartTitle, moodChart);
        chartCard.setPadding(new Insets(30));
        chartCard.setMaxWidth(900);
        chartCard.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 28;"
        );
        chartCard.setEffect(new DropShadow(10, Color.rgb(0,0,0,0.2)));

        /* ================= INSIGHT ================= */
        ImageView insightStar = new ImageView(
            new Image(getClass().getResourceAsStream("assets/icons/star.png"))
        );
        insightStar.setFitWidth(16);
        insightStar.setFitHeight(16);

        Label insightTitleText = new Label("Weekly Insight");
        insightTitleText.setFont(Font.font("Segoe UI", 18));

        HBox insightTitle = new HBox(8, insightStar, insightTitleText);
        insightTitle.setAlignment(Pos.CENTER_LEFT);

        Label insightText = new Label(
            "Your mood tends to improve towards the end of the week.\n" +
            "Keep journaling to maintain emotional awareness."
        );
        insightText.setWrapText(true);
        insightText.setTextFill(Color.GRAY);

        VBox insightCard = new VBox(12, insightTitle, insightText);
        insightCard.setPadding(new Insets(30));
        insightCard.setMaxWidth(900);
        insightCard.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 28;"
        );
        insightCard.setEffect(new DropShadow(10, Color.rgb(0,0,0,0.2)));

        /* ================= ASSEMBLE ================= */
        root.getChildren().addAll(
            headerCard,
            chartCard,
            insightCard
        );

        scene = new Scene(root, stage.getWidth(), stage.getHeight());
    }

    public Scene getScene() {
        return scene;
    }
}
