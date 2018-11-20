package edu.um.chromaster;

import edu.um.chromaster.event.EventHandler;
import edu.um.chromaster.event.EventListener;
import edu.um.chromaster.event.Subscribe;
import edu.um.chromaster.event.events.NodeClickedEvent;
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

    private final static EventHandler eventHandler = new EventHandler();

    public static void main(String[] args) {


        launch(args);

    }

    public static EventHandler getEventHandler() {
        return eventHandler;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        eventHandler.registerListener(new EventListener() {

            @Subscribe
            public void handleClickEvent(NodeClickedEvent event) {
                System.out.println(event.getNode().getId());
            }

        });

        StackPane stackPane = new StackPane();
        Graph graph = new Graph();
        final int nodes = 200;
        IntStream.range(0, nodes).forEach(i -> graph.addNode(i, -1));
        Random random = new Random(200); //TODO same seed to ease debugging efforts

        for(int from = 0; from < nodes; from++) {
            for(int to = 0; to < nodes; to++) {
                if (from != to && random.nextDouble() < 0.30) {
                    graph.addEdge(from, to, true);
                }
            }
        }

        ChromaticNumber.computeAsync(ChromaticNumber.Type.EXACT, graph, integer -> System.out.printf("Result %d\n", integer));
        GraphElement graphElement  = new GraphElement(graph, GraphElement.RenderType.CIRCLE, GraphElement.BackgroundType.COLOUR);

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
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
