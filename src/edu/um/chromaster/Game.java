package edu.um.chromaster;

import edu.um.chromaster.event.EventHandler;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.RandomGraph;
import edu.um.chromaster.gui.GraphGameElement;
import edu.um.chromaster.modes.FirstGameMode;
import edu.um.chromaster.modes.GameMode;
import edu.um.chromaster.modes.SecondGameMode;
import edu.um.chromaster.modes.ThirdGameMode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Random;
import java.util.stream.IntStream;

public class Game extends Application {

    public final static Random random = new Random(1); //TODO same seed to ease debugging efforts

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

/*
            Graph graph = new Graph();
            final int nodes = 30;
            IntStream.range(0, nodes).forEach(i -> graph.addNode(i, -1));

            for (int from = 0; from < nodes; from++) {
                for (int to = 0; to < nodes; to++) {
                    if (from != to && random.nextDouble() < 0.1) {
                        graph.addEdge(from, to, true);
                    }
                }
            }*/


        RandomGraph g = new RandomGraph();
        g.setLIMIT(10);
        g.setPMedium();
        g.setNada(true);
        System.out.println(g.getProbability());
        Graph graph = g.getGraph();



        this.gameMode = new FirstGameMode(graph);
        this.gameMode.start();

        GraphGameElement graphGameElement = new GraphGameElement(primaryStage, graph, gameMode);
        Scene scene = new Scene(graphGameElement, -1, -1, true, SceneAntialiasing.BALANCED);


        // TODO testing hint functions
/*        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case F1: graphElement.displayHints(GraphElement.HintType.HIGHES_DEGREE); break;
                case F2: graphElement.displayHints(GraphElement.HintType.CLIQUE); break;
                case F3: graphElement.displayHints(GraphElement.HintType.MAX_NEIGHBOURS); break;
                case F4: graphElement.displayHints(GraphElement.HintType.HIGHES_DEGREE, GraphElement.HintType.CLIQUE, GraphElement.HintType.MAX_NEIGHBOURS);
            }
            //graphElement.draw();
        });
*/
        // TODO sample mouse-click to node
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}
