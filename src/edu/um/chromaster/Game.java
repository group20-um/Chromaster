package edu.um.chromaster;

import edu.um.chromaster.event.EventHandler;
import edu.um.chromaster.event.EventListener;
import edu.um.chromaster.event.Subscribe;
import edu.um.chromaster.event.events.NodeClickedEvent;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.gui.GraphElement;
import edu.um.chromaster.gui.GraphElementOG;
import edu.um.chromaster.modes.FirstGameMode;
import edu.um.chromaster.modes.GameMode;
import edu.um.chromaster.modes.ThirdGameMode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;
import java.util.stream.IntStream;

public class Game extends Application {

    private final static EventHandler eventHandler = new EventHandler();
    private static Game instance;

    private GameMode gameMode = null;

    public static void main(String[] args) {
        launch(args);
    }

    public static Game getInstance() {
        return instance;
    }

    public static EventHandler getEventHandler() {
        return eventHandler;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public void start(Stage primaryStage) {
        instance = this;


        StackPane stackPane = new StackPane();
        Graph graph = new Graph();
        final int nodes = 30;
        IntStream.range(0, nodes).forEach(i -> graph.addNode(i, -1));
        Random random = new Random(); //TODO same seed to ease debugging efforts

        for(int from = 0; from < nodes; from++) {
            for(int to = 0; to < nodes; to++) {
                if (from != to && random.nextDouble() < 0.1) {
                    graph.addEdge(from, to, true);
                }
            }
        }

        this.gameMode = new ThirdGameMode(graph);
        this.gameMode.start();

        ChromaticNumber.computeAsync(ChromaticNumber.Type.EXACT, graph, graph::setChromaticNumber);
        GraphElement graphElement  = new GraphElement(graph, GraphElement.RenderType.SPIRAL, GraphElement.BackgroundType.COLOUR);

        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x((random.nextDouble() * graphElement.getWidth()) - graphElement.getWidth() / 2);
            node.getMeta().y((random.nextDouble() * graphElement.getHeight()) - graphElement.getHeight() / 2);
        });


        graphElement.render();

        stackPane.getChildren().add(graphElement);
        Scene scene = new Scene(stackPane);
        scene.getStylesheets().add("res/style.css");

        // TODO testing hint functions
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case F1: graphElement.displayHints(GraphElement.HintType.HIGHES_DEGREE); break;
                case F2: graphElement.displayHints(GraphElement.HintType.CLIQUE); break;
                case F3: graphElement.displayHints(GraphElement.HintType.MAX_NEIGHBOURS); break;
                case F4: graphElement.displayHints(GraphElement.HintType.HIGHES_DEGREE, GraphElement.HintType.CLIQUE, GraphElement.HintType.MAX_NEIGHBOURS);
            }
            //graphElement.draw();
        });

        // TODO sample mouse-click to node
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
