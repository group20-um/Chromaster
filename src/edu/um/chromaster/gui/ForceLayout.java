package edu.um.chromaster.gui;

import edu.um.chromaster.Game;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;

public class ForceLayout {

    private Graph graph;
    private double width;
    private double height;

    public ForceLayout(Graph graph, double width, double height) {
        this.graph = graph;
        this.width = width;
        this.height = height;
    }

    public void run() {

        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x(Game.random.nextDouble() * width);
            node.getMeta().y(Game.random.nextDouble() * height);
        });

        int iterations = 100;
        for(int i = 0; i < iterations; i++) {
            //graph.getNodes().forEach((id, node) -> wallForce(node));
        }

    }

    private void wallForce(Node node) {

        double topDistance = node.getMeta().y();
        double bottomDistance = this.width - node.getMeta().y();
        double leftDistance = node.getMeta().x();
        double rightDistance = this.height - node.getMeta().x();

        Vector top = Vector.of(0, -(topDistance/(height/2D)));
        Vector bottom = Vector.of(0, (bottomDistance/(height/2D)));
        Vector left = Vector.of(-(leftDistance/(width/2D)), 0);
        Vector right = Vector.of((rightDistance/(width/2D)), 0);

        Vector unit = top.add(bottom).add(left).add(right);
        System.out.println(unit.getX() + " - " + unit.getY());
        node.getMeta().x(node.getMeta().x() + unit.getX());
        node.getMeta().y(node.getMeta().y() + unit.getY());

    }

    private static class Vector {

        private double x;
        private double y;
        private double length;

        public Vector(double x, double y) {
            this.x = x;
            this.y = y;
            this.length = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getLength() {
            return length;
        }

        public Vector add(Vector v) {
            return Vector.of(this.x + v.getX(), this.y + v.getY());
        }

        public Vector mul(Vector v) {
            return Vector.of(this.x * v.getX(), this.y * v.getY());
        }

        public Vector mul(double v) {
            return Vector.of(this.x * v, this.y * v);
        }

        public static Vector of(double x, double y) {
            return new Vector(x, y);
        }

        public static Vector of(Node a, Node b) {
            return new Vector(a.getMeta().x()-b.getMeta().x(), a.getMeta().y() - b.getMeta().y());
        }

    }

}
