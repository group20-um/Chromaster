package edu.um.chromaster.gui;

import edu.um.chromaster.Game;
import edu.um.chromaster.GraphDrawer;
import edu.um.chromaster.HintManager;
import edu.um.chromaster.event.events.NodeClickedEvent;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import edu.um.chromaster.modes.ThirdGameMode;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GraphElementOG extends Canvas {


    private final Graph graph;
    private GraphElement.RenderType renderType;
    private GraphElement.BackgroundType backgroundType;

    private ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(1);

    public GraphElementOG(Graph graph, GraphElement.RenderType renderType, GraphElement.BackgroundType backgroundType) {
        this.graph = graph;
        this.renderType = renderType;
        this.backgroundType = backgroundType;
        this.setWidth(1024);
        this.setHeight(1024);

        this.setOnMouseClicked(event -> {
            Optional<Node> node = graph.getNodes().values().stream()
                    .filter(e -> e.getMeta().isAllowedToChangeColour() && e.getMeta().visible() && e.getMeta().area().contains(event.getSceneX(), event.getSceneY()))
                    .findAny();
            node.ifPresent(e -> Game.getEventHandler().trigger(new NodeClickedEvent(e)));
        });
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


        // sort all nodes by #connect nodes descending
        Stack<Node> nodes = graph.getNodes().values().stream()
                .sorted((o1, o2) -> {
                    int a = graph.getEdges(o1.getId()).size();
                    int b = graph.getEdges(o2.getId()).size();

                    if(a == 0 && b == 0) {
                        return Integer.compare(-o1.getId(), o2.getId());
                    }

                    return Integer.compare(a, b);
                })
                .collect(Collectors.toCollection(Stack::new));

        final long MAX_TIME_TO_DRAW = 1000;
        final long MAX_TIME_STEP = (MAX_TIME_TO_DRAW / graph.getNodes().size());

        Stack<Node> priorityNodes = new Stack<>();
        AtomicReference<ScheduledFuture<?>> scheduledFuture = new AtomicReference<>();
        scheduledFuture.set(schedule.scheduleAtFixedRate(() -> {

            if(Game.getInstance().getGameMode() instanceof ThirdGameMode) {
                Platform.runLater(this::draw);
                return;
            }

            if(graph.getNodes().values().stream().anyMatch(e -> !e.getMeta().visible())) {

                // If there is nothing to deal with in the priority queue
                if(priorityNodes.isEmpty()) {

                    // Take the node with the highest degree that is still on the stack
                    Node node = nodes.peek();

                    if (node.getMeta().visible()) {
                        List<Node.Edge> edges = graph.getEdges(node.getId()).stream().filter(e -> !e.getTo().getMeta().visible()).collect(Collectors.toList());
                        // If the parent node (from the stack) is visible and has a neighbour that is not yet visible then make it visible.
                        if (!edges.isEmpty()) {
                            Node.Edge edge = edges.get(0);
                            Node toNode = edge.getTo();
                            toNode.getMeta().visible(true);

                            // Add the neighbouring nodes to the priority list, so we deal with them before we move on
                            // to anything else.
                            List<Node> priority = edges.stream().map(Node.Edge::getTo).collect(Collectors.toList());
                            nodes.removeAll(priority);
                            priority.remove(edge.getTo());
                            priorityNodes.addAll(priority);
                        }
                        // If all the neighbours of the parent node are already visible then remove it from the stack and turn
                        // the next node visible to ensure that every step draws exactly one node
                        else {
                            nodes.remove(node);
                            if (!nodes.isEmpty()) {
                                nodes.pop().getMeta().visible(true);
                            }
                        }

                    }
                    // If the peek node is not yet visible
                    else {
                        node.getMeta().visible(true);
                    }

                }
                // Handle priority queue
                else {
                    priorityNodes.pop().getMeta().visible(true);
                }

                Platform.runLater(this::draw);

            } else {
                scheduledFuture.get().cancel(true);
            }
        }, 100L, (1000 / 60), TimeUnit.MILLISECONDS));

    }

    public void draw() {
        this.drawBackground();

        graph.getEdges().values().forEach(edgeList -> {
            edgeList.forEach((to, edge) -> {
                if(edge.getTo().getMeta().visible() && edge.getFrom().getMeta().visible()) {
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
            if(node.getMeta().visible()) {
                GraphicsContext g = this.getGraphicsContext2D();
                Node.Meta m = node.getMeta();
                g.setFill(m.isAllowedToChangeColour() ? Color.WHITE : Color.DARKGRAY);
                g.fillOval(m.x() - m.radius(), m.y() - m.radius(), m.radius() * 2, m.radius() * 2);
                g.setFill(m.colour());
                g.fillOval(m.x() - (int) (m.radius() * 0.6), m.y() - (int) (m.radius() * 0.6), (int) (m.radius() * 0.6) * 2, (int) (m.radius() * 0.6) * 2);
                g.setStroke(Color.WHITE);
                g.strokeText(m.text(), (m.x() - (int) (m.radius() * 0.6)) + g.getFont().getSize() / 2, (node.getMeta().y() - (int) (m.radius() * 0.6)) + g.getFont().getSize(), m.radius() * 2);
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

    public void displayHints(GraphElement.HintType... hintTypes) {
        this.graph.getNodes().values().forEach(e -> e.getMeta().colour(ColorList.NODE_INNER_DEFAULT));
        for(GraphElement.HintType hintType : hintTypes) {
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
                    }, (meta) -> meta.colour(Color.YELLOW));
                } break;
                case HIGHES_DEGREE: {
                    Node node = HintManager.highestDegree(graph);
                    node.getMeta().colour(Color.PINK);
                } break;
                case MAX_NEIGHBOURS:
                    Node node = HintManager.maxNeighboursColoured(graph);
                    this.computeHighlighting(e -> e == node, (meta) -> meta.colour(Color.GREEN));
                    break;
            }
        }
    }

    private void computeHighlighting(Predicate<Node> filter, Callback<Node.Meta> modify) {
        this.graph.getNodes().values().stream().filter(filter).forEach(e -> modify.modify(e.getMeta()));
    }

    public interface Callback<T> {
        void modify(T param);
    }
}
