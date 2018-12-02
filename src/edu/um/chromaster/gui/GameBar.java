package edu.um.chromaster.gui;

import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class GameBar extends HBox {

    private GraphElement graphElement;

    public GameBar(GraphElement graphElement) {
        this.getStyleClass().add("game-bar");
        this.setMaxSize(1280, 720 * 0.05);
        this.graphElement = graphElement;
        this.setup();
    }

    private void setup() {

        for(GraphElement.HintTypes hintType : GraphElement.HintTypes.values()) {
            Button button = new Button(hintType.getDisplayName());
            button.getStyleClass().add("button");
            button.setOnAction(e -> {
                if(hintType.isVisual()) {
                    graphElement.displayHints(hintType);
                } else {
                    Tooltip tooltip = new Tooltip(String.valueOf(graphElement.getHint(hintType)));
                    System.out.println(button.getLayoutX() + " - " + button.getLayoutY() + " - " + button.getScaleX());

                    Bounds bounds = GameBar.this.localToScreen(button.getLayoutBounds());
                    tooltip.show(button, bounds.getMaxX(), bounds.getMinY());
                    tooltip.setContentDisplay(ContentDisplay.TOP);
                    tooltip.getStyleClass().add("tooltip");
                    tooltip.setAutoHide(true);
                    tooltip.setAutoFix(true);
                    tooltip.setHideOnEscape(true);
                    button.setTooltip(tooltip);
                }
            });
            this.getChildren().add(button);
        }

    }

}
