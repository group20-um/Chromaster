package edu.um.chromaster.graph;

import edu.um.chromaster.gui.GraphElement;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Node {

    private final int id;
    private final Meta meta;
    private int value;

    public Node(int id, int value) {
        this.id = id;
        this.meta = new Meta(String.valueOf(id));
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


        private Circle area;
        private String text;

        public boolean visible = false;
        public double displacementX, displacementY;
        private double positionX, positionY;
        private double radius = 10;

        public Meta(String text) {
            this.text = text;
        }

        public String text() {
            return this.text;
        }

        public void text(String text) {
            this.text = text;
        }

        public Circle area() {
            return this.area;
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

        public void x(double x) {
            this.positionX = x;
            this.updateArea();
        }

        public void y(double y) {
            this.positionY = y;
            this.updateArea();
        }

        private void updateArea() {
            this.area = new Circle(x(), y(), radius());
        }
    }

    public static class Edge {

        private Node from;
        private Node to;

        public Edge(Node from, Node to) {
            this.from = from;
            this.to = to;
        }

        public Node getFrom() {
            return this.from;
        }

        public Node getTo() {
            return this.to;
        }

    }

}
