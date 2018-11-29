package edu.um.chromaster.graph;

import edu.um.chromaster.gui.ColorList;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Node {

    private final Graph graph;
    private final int id;
    private final Meta meta;
    private int value = -1;

    protected Node(Graph graph, int id, int value) {
        this.graph = graph;
        this.id = id;
        this.meta = new Meta(this);
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Meta getMeta() {
        return this.meta;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public class Meta {

        public final static double DEFAULT_RADIUS = 20;

        private String textValue;
        private Text text = new Text();
        private Circle outer = new Circle();
        private Circle inner = new Circle();

        private boolean visible = false;
        private boolean allowedToChangeColour = true;
        private boolean highlight = false;

        public double displacementX, displacementY;
        private double positionX, positionY;
        private double radius = DEFAULT_RADIUS;



        public Meta(Node node) {
            this.textValue = String.valueOf(node.getId());
            this.text.setFill(ColorList.NODE_TEXT_DEFAULT);

            EnteredEvent enteredEvent = new EnteredEvent(node);
            inner.setOnMouseEntered(enteredEvent);
            outer.setOnMouseEntered(enteredEvent);
            text.setOnMouseEntered(enteredEvent);

            ExitedEvent exitedEvent = new ExitedEvent(node);
            inner.setOnMouseExited(exitedEvent);
            outer.setOnMouseExited(exitedEvent);
            text.setOnMouseExited(exitedEvent);

            this.inner.getStyleClass().add("node");
            this.outer.getStyleClass().add("node_border");
            updateCircles();

        }

        private class EnteredEvent implements EventHandler<MouseEvent> {

            private Node node;

            public EnteredEvent(Node node) {
                this.node = node;
            }

            @Override
            public void handle(MouseEvent event) {
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
            }
        }

        private class ExitedEvent implements EventHandler<MouseEvent> {

            private Node node;

            public ExitedEvent(Node node) {
                this.node = node;
            }

            @Override
            public void handle(MouseEvent event) {
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
            }
        }


        public void hide() {
            this.outer.getStyleClass().add("disabled");
            this.inner.getStyleClass().add("disabled");
        }

        public void unhide() {
            this.outer.getStyleClass().remove("disabled");
            this.inner.getStyleClass().remove("disabled");
        }


        public boolean isAllowedToChangeColour() {
            return allowedToChangeColour;
        }

        public void setAllowedToChangeColour(boolean allowedToChangeColour) {
            this.allowedToChangeColour = allowedToChangeColour;
        }

        public void colour(Color colour) {
            this.inner.fillProperty().set(colour);
            updateCircles();
        }

        public Color colour() {
            // TODO look up if this cast is actually save
            return (Color) this.outer.getFill();
        }

        public void highlight(boolean highlight) {
            this.highlight = highlight;
            updateCircles();
        }

        public boolean highlight() {
            return this.highlight;
        }

        public boolean visible() {
            return this.visible;
        }

        public String text() {
            return this.textValue;
        }

        public void text(String text) {
            this.textValue = text;
            updateCircles();
        }

        public Circle area() {
            return this.outer;
        }

        public double x() {
            return this.positionX;
        }

        public double y() {
            return this.positionY;
        }

        public double radius() {
            return this.radius;
        }

        public void visible(boolean visible) {
            this.visible = visible;
            updateCircles();
        }

        public void x(double x) {
            this.positionX = x;
            this.updateCircles();
        }

        public void y(double y) {
            this.positionY = y;
            this.updateCircles();
        }

        private void updateCircles() {
            Platform.runLater(() -> {
                Meta.this.outer.visibleProperty().setValue(visible());
                Meta.this.inner.visibleProperty().setValue(visible());
                Meta.this.text.visibleProperty().setValue(visible());

                if(visible()) {

                    Meta.this.outer.centerXProperty().setValue(x());
                    Meta.this.outer.centerYProperty().setValue(y());
                    Meta.this.outer.radiusProperty().setValue(radius());

                    if(highlight()) {
                        Meta.this.outer.fillProperty().setValue(ColorList.NODE_HIGHLIGHTED);
                    } else if(isAllowedToChangeColour()) {
                        Meta.this.outer.fillProperty().set(ColorList.NODE_OUTER_DEFAULT);
                    }

                    Meta.this.inner.centerXProperty().setValue(x());
                    Meta.this.inner.centerYProperty().setValue(y());
                    Meta.this.inner.radiusProperty().setValue(radius() * 0.6);

                    Meta.this.text.textProperty().setValue(textValue);
                    Meta.this.text.xProperty().setValue(x() - (Meta.this.text.getFont().getSize() / 2) * (textValue.length() / 2));
                    Meta.this.text.yProperty().setValue(y() + (Meta.this.text.getFont().getSize() / 4));
                    Meta.this.text.textAlignmentProperty().set(TextAlignment.CENTER);
                    Meta.this.text.setTextAlignment(TextAlignment.CENTER);
                }

            });
        }

        public Shape[] getGraphicElements() {
            return new Shape[] {outer, inner, text};
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
