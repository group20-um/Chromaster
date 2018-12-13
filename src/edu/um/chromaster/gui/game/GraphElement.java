package edu.um.chromaster.gui.game;

import edu.um.chromaster.Game;
import edu.um.chromaster.GraphDrawer;
import edu.um.chromaster.HintManager;
import edu.um.chromaster.event.events.NodeClickedEvent;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import edu.um.chromaster.gui.ColorList;
import edu.um.chromaster.modes.ThirdGameMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The GraphElement manages all the nodes of the Graph.
 */
public class GraphElement extends Pane {

    private final GameElement gameElement;
    private final Graph graph;

    private RenderType renderType;

    /**
     *
     * @param gameElement The parent GameElement that is associated with this element.
     * @param graph The graph that is supposed to be displayed.
     * @param renderType The RenderType that is supposed to be applied to the graph to visualise it.
     */
    public GraphElement(GameElement gameElement, Graph graph, RenderType renderType) {
        this.gameElement = gameElement;
        this.renderType = renderType;
        this.graph = graph;

        this.setOnMouseClicked(event -> {
            Optional<Node> node = graph.getNodes().values().stream()
                    .filter(e -> e.getMeta().isAllowedToChangeColour() && e.getMeta().visible() && e.getMeta().area().contains(event.getX(), event.getY()))
                    .findAny();
            node.ifPresent(e -> Game.getInstance().getEventHandler().trigger(new NodeClickedEvent(e)));
        });

        graph.getEdges().forEach((id, edges) -> edges.forEach((to, e) -> {
            this.getChildren().addAll(e.getMeta().getGraphicElements());
        }));
        graph.getNodes().forEach((id, node) ->  {
            this.getChildren().addAll(node.getMeta().getGraphicElements());
        });

    }

    public RenderType getRenderType() {
        return renderType;
    }

    /**
     * Returns the associated GameElement.
     * @return
     */
    public GameElement getGameElement() {
        return gameElement;
    }

    /**
     * Sets a new layout but does not apply it yet.
     * @param renderType
     */
    public void setLayout(RenderType renderType) {
        this.renderType = renderType;
    }

    /**
     * Applies the layout to the actual graph and causes it to redraw.
     */
    public void applyLayout() {
        double width = this.getMinWidth() * 0.75D;
        double height = this.getMinHeight()  * 0.75D;
        switch (this.renderType) {
            case CIRCLE: GraphDrawer.circle(graph, Math.min(width, height), Math.min(width, height)); break;
            case SHELL: GraphDrawer.shell(graph, width, height);  break;
            case SCALE: GraphDrawer.scale(graph, width, height); break;
            case BUTTERFLY: GraphDrawer.butterfly(graph, width, height); break;
            case SPIRAL: GraphDrawer.archemedianSpiral(graph, width, height); break;
            case VALENTINE: GraphDrawer.valentineCurve(graph,width,height);break;
            case TEST: GraphDrawer.test(graph, width, height); break;
            case TEST2: GraphDrawer.test2(graph, width, height); break;
            case RANDOM: GraphDrawer.random(graph, width, height); break;
            default: throw new IllegalArgumentException();
        }

        graph.getNodes().values().forEach(e -> {
            e.getMeta().x(e.getMeta().x() + width * 0.125D);
            e.getMeta().y(e.getMeta().y() + height * 0.125D);
        });

    }

    public void render() {

        // sort all nodes by #connect nodes descending
        Stack<Node> nodes = graph.getNodes().values().stream()
                .sorted((o1, o2) -> {
                    int a = graph.getEdges(o1.getId()).size();
                    int b = graph.getEdges(o2.getId()).size();

                    return Integer.compare(a, b);
                })
                .collect(Collectors.toCollection(Stack::new));

        final long MAX_TIME_TO_DRAW = 5000;
        final long MAX_TIME_STEP = (MAX_TIME_TO_DRAW / graph.getNodes().size());

        Stack<Node> priorityNodes = new Stack<>();
        AtomicReference<ScheduledFuture<?>> scheduledFuture = new AtomicReference<>();
        scheduledFuture.set(Game.getInstance().getSchedule().scheduleAtFixedRate(() -> {

            if(Game.getInstance().getGameElement().getGameMode() instanceof ThirdGameMode) {
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

            } else {
                while (scheduledFuture.get() == null); // like that should never actually happen... hopefully
                scheduledFuture.get().cancel(true);
            }
        }, 100L, MAX_TIME_STEP, TimeUnit.MILLISECONDS));

    }


