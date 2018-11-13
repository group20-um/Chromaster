package edu.um.chromaster.gui;

import edu.um.chromaster.GraphDrawer;
import edu.um.chromaster.HintManager;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class GraphElement extends Canvas {

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
                draw(graph);
            }
        }, 100L, 100L, TimeUnit.MILLISECONDS);

    }

    private void draw(Graph graph) {
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
                this.getGraphicsContext2D().setFill(node.getMeta().colour);
                this.getGraphicsContext2D().fillOval(node.getMeta().x() - 5, node.getMeta().y() - 5, 10, 10);
                this.getGraphicsContext2D().setFill(Color.WHITE);
                this.getGraphicsContext2D().fillOval(node.getMeta().x() - 3, node.getMeta().y() - 3, 6, 6);
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
        this.graph.getNodes().values().forEach(e -> e.getMeta().colour = Color.BLACK);
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
                    this.computeHighlighting(e -> e == node, (meta) -> meta.colour = Color.BISQUE);
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
        BANANA
    }

    public static interface Callback<T> {
        void modify(T param);
    }
}
