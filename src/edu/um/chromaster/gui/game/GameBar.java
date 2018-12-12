package edu.um.chromaster.gui.game;

import edu.um.chromaster.Game;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The GameBar contains elements that the user can interact with while playing the game.
 */
public class GameBar extends HBox {

    private Map<Button, Double> timeConstraints = new LinkedHashMap<>();

    private ComboBox<GraphElement.RenderType> renderTypes = new ComboBox<>(FXCollections.observableArrayList(
            GraphElement.RenderType.values()
    ));

    private GraphElement graphElement;
    private Label timer = new Label();

    public GameBar(GraphElement graphElement, double timeInMilliseconds) {

        this.getStylesheets().add("res/style.css");
        this.getStyleClass().add("game-bar");

        // Add all hints as buttons
        for(GraphElement.HintTypes e : Stream.of(GraphElement.HintTypes.values()).sorted(Comparator.comparingDouble(GraphElement.HintType::getTimeConstraint)).collect(Collectors.toList())) {
            if(e.getDifficulties().contains(graphElement.getGameElement().getGameMode().getDifficulty())) {
                Button b = new Button(e.getDisplayName());
                b.getProperties().put("type", e);
                timeConstraints.put(b, e.getTimeConstraint() * timeInMilliseconds);
            }
        }

        renderTypes.getStyleClass().add("combo-box");
        renderTypes.getSelectionModel().select(graphElement.getRenderType());

        this.graphElement = graphElement;
        this.setup();
    }

    private void setup() {

        //--- hints only become available after a certain amount of time. This task is responsible for enabling the buttons
        // in time.
        Game.getInstance().getSchedule().scheduleAtFixedRate(() -> {
            timeConstraints.forEach((k, v) -> {
                if(v > 0) {
                    timeConstraints.put(k, v - 500);

                    if (v - 500 <= 0 && k.isDisabled()) {
                        k.setDisable(false);
                    }
                }

                if(graphElement.getGameElement().getGameMode().isDisplayingTime()) {
                    Platform.runLater(() -> {
                        long time = TimeUnit.MILLISECONDS.toSeconds(graphElement.getGameElement().getGameMode().getTimeLeft());

                        int minutes = (int) (time / 60);
                        int seconds = 0;

                        Color color = Color.WHITE;

                        if(time % 60 > 0) {
                            seconds = (int) (time % 60);
                        } else {
                            color = Color.RED;
                        }

                        if(time <= 10) {
                            color = time % 2 == 0 ? Color.RED : Color.ORANGE;
                        }

                        this.timer.setTextFill(color);
                        this.timer.setText(String.format("%02d:%02d", minutes, seconds));
                    });
                }
            });
        }, 0, 500, TimeUnit.MILLISECONDS);

        this.getChildren().add(this.renderTypes);
        this.renderTypes.setOnAction(event -> {
            graphElement.setLayout(this.renderTypes.getSelectionModel().getSelectedItem());
            graphElement.applyLayout();
        });

        //--- Adds the buttons to the GameBar to make them actually visible to the user.
        for(Button button : timeConstraints.keySet()) {
            button.getStyleClass().add("button");
            button.setOnAction(e -> {
                GraphElement.HintTypes hintType = (GraphElement.HintTypes) button.getProperties().get("type");
                if(hintType.isVisual()) {
                    graphElement.displayHints(hintType);
                } else {
                    Tooltip tooltip = new Tooltip(String.valueOf(graphElement.getHint(hintType)));

                    tooltip.setContentDisplay(ContentDisplay.CENTER);
                    tooltip.getStyleClass().add("tooltip");
                    tooltip.setAutoHide(true);
                    tooltip.setHideOnEscape(true);
                    Stage s = Game.getInstance().getStage();
                    tooltip.show(button, s.getX() + (s.getWidth() * 0.95), s.getY() + s.getHeight() * 0.875);
                    button.setTooltip(tooltip);
                }
            });
            button.setDisable(true);
            this.getChildren().add(button);
        }


        if(graphElement.getGameElement().getGameMode().isDisplayingTime()) {
            this.getChildren().add(this.timer);
        }

    }

}
