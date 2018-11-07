package edu.um.chromaster.gui;

import edu.um.chromaster.ChromaticNumber;
import edu.um.chromaster.GraphDrawer;
import edu.um.chromaster.HintManager;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class GraphElement extends Application {

    private ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(1);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Graph graph = new Graph();
        final int nodes = 30;
        IntStream.range(0, nodes).forEach(i -> graph.addNode(i, -1));
        Random random = new Random();

        for(int from = 0; from < nodes; from++) {
            for(int to = 0; to < nodes; to++) {
                if (from != to && random.nextDouble() < 0.3) {
                    graph.addEdge(from, to, true);
                }
            }
        }

        StackPane stackPane = new StackPane();
        Canvas canvas = new Canvas();
        canvas.setWidth(1024);
        canvas.setHeight(1024);

        //---background
        //canvas.getGraphicsContext2D().drawImage(new Image("file:///C:/Users/Jan/Downloads/Earth_Eastern_Hemisphere.jpg"), 0, 0);
        canvas.getGraphicsContext2D().setFill(Color.DARKBLUE);
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        stackPane.getChildren().add(canvas);
        primaryStage.setScene(new Scene(stackPane));
        primaryStage.show();

        graph.getNodes().forEach((id, node) -> {
            node.getMeta().positionX = (random.nextDouble() * canvas.getWidth()) - canvas.getWidth() / 2;
            node.getMeta().positionY = (random.nextDouble() * canvas.getHeight() ) - canvas.getHeight() / 2;
        });
        GraphDrawer.fruchtermanReingoldNext(graph, canvas.getWidth(), canvas.getHeight());
        /*graph.getNode(graph.getEdges().entrySet().stream()
                .min((o1, o2) -> -1 * Integer.compare(o1.getValue().size(), o2.getValue().size()))
                .get().getKey()).getMeta().visible = true;*/
        graph.getNode(0).getMeta().visible = true;


        List<List<Node>> cliques = new ArrayList<>();
        cliques.add(HintManager.cliqueDetector9000(graph));

        schedule.scheduleAtFixedRate(() -> {
            for (Node node : graph.getNodes().values()) {
                if (node.getMeta().visible) {
                    boolean t = false;
                    for (Node.Edge e : graph.getEdges(node.getId())) {
                        if(!e.getTo().getMeta().visible) {
                            t = true;
                            e.getTo().getMeta().visible = true;
                            break;
                        }
                    }
                    if(t) break;
                }
            }
            draw(graph, canvas, cliques);
        }, 100L, 100L, TimeUnit.MILLISECONDS);


    }

    private static void draw(Graph graph, Canvas canvas, List<List<Node>> cliques) {
        //canvas.getGraphicsContext2D().drawImage(new Image("file:///C:/Users/Jan/Downloads/Earth_Eastern_Hemisphere.jpg"), 0, 0);
        canvas.getGraphicsContext2D().setFill(Color.DARKBLUE);
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        graph.getEdges().values().forEach(edgeList -> {
            edgeList.forEach(edge -> {
                if(edge.getTo().getMeta().visible && edge.getFrom().getMeta().visible) {
                    canvas.getGraphicsContext2D().setStroke(Color.WHITE);
                    canvas.getGraphicsContext2D().strokeLine(
                            edge.getFrom().getMeta().positionX,
                            edge.getFrom().getMeta().positionY,
                            edge.getTo().getMeta().positionX,
                            edge.getTo().getMeta().positionY
                    );
                    /*Timeline timeline = generateTimeline(
                            edge.getFrom().getMeta().positionX,
                            edge.getFrom().getMeta().positionY,
                            edge.getTo().getMeta().positionX,
                            edge.getTo().getMeta().positionY
                    );
                    timeline.playFromStart();*/
                }
            });
        });

        Color[] colours = new Color[] {Color.rgb(0, 255, 0), Color.rgb(255, 0, 0), Color.rgb(255, 10, 159)};
        for(int i = 0; i < cliques.size(); i++) {
            int finalI = i;
            cliques.get(i).forEach(node -> node.getMeta().colour = colours[finalI]);
        }
        graph.getNodes().forEach((id, node) -> {
            if(node.getMeta().visible) {
                canvas.getGraphicsContext2D().setFill(node.getMeta().colour);
                canvas.getGraphicsContext2D().fillOval(node.getMeta().positionX - 5, node.getMeta().positionY - 5, 10, 10);
                canvas.getGraphicsContext2D().setFill(Color.WHITE);
                canvas.getGraphicsContext2D().fillOval(node.getMeta().positionX - 3, node.getMeta().positionY - 3, 6, 6);
            }
        });

        canvas.getGraphicsContext2D().restore();

    }

    private static Timeline generateTimeline(double fromX, double fromY, double toX, double toY) {
        Line line = new Line(fromX, fromY,toX, toY);
        Timeline timeline = new Timeline(
            new KeyFrame(
                Duration.millis(300),
                new KeyValue(
                    line.endXProperty(),
                    toX,
                    Interpolator.LINEAR
                ),
                new KeyValue(
                    line.endYProperty(),
                    toY,
                    Interpolator.LINEAR
                )
            )
        );
        timeline.setCycleCount(1);
        return timeline;

    }

}
