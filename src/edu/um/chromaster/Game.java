package edu.um.chromaster;

import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import edu.um.chromaster.gui.GraphElement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
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
        final int nodes = 20;
        IntStream.range(0, nodes).forEach(i -> graph.addNode(i, -1));
        Random random = new Random(1L);

        for(int from = 0; from < nodes; from++) {
            for(int to = 0; to < nodes; to++) {
                if (from != to && random.nextDouble() < 0.3) {
                    graph.addEdge(from, to, true);
                }
            }
        }

        GraphElement graphElement  = new GraphElement(graph, GraphElement.RenderType.CIRCLE, GraphElement.BackgroundType.COLOUR);

        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x((random.nextDouble() * graphElement.getWidth()) - graphElement.getWidth() / 2);
            node.getMeta().y((random.nextDouble() * graphElement.getHeight()) - graphElement.getHeight() / 2);
        });

        graphElement.displayHints(GraphElement.HintType.HIGHES_DEGREE);
        graphElement.render();

        stackPane.getChildren().add(graphElement);
        primaryStage.setScene(new Scene(stackPane));
        primaryStage.show();
    }
}
