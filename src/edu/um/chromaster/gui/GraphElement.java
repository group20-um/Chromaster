package edu.um.chromaster.gui;

import edu.um.chromaster.GraphDrawer;
import edu.um.chromaster.HintManager;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class GraphElement extends Canvas {

    private final double NODE_RADIUS = 10;

    private final Graph graph;
    private RenderType renderType;
    private BackgroundType backgroundType;

    private ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(1);

    public GraphElement(Graph graph, RenderType renderType, BackgroundType backgroundType) {
        this.graph = graph;
        this.renderType = renderType;
        this.backgroundType = backgroundType;
        this.setWidth(1024);
        this.setHeight(1024);
    }


    public void render() {

        switch (this.renderType) {
            case CIRCLE: GraphDrawer.circle(graph, this.getWidth(), this.getHeight()); break;
            case SHELL: GraphDrawer.shell(graph, this.getWidth(), this.getHeight());  break;
            case SCALE: GraphDrawer.scale(graph, this.getWidth(), this.getHeight()); break;
            case BANANA: GraphDrawer.banana(graph, this.getWidth(), this.getHeight()); break;
            case LIMACON: GraphDrawer.limacon(graph, this.getWidth(), this.getHeight()); break;
            case SPIRAL: GraphDrawer.archemedianSprial(graph, this.getWidth(), this.getHeight()); break;
            case ROSE: GraphDrawer.rose(graph, this.getWidth(), this.getHeight()); break;
            default: throw new IllegalArgumentException();
        }

        /*graph.getNode(graph.getEdges().entrySet().stream()
                .min((o1, o2) -> -1 * Integer.compare(o1.getValue().size(), o2.getValue().size()))
                .get().getKey()).getMeta().visible = true;*/
        graph.getNode(0).getMeta().visible = true;

        schedule.scheduleAtFixedRate(() -> {
            if(graph.getNodes().values().stream().anyMatch(e -> !e.getMeta().visible)) {
                for (Node node : graph.getNodes().values()) {
                    if (node.getMeta().visible) {
                        boolean t = false;
                        for (Node.Edge e : graph.getEdges(node.getId())) {
                            if (!e.getTo().getMeta().visible) {
                                t = true;
                                e.getTo().getMeta().visible = true;
                                break;
                            }
                        }
                        if (t) break;
                    }
                }
                Platform.runLater(this::draw);
            }
        }, 100L, 100L, TimeUnit.MILLISECONDS);

    }

    public void draw() {
        this.drawBackground();

        graph.getEdges().values().forEach(edgeList -> {
            edgeList.forEach(edge -> {
                if(edge.getTo().getMeta().visible && edge.getFrom().getMeta().visible) {
                    this.getGraphicsContext2D().setStroke(Color.WHITE);
                    this.getGraphicsContext2D().strokeLine(
                            edge.getFrom().getMeta().x(),
                            edge.getFrom().getMeta().y(),
                            edge.getTo().getMeta().x(),
                            edge.getTo().getMeta().y()
                    );
                }
            });
        });

        graph.getNodes().forEach((id, node) -> {
            if(node.getMeta().visible) {
                GraphicsContext g = this.getGraphicsContext2D();

                // TODO not the ideal position to update, should be moved to the Meta struct to avoid inconsistent data
                // if the draw method is not called after updating the coordinates of a node
                node.getMeta().area(new Circle(node.getMeta().x(), node.getMeta().y(), NODE_RADIUS));

                g.setFill(Color.WHITE);
                g.fillOval(node.getMeta().x() - NODE_RADIUS, node.getMeta().y() - NODE_RADIUS, NODE_RADIUS * 2, NODE_RADIUS * 2);
                g.setFill(node.getMeta().colour);
                g.fillOval(node.getMeta().x() - (int)(NODE_RADIUS * 0.6), node.getMeta().y() - (int)(NODE_RADIUS * 0.6), (int)(NODE_RADIUS * 0.6)*2, (int)(NODE_RADIUS * 0.6)*2);
                g.setStroke(Color.WHITE);
                g.strokeText(node.getMeta().text(), (node.getMeta().x() - (int)(NODE_RADIUS * 0.6)) + g.getFont().getSize() / 2, (node.getMeta().y() - (int)(NODE_RADIUS * 0.6)) + g.getFont().getSize(), NODE_RADIUS * 2);
            }
        });

        this.getGraphicsContext2D().restore();

    }

    private void drawBackground() {
        switch (this.backgroundType) {
            case IMAGE: {
                this.getGraphicsContext2D().drawImage(new Image("file:///C:/Users/Jan/Downloads/Earth_Eastern_Hemisphere.jpg"), 0, 0);
            } break;
            case COLOUR: {
                this.getGraphicsContext2D().setFill(Color.DARKBLUE);
                this.getGraphicsContext2D().fillRect(0, 0, this.getWidth(), this.getHeight());
            } break;
            default: throw new IllegalArgumentException();
        }
    }

    public void displayHints(HintType... hintTypes) {
        this.graph.getNodes().values().forEach(e -> e.getMeta().colour = Node.Meta.defaultColour);
        for(HintType hintType : hintTypes) {
            switch (hintType) {
                case CLIQUE: {
                    List<List<Node>> cliques = new ArrayList<>();
                    cliques.add(HintManager.cliqueDetector9000(graph));
                    this.computeHighlighting((node) -> {
                        for(List<Node> l : cliques) {
                            if(l.contains(node)) {
                                return true;
                            }
                        }
                        return false;
                    }, (meta) -> meta.colour = Color.YELLOW);
                } break;
                case HIGHES_DEGREE: {
                    Node node = HintManager.highestDegree(graph);
                    node.getMeta().colour = Color.PINK;
                } break;
                case MAX_NEIGHBOURS:
                    Node node = HintManager.maxNeighboursColoured(graph);
                    this.computeHighlighting(e -> e == node, (meta) -> meta.colour = Color.GREEN);
                    break;
            }
        }
    }

    private void computeHighlighting(Predicate<Node> filter, Callback<Node.Meta> modify) {
        this.graph.getNodes().values().stream().filter(filter).forEach(e -> modify.modify(e.getMeta()));
    }

    public static enum HintType {
        CLIQUE,
        HIGHES_DEGREE,
        MAX_NEIGHBOURS
    }

    public static enum BackgroundType {
        IMAGE,
        COLOUR
    }

    public static enum RenderType {
        CIRCLE,
        SHELL,
        SCALE,
        BANANA,
        SPIRAL,
        ROSE,
        LIMACON
    }

    public interface Callback<T> {
        void modify(T param);
    }
}
