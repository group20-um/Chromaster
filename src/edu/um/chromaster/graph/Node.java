package edu.um.chromaster.graph;

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
        public double displacementX, displacementY;
        public double positionX, positionY;
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
