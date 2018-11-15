package edu.um.chromaster;

import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import edu.um.chromaster.gui.GraphElement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

public class Game extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {


        StackPane stackPane = new StackPane();
        Graph graph = new Graph();
        final int nodes = 5;
        IntStream.range(0, nodes).forEach(i -> graph.addNode(i, -1));
        Random random = new Random(1L); //TODO same seed to ease debugging efforts

        for(int from = 0; from < nodes; from++) {
            for(int to = 0; to < nodes; to++) {
                if (from != to && random.nextDouble() < 0.9) {
                    graph.addEdge(from, to, true);
                }
            }
        }

        System.out.println(ChromaticNumber.compute(ChromaticNumber.Type.EXACT, graph, false));

        GraphElement graphElement  = new GraphElement(graph, GraphElement.RenderType.SHELL, GraphElement.BackgroundType.COLOUR);

        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x((random.nextDouble() * graphElement.getWidth()) - graphElement.getWidth() / 2);
            node.getMeta().y((random.nextDouble() * graphElement.getHeight()) - graphElement.getHeight() / 2);
        });

        graphElement.render();
        stackPane.getChildren().add(graphElement);
        Scene scene = new Scene(stackPane);

        // TODO testing hint functions
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case F1: graphElement.displayHints(GraphElement.HintType.HIGHES_DEGREE); break;
                case F2: graphElement.displayHints(GraphElement.HintType.CLIQUE); break;
                case F3: graphElement.displayHints(GraphElement.HintType.MAX_NEIGHBOURS); break;
                case F4: graphElement.displayHints(GraphElement.HintType.HIGHES_DEGREE, GraphElement.HintType.CLIQUE, GraphElement.HintType.MAX_NEIGHBOURS);
            }
            graphElement.draw();
        });

        // TODO sample mouse-click to node
        graphElement.setOnMouseClicked(event -> {
            Optional<Node> node = graph.getNodes().values().stream()
                    .filter(e -> e.getMeta().area().contains(event.getSceneX(), event.getSceneY()))
                    .findAny();
            node.ifPresent(e -> {
                System.out.println(e.getId());
            });

        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
