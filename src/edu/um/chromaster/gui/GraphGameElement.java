package edu.um.chromaster.gui;

import edu.um.chromaster.ChromaticNumber;
import edu.um.chromaster.Game;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.modes.GameMode;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GraphGameElement extends BorderPane {

    private GameMode gameMode;
    private GraphElement graphElement;
    private ColourSelectorElement colourSelectorElement;

    public GraphGameElement(Stage stage, Graph graph, GameMode gameMode) {
        ChromaticNumber.computeAsync(ChromaticNumber.Type.EXACT, graph.clone(), graph::setChromaticResults);

        this.getStylesheets().add("res/style.css");

        this.graphElement = new GraphElement(graph, GraphElement.RenderType.SPIRAL);
        this.colourSelectorElement = new ColourSelectorElement(stage);
        this.gameMode = gameMode;

        // distribute graph nodes randomly
        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x((Game.random.nextDouble() * graphElement.getWidth()) - graphElement.getWidth() / 2);
            node.getMeta().y((Game.random.nextDouble() * graphElement.getHeight()) - graphElement.getHeight() / 2);
        });

        this.graphElement.render();

        this.setMinSize(1024, 1024);
        this.setPrefSize(1024, 1024);
        this.setMaxSize(1024, 1024);
        this.setCenter(this.graphElement);
        this.setTop(this.colourSelectorElement);

        //--- TODO
        Button button = new Button("Check Win Condition");
        button.setOnAction(event -> {
            System.out.println("Did the user win? " + gameMode.gameWon());
        });

        Button buttonSolution = new Button("Show Solution");
        buttonSolution.setOnAction(event -> {
            graph.getChromaticResult().getSolution().getNodes().forEach((id, node) -> {
                graph.getNode(id).getMeta().text(String.valueOf(node.getValue()));
            });
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(button, buttonSolution);
        this.setBottom(hBox);

        gameMode.start();
    }


}
