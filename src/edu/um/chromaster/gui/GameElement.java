package edu.um.chromaster.gui;

import edu.um.chromaster.ChromaticNumber;
import edu.um.chromaster.Game;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.modes.GameMode;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameElement extends StackPane {

    private BackgroundElement backgroundElement;
    private GraphElement graphElement;
    private ColourSelectorElement colourSelectorElement;

    private GameBar menuBar;

    public GameElement(Stage stage, Graph graph, GameMode gameMode) {
        ChromaticNumber.computeAsync(ChromaticNumber.Type.EXACT, graph.clone(), graph::setChromaticResults);

        this.graphElement = new GraphElement(graph, GraphElement.RenderType.SPIRAL);
        this.graphElement.backgroundProperty().set(Background.EMPTY);

        this.menuBar = new GameBar(this.graphElement);
        this.colourSelectorElement = new ColourSelectorElement(stage);

        changeWindowSize(1280, 720);

        // distribute graph nodes randomly
        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x((Game.random.nextDouble() * graphElement.getWidth()) - graphElement.getWidth() / 2 + colourSelectorElement.getHeight());
            node.getMeta().y((Game.random.nextDouble() * graphElement.getHeight()) - graphElement.getHeight() / 2);
        });


        graphElement.applyLayout();
        this.graphElement.render();

        //--- have to stay together
        gameMode.start();
        this.backgroundElement = new BackgroundElement();

        //---
        this.getChildren().add(this.backgroundElement);
        this.getChildren().add(this.graphElement);
        this.getChildren().add(this.colourSelectorElement);
        this.getChildren().add(this.menuBar);

        StackPane.setAlignment(colourSelectorElement, Pos.TOP_CENTER);
        StackPane.setAlignment(menuBar, Pos.BOTTOM_CENTER);

    }

    public void changeWindowSize(double width, double height) {
        this.setMinSize(width, height);
        this.graphElement.setMinSize(width, 0.85D * height);
        this.colourSelectorElement.setMinSize(width, height * 0.1D);
        this.menuBar.setMinSize(width, height * 0.05D);
        if (backgroundElement != null) {
            this.backgroundElement.setFitWidth(width);
            this.backgroundElement.setFitHeight(height);
        }
        this.graphElement.applyLayout();
    }
}
