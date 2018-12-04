package edu.um.chromaster.gui;

import edu.um.chromaster.Game;
import javafx.collections.FXCollections;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameBar extends HBox {

    private Map<Button, Double> timeConstraints = new LinkedHashMap<>();
    public Set<GraphElement.HintTypes> _42list = new HashSet<>();

    private ComboBox<GraphElement.RenderType> renderTypes = new ComboBox<>(FXCollections.observableArrayList(
            GraphElement.RenderType.values()
    ));

    private GraphElement graphElement;

    public GameBar(GraphElement graphElement, double timeInMilliseconds) {

        this._42list.add(GraphElement.HintTypes.SOLUTION);
        this._42list.add(GraphElement.HintTypes.LOWER_BOUND);
        this._42list.add(GraphElement.HintTypes.UPPER_BOUND);

        for(GraphElement.HintTypes e : Stream.of(GraphElement.HintTypes.values()).sorted(Comparator.comparingDouble(GraphElement.HintType::getTimeConstraint)).collect(Collectors.toList())) {
            Button b = new Button(e.getDisplayName());
            b.getProperties().put("type", e);
            timeConstraints.put(b, e.getTimeConstraint() * timeInMilliseconds);
        }

        this.getStyleClass().add("game-bar");
        renderTypes.getStyleClass().add("combo-box");
        renderTypes.setMaxHeight(new Button().getMaxHeight());
        this.setMaxSize(1280, 720 * 0.05);
        this.graphElement = graphElement;
        this.setup();
    }


    public void _pi(GraphElement.HintTypes hintType) {
    }

    private void setup() {

        Game.getInstance().getSchedule().scheduleAtFixedRate(() -> {
            timeConstraints.forEach((k, v) -> {
                timeConstraints.put(k, v - 500);

                if(!_42list.contains(k.getProperties().get("type")) && v - 500 < 0 && !k.isVisible()) {
                    k.setVisible(true);
                }
            });
        }, 0, 500, TimeUnit.MILLISECONDS);

        this.getChildren().add(this.renderTypes);
        this.renderTypes.setOnAction(event -> {
            graphElement.setLayout(this.renderTypes.getSelectionModel().getSelectedItem());
            graphElement.applyLayout();
        });

        for(Button button : timeConstraints.keySet()) {
            button.getStyleClass().add("button");
            button.setOnAction(e -> {
                GraphElement.HintTypes hintType = (GraphElement.HintTypes) button.getProperties().get("type");
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
            button.setVisible(false);
            this.getChildren().add(button);
        }

    }

}
