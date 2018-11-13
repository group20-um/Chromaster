package edu.um.chromaster.graph;

import javafx.scene.paint.Color;

public class Node {

    private final int id;
    private final Meta meta = new Meta();
    private int value;

    public Node(int id, int value) {
        this.id = id;
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
        public Color colour = Color.rgb(255, 0, 0);
        public boolean visible = false;
        public double displacementX, displacementY;
        private double positionX, positionY;

        public double x() {
            return this.positionX;
        }

        public double y() {
            return this.positionY;
        }

        public void x(double x) {
            this.positionX = x;
        }

        public void y(double y) {
            this.positionY = y;
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
