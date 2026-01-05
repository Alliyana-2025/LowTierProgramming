package UI;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class UIPolish {

    // === BUTTON EFFECT ===
    public static void polishButton(Button btn) {
        btn.setOnMouseEntered(e ->
            btn.setStyle(btn.getStyle() + "-fx-opacity: 0.9;")
        );
        btn.setOnMouseExited(e ->
            btn.setStyle(btn.getStyle() + "-fx-opacity: 1.0;")
        );
        btn.setOnMousePressed(e -> {
            btn.setScaleX(0.97);
            btn.setScaleY(0.97);
        });
        btn.setOnMouseReleased(e -> {
            btn.setScaleX(1.0);
            btn.setScaleY(1.0);
        });
    }

    // === CARD HOVER ===
    public static void polishCard(Node card) {
        card.setOnMouseEntered(e ->
            card.setEffect(new DropShadow(12, Color.rgb(180,160,255,0.45)))
        );
        card.setOnMouseExited(e ->
            card.setEffect(new DropShadow(5, Color.rgb(200,200,200,0.3)))
        );
    }

    // === PAGE ENTRANCE ===
    public static void playEntrance(Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(350), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(350), node);
        slide.setFromY(12);
        slide.setToY(0);

        fade.play();
        slide.play();
    }

    // === TEXTAREA FOCUS ===
    public static void polishTextArea(TextArea area) {
        area.focusedProperty().addListener((o, oldV, focused) -> {
            if (focused) {
                area.setStyle(
                    "-fx-background-radius: 15;" +
                    "-fx-border-radius: 15;" +
                    "-fx-border-color: #8B72FF;"
                );
            } else {
                area.setStyle(
                    "-fx-background-radius: 15;" +
                    "-fx-border-radius: 15;" +
                    "-fx-border-color: #E0D7FF;"
                );
            }
        });
    }
}
