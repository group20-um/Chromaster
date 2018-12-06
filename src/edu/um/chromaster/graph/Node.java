package edu.um.chromaster.graph;

import edu.um.chromaster.gui.ColorList;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A Node represents a vertex of a graph.
 */
public class Node {

    private final Graph graph;
    private final int id;
    private Meta meta;
    private int value = -1;

    /**
     *
     * @param graph The graph the node is associated with.
     * @param id The unique id of the node.
     * @param value The value of the node.
     */
    protected Node(Graph graph, int id, int value) {
        this.graph = graph;
        this.id = id;
        this.meta = new Meta(this);
        this.value = value;
    }

    public int getId() {
        return id;
    }

    /**
     * Returns the associated Meta object of a Node.
     * @return
     */
    public Meta getMeta() {
        return this.meta;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**
     * The meta object contains information about the visual representation and other associated game values for the associated node.
     */
    public class Meta {

        public final static double DEFAULT_RADIUS = 20;

        private String textValue;
        private Text text = new Text();
        private Circle outer = new Circle();
        private Circle inner = new Circle();
        private Circle hintCircle = new Circle();

        private boolean visible = false;
        private boolean allowedToChangeColour = true;
        private boolean highlight = false;

        public double displacementX, displacementY;
        private double positionX, positionY;
        private double radius = DEFAULT_RADIUS;

        /**
         * The associated node.
         * @param node
         */
        public Meta(Node node) {

            //--- Setup default values and register listeners to enable highlighting when the user hovers over a node.
            this.textValue = String.valueOf(node.getId());
            this.outer.setFill(ColorList.NODE_OUTER_DEFAULT);
            this.inner.setFill(ColorList.NODE_INNER_DEFAULT);
            this.text.setFill(ColorList.NODE_TEXT_DEFAULT);

            EnteredEvent enteredEvent = new EnteredEvent(node);
            inner.setOnMouseEntered(enteredEvent);
            outer.setOnMouseEntered(enteredEvent);
            text.setOnMouseEntered(enteredEvent);
            hintCircle.setOnMouseEntered(enteredEvent);

            ExitedEvent exitedEvent = new ExitedEvent(node);
            inner.setOnMouseExited(exitedEvent);
            outer.setOnMouseExited(exitedEvent);
            text.setOnMouseExited(exitedEvent);
            hintCircle.setOnMouseExited(exitedEvent);

            this.inner.getStyleClass().add("node");
            this.outer.getStyleClass().add("node_border");
            updateCircles();

        }

        public void textColor(Color lightpink) {
            this.text.fillProperty().setValue(lightpink);
        }

        /**
         * The EnteredEvent is called when the user's mouse pointer enters the area of a node. It will then highlight associated
         * nodes (neighbours) and the edges inbetween.
         */
        private class EnteredEvent implements EventHandler<MouseEvent> {

            private Node node;

            public EnteredEvent(Node node) {
                this.node = node;
            }

            @Override
            public void handle(MouseEvent event) {
                Platform.runLater(() -> {
                    List<Node> connectedNodes = node.graph.getEdges(node.getId()).stream().map(Edge::getTo).collect(Collectors.toList());
                    connectedNodes.add(node);
                    node.graph.getEdges().values().forEach(map -> map.values().forEach(edge -> {

                        if(!connectedNodes.contains(edge.getFrom())) {
                            edge.getFrom().getMeta().hide();
                        }

                        if(!connectedNodes.contains(edge.getTo())) {
                            edge.getTo().getMeta().hide();
                        }

                        edge.getMeta().hide();


                    }));

                    node.graph.getEdges(node.getId()).forEach(e -> {
                        e.getTo().getMeta().highlight(true);
                        e.getMeta().line.strokeProperty().set(ColorList.EDGE_HOVER);
                    });
                    outer.setFill(ColorList.NODE_OUTER_DEFAULT);
                });
            }
        }

        /**
         * The ExitedEvent is called when the user's mouse pointer leaves the area of a node, and it basically negates the highlighting
         * of {@link EnteredEvent}.
         */
        private class ExitedEvent implements EventHandler<MouseEvent> {

            private Node node;

            public ExitedEvent(Node node) {
                this.node = node;
            }

            @Override
            public void handle(MouseEvent event) {
                Platform.runLater(() -> {
                    node.graph.getEdges().values().forEach(map -> map.values().forEach(edge -> {
                        edge.getTo().getMeta().unhide();
                        edge.getFrom().getMeta().unhide();
                        edge.getMeta().unhide();
                    }));

                    node.graph.getEdges(node.getId()).forEach(e -> {
                        e.getTo().getMeta().highlight(false);
                        e.getMeta().line.strokeProperty().set(ColorList.EDGE_DEFAULT);
                    });
                    outer.setFill(ColorList.NODE_OUTER_DEFAULT);
                });
            }
        }

        /**
         * This method fades the node into the background.
         */
        public void hide() {
            this.outer.getStyleClass().add("disabled");
            this.inner.getStyleClass().add("disabled");
            this.hintCircle.getStyleClass().add("disabled");
        }

        /**
         * This method brings the entire node back, and it is no longer a faded object in the background.
         */
        public void unhide() {
            this.outer.getStyleClass().remove("disabled");
            this.inner.getStyleClass().remove("disabled");
            this.hintCircle.getStyleClass().remove("disabled");
        }

        /**
         *
         * @return True, if the user is allowed to change the colour/value of this node, otherwise false.
         */
        public boolean isAllowedToChangeColour() {
            return allowedToChangeColour;
        }

        /**
         * Set whether or not the user is allowed to change the colour/value of this node.
         * @param allowedToChangeColour True, if they are allowed, otherwise false.
         */
        public void setAllowedToChangeColour(boolean allowedToChangeColour) {
            this.allowedToChangeColour = allowedToChangeColour;
        }

        /**
         * Sets the colour of the node.
         * @param colour
         */
        public void colour(Color colour) {
            this.inner.fillProperty().set(colour);
            updateCircles();
        }

        /**
         * Returns the colour of the node.
         * @return
         */
        public Color colour() {
            return (Color) this.outer.getFill();
        }

        /**
         * Highlights a node by changing the colour of the second-most outer ring.
         * @param highlight True, to enable highlighting, false to turn it off again.
         */
        public void highlight(boolean highlight) {
            Meta.this.outer.fillProperty().setValue(highlight ? ColorList.NODE_HIGHLIGHTED : ColorList.NODE_OUTER_DEFAULT);
        }

        /**
         *
         * @return True, if the node is visible, otherwise false.
         */
        public boolean visible() {
            return this.visible;
        }

        /**
         *
         * @return The text inside the node.
         */
        public String text() {
            return this.textValue;
        }

        /**
         *
         * @return
         */
        public Circle area() {
            //--- if the hintCircle is not filled (no hint is being indicated on this node) then the biggest footprint
            // is the outer-circle, otherwise it is the hintCircle.
            return hintCircle.getFill() == null ? outer : hintCircle;
        }

        /**
         * The x-coordinate of the center of the node.
         * @return
         */
        public double x() {
            return this.positionX;
        }

        /**
         * The y-coordinate of the center of the node.
         * @return
         */
        public double y() {
            return this.positionY;
        }

        /**
         * The radius of the inner node.
         * @return
         */
        public double radius() {
            return this.radius;
        }

        /**
         * Enable the hint function by providing a oolor that is not null. The most outer area of the node will then
         * appear in the provided color.
         * @param color Null, to turn it off, a valid color to change the outer ring of the node.
         */
        public void hint(Color color) {
            Runnable runnable = () -> {
                hintCircle.visibleProperty().set(color != null);
                hintCircle.fillProperty().set(color);
            };
            if(Platform.isFxApplicationThread()) {
                runnable.run();
            } else {
                Platform.runLater(runnable);
            }
        }

        /**
         * Sets the visibility of the node.
         * @param visible True, to make it visible, false to make it invisible.
         */
        public void visible(boolean visible) {
            this.visible = visible;
            updateCircles();
        }

        /**
         * Moves the entire visual representation to this new x-coordinate which is going to be the center of it.
         * @param x
         */
        public void x(double x) {
            this.positionX = x;
            this.updateCircles();
        }

        /**
         * Moves the entire visual representation to this new y-coordinate which is going to be the center of it.
         * @param y
         */
        public void y(double y) {
            this.positionY = y;
            this.updateCircles();
        }

        /**
         * Updates all properties of all objects in one go. This keeps the Platform UI update queue clean which is
         * recommended to keep the UI in a responsive state.
         */
        private void updateCircles() {
            Runnable runnable = () -> {
                Meta.this.outer.visibleProperty().setValue(visible());
                Meta.this.inner.visibleProperty().setValue(visible());
                Meta.this.hintCircle.visibleProperty().setValue(visible());
                Meta.this.text.visibleProperty().setValue(visible());

                if(visible()) {
                    Meta.this.hintCircle.centerXProperty().setValue(x());
                    Meta.this.hintCircle.centerYProperty().setValue(y());
                    Meta.this.hintCircle.centerYProperty().setValue(y());
                    Meta.this.hintCircle.radiusProperty().setValue(radius() * 1.3D);
                    Meta.this.hintCircle.fillProperty().set(Color.color(0, 0, 0, 0));
                    Meta.this.hintCircle.setVisible(hintCircle.getFill() != null);

                    Meta.this.outer.centerXProperty().setValue(x());
                    Meta.this.outer.centerYProperty().setValue(y());
                    Meta.this.outer.radiusProperty().setValue(radius());

                    Meta.this.inner.centerXProperty().setValue(x());
                    Meta.this.inner.centerYProperty().setValue(y());
                    Meta.this.inner.radiusProperty().setValue(radius() * 0.6D);

                    Meta.this.text.textProperty().setValue(textValue);
                    Meta.this.text.xProperty().setValue(x() - (Meta.this.text.getFont().getSize() / 2) * (textValue.length() / 2));
                    Meta.this.text.yProperty().setValue(y() + (Meta.this.text.getFont().getSize() / 4));
                    Meta.this.text.textAlignmentProperty().set(TextAlignment.CENTER);
                    Meta.this.text.setTextAlignment(TextAlignment.CENTER);
                }
            };

            if(Platform.isFxApplicationThread()) {
                runnable.run();
            } else {
                Platform.runLater(runnable);
            }

        }

        public Shape[] getGraphicElements() {
            return new Shape[] { hintCircle, outer, inner, text};
        }
    }

    public static class Edge {

        private Meta meta;
        private Node from;
        private Node to;

        public Edge(Node from, Node to) {
            this.from = from;
            this.to = to;
            this.meta = new Meta();
        }

        public Meta getMeta() {
            return meta;
        }

        public Node getFrom() {
            return this.from;
        }

        public Node getTo() {
            return this.to;
        }

        public class Meta {

            private Line line = new Line();

            public Meta() {

                Platform.runLater(() -> {
                    Map<Integer, Edge> edges = from.graph.getEdgeMap(from.getId());
                    if(edges.containsKey(to.getId()) && !line.strokeProperty().isBound()) {
                        from.graph.getEdge(to.getId(), from.getId()).getMeta().line.strokeProperty().bindBidirectional(Meta.this.line.strokeProperty());
                    }
                    Meta.this.line.strokeProperty().set(ColorList.EDGE_DEFAULT);

                    Meta.this.line.startXProperty().bind(from.getMeta().area().centerXProperty());
                    Meta.this.line.startYProperty().bind(from.getMeta().area().centerYProperty());
                    Meta.this.line.endXProperty().bind(to.getMeta().area().centerXProperty());
                    Meta.this.line.endYProperty().bind(to.getMeta().area().centerYProperty());
                    Meta.this.line.strokeWidthProperty().setValue(2);
                    Meta.this.line.visibleProperty().bind(from.getMeta().area().visibleProperty().and(to.getMeta().area().visibleProperty()));
                });

            }

            public void hide() {
                this.line.getStyleClass().add("disabled");
            }

            public void unhide() {
                this.line.getStyleClass().remove("disabled");
            }

            public boolean visible() {
                return this.line.isVisible();
            }

            public Shape[] getGraphicElements() {
                return new Shape[] {line};
            }
        }

    }

}