    public void displayHints(HintTypes... hintTypes) {
        this.graph.getNodes().values().forEach(e -> e.getMeta().hint(null));
        for(HintTypes hintType : hintTypes) {

            if(!(hintType.isVisual())) {
                throw new IllegalArgumentException();
            }

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
                    }, (meta) -> meta.hint(ColorList.HINT_CLIQUE));
                } break;
                case HIGHEST_DEGREE: {
                    Node node = HintManager.highestDegree(graph);
                    node.getMeta().hint(ColorList.HINT_HIGHEST_DEGREE);
                } break;
                case MAX_NEIGHBOURS:
                    Node node = HintManager.maxNeighboursColoured(graph);
                    this.computeHighlighting(e -> e == node, (meta) -> meta.hint(ColorList.HINT_MAX_NEIGHBOURS));
                    break;

                case SOLUTION: {
                    Graph solution = this.graph.getChromaticResult().getSolution();
                    List<Color> colours = this.gameElement.getColourSelectorElement().getItems();

                    int delta = this.graph.getChromaticResult().getExact() - colours.size();
                    if(delta > 0) {

                        for(int i = 0; i < delta; i++) {
                            colours.add(Color.color(100/255D + i/delta * 100D * Game.random.nextDouble(), 100/255D + i/delta * 100D * Game.random.nextDouble(), 100/255D + i/delta * 100D * Game.random.nextDouble()));
                        }

                    }

                    this.graph.getNodes().values().forEach(n -> n.getMeta().hint(colours.get(solution.getNode(n.getId()).getValue() - 1)));
                } break;

                default: throw new IllegalStateException();
            }
        }
    }

    public int getHint(HintTypes hintType) {

        if(hintType.isVisual()) {
            throw new IllegalArgumentException();
        }

        switch (hintType) {
            case UPPER_BOUND: return graph.getChromaticResult().getUpper();
            case LOWER_BOUND: return graph.getChromaticResult().getLower();
            case CHROMATIC_NUMBER: return graph.getChromaticResult().getExact();
        }

        throw new IllegalStateException();
    }

    private void computeHighlighting(Predicate<Node> filter, Callback<Node.Meta> modify) {
        this.graph.getNodes().values().stream().filter(filter).forEach(e -> modify.modify(e.getMeta()));
    }

    public enum HintTypes implements HintType {
        HIGHEST_DEGREE {
            @Override
            public String getDisplayName() {
                return "Node w/ max. Degree";
            }

            @Override
            public double getTimeConstraint() {
                return 0.1D;
            }

            @Override
            public List<Difficulty> getDifficulties() {
                return Arrays.asList(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD);
            }

            @Override
            public boolean isVisual() {
                return true;
            }
        },
        MAX_NEIGHBOURS {
            @Override
            public String getDisplayName() {
                return "Max Neighbours Coloured";
            }

            @Override
            public double getTimeConstraint() {
                return 0.15D;
            }

            @Override
            public List<Difficulty> getDifficulties() {
                return Arrays.asList(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD);
            }

            @Override
            public boolean isVisual() {
                return true;
            }
        },
        CLIQUE {
            @Override
            public String getDisplayName() {
                return "Largest Clique";
            }

            @Override
            public double getTimeConstraint() {
                return 0.2;
            }

            @Override
            public List<Difficulty> getDifficulties() {
                return Arrays.asList(Difficulty.MEDIUM, Difficulty.HARD);
            }

            @Override
            public boolean isVisual() {
                return true;
            }
        },
        LOWER_BOUND {
            @Override
            public String getDisplayName() {
                return "Lower Bound";
            }

            @Override
            public double getTimeConstraint() {
                return 0.49999D;
            }

            @Override
            public List<Difficulty> getDifficulties() {
                return Arrays.asList(Difficulty.MEDIUM, Difficulty.HARD);
            }

            @Override
            public boolean isVisual() {
                return false;
            }
        },
        UPPER_BOUND {
            @Override
            public String getDisplayName() {
                return "Upper Bound";
            }

            @Override
            public double getTimeConstraint() {
                return 0.5D;
            }

            @Override
            public List<Difficulty> getDifficulties() {
                return Arrays.asList(Difficulty.MEDIUM, Difficulty.HARD);
            }

            @Override
            public boolean isVisual() {
                return false;
            }
        },
        CHROMATIC_NUMBER {
            @Override
            public String getDisplayName() {
                return "Chromatic Number";
            }

            @Override
            public double getTimeConstraint() {
                return 0.9D;
            }

            @Override
            public List<Difficulty> getDifficulties() {
                return Arrays.asList(Difficulty.MEDIUM, Difficulty.HARD);
            }

            @Override
            public boolean isVisual() {
                return false;
            }
        },
        SOLUTION {
            @Override
            public String getDisplayName() {
                return "Solution";
            }

            @Override
            public double getTimeConstraint() {
                return 0.95D;
            }

            @Override
            public List<Difficulty> getDifficulties() {
                return Arrays.asList(Difficulty.MEDIUM, Difficulty.HARD);
            }

            @Override
            public boolean isVisual() {
                return true;
            }
        }
    }

    public enum RenderType {
        CIRCLE,
        SHELL,
        SCALE,
        SPIRAL,
        BUTTERFLY,
        VALENTINE,
        TEST,
        TEST2,
        RANDOM
    }

    public interface HintType {

        String getDisplayName();

        double getTimeConstraint();

        List<Difficulty> getDifficulties();

        boolean isVisual();

    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    public interface Callback<T> {
        void modify(T param);
    }
}
