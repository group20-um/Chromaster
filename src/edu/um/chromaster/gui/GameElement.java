package edu.um.chromaster.gui;

import edu.um.chromaster.ChromaticNumber;
import edu.um.chromaster.Game;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.modes.GameMode;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GameElement extends StackPane {

    private BackgroundElement backgroundElement;
    private GraphElement graphElement;
    private ColourSelectorElement colourSelectorElement;

    private HBox menuBar = new HBox();

    public GameElement(Stage stage, Graph graph, GameMode gameMode) {
        ChromaticNumber.computeAsync(ChromaticNumber.Type.EXACT, graph.clone(), graph::setChromaticResults);

        this.graphElement = new GraphElement(graph, GraphElement.RenderType.SPIRAL);
        this.colourSelectorElement = new ColourSelectorElement(stage);

        changeWindowSize(1280, 720);

        // distribute graph nodes randomly
        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x((Game.random.nextDouble() * graphElement.getWidth()) - graphElement.getWidth() / 2 + colourSelectorElement.getHeight());
            node.getMeta().y((Game.random.nextDouble() * graphElement.getHeight()) - graphElement.getHeight() / 2);
        });


        {
            ComboBox<GraphElement.HintType> hint = new ComboBox<>(FXCollections.observableArrayList(GraphElement.HintType.values()));
            hint.setOnAction(e -> {
                graphElement.displayHints(hint.getValue());
            });
            menuBar.getChildren().add(hint);
        }

        this.graphElement.setBackground(Background.EMPTY);

        graphElement.applyLayout();
        this.graphElement.render();
        gameMode.start();
        this.backgroundElement = new BackgroundElement();
        this.getChildren().add(this.backgroundElement);
        this.getChildren().add(this.graphElement);
        this.getChildren().add(this.colourSelectorElement);
        //this.getChildren().add(this.menuBar);

        StackPane.setAlignment(colourSelectorElement, Pos.TOP_CENTER);
        StackPane.setAlignment(menuBar, Pos.BOTTOM_CENTER);

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
