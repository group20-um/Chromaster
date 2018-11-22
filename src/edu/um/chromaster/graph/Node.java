package edu.um.chromaster.graph;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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

    public static class Meta {

        public final static Color defaultColour = Color.rgb(0, 0, 0);
        public Color colour = defaultColour;

        private String textValue;
        private Text text = new Text();
        private Circle outer = new Circle();
        private Circle inner = new Circle();

        private boolean visible = false;
        private boolean allowedToChangeColour = true;
        private boolean highlight = false;

        public double displacementX, displacementY;
        private double positionX, positionY;
        private double radius = 20;

        public Meta(Node node) {
            this.textValue = String.valueOf(node.getId());
            this.text.setFill(Color.WHITE);

            this.inner.getStyleClass().add("node");
            this.outer.getStyleClass().add("node");
            updateCircles();

        }

        public boolean isAllowedToChangeColour() {
            return allowedToChangeColour;
        }

        public void setAllowedToChangeColour(boolean allowedToChangeColour) {
            this.allowedToChangeColour = allowedToChangeColour;
            updateCircles();
        }

        public void highlight(boolean highlight) {
            this.highlight = highlight;
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
            this.outer.visibleProperty().setValue(visible());
            this.inner.visibleProperty().setValue(visible());
            this.text.visibleProperty().setValue(visible());

            if(visible()) {

                this.outer.centerXProperty().setValue(x());
                this.outer.centerYProperty().setValue(y());
                this.outer.radiusProperty().setValue(radius());

                if(highlight()) {
                    this.outer.fillProperty().setValue(Color.YELLOW);
                } else {
                    this.outer.fillProperty().setValue(isAllowedToChangeColour() ? Color.WHITE : Color.DARKGRAY);
                }


                this.inner.centerXProperty().setValue(x());
                this.inner.centerYProperty().setValue(y());
                this.inner.radiusProperty().setValue(radius() * 0.6);
                this.inner.fillProperty().setValue(colour);

                this.text.textProperty().setValue(textValue);
                this.text.xProperty().setValue(x() - (this.text.getFont().getSize() / 2) * (textValue.length() / 2));
                this.text.yProperty().setValue(y() + (this.text.getFont().getSize() / 4));
                this.text.textAlignmentProperty().set(TextAlignment.CENTER);
                this.text.setTextAlignment(TextAlignment.CENTER);
            }

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
            this.meta = new Meta(this);
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

            private Edge edge;

            private Line line = new Line();
            private boolean visible = true;

            public Meta(Edge edge) {
                this.edge = edge;

                this.line.startXProperty().bind(from.getMeta().area().centerXProperty());
                this.line.startYProperty().bind(from.getMeta().area().centerYProperty());
                this.line.endXProperty().bind(from.getMeta().area().centerXProperty());
                this.line.endYProperty().bind(from.getMeta().area().centerYProperty());
                this.line.strokeWidthProperty().setValue(2);
                this.line.visibleProperty().bind(from.getMeta().area().visibleProperty().and(to.getMeta().area().visibleProperty()));

            }

            public boolean visible() {
                return visible;
            }

            public void visible(boolean visible) {
                this.visible = visible;
            }
            public Shape[] getGraphicElements() {
                return new Shape[] {line};
            }
        }

    }

}
