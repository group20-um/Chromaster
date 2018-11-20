package edu.um.chromaster;

import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import javafx.scene.paint.Color;

import java.util.Random;

public class GraphDrawer {

    private final static Random random = new Random();

    private GraphDrawer() {}

    public static void circle(Graph graph,double width, double height){
        double angle = 2*Math.PI/graph.getNodes().size();
        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x(width/2 + ((width-200)/2)*Math.cos(id*angle));
            node.getMeta().y(height/2 + ((height-100)/2)*Math.sin(id*angle));
        });
    }

    public static void shell(Graph graph,double width, double height){
        double angle = 2*Math.PI/graph.getNodes().size();
        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x(width/2 + (((width-200)/2) - ((width-200)/2) * id/graph.getNodes().size())*Math.cos(id*angle));
            node.getMeta().y(height/2 + (((height-100)/2) - ((height-200)/2) * id/graph.getNodes().size())*Math.sin(id*angle));
        });
    }

    public static void archemedianSprial(Graph graph,double width, double height){
        double angle = 4*Math.PI/graph.getNodes().size();
        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x(width/2 + (((width-200)/2) - ((width-200)/2) * id/graph.getNodes().size())*Math.cos(id*angle));
            node.getMeta().y(height/2 + (((height-100)/2) - ((height-200)/2) * id/graph.getNodes().size())*Math.sin(id*angle));
        });
    }

    public static void limacon(Graph graph,double width, double height){
        double angle = 2*Math.PI/graph.getNodes().size();
        double r = width;
        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x(width/2 + r*Math.sin(5*angle)*Math.cos(id*angle));
            node.getMeta().y(height/2 + r*Math.sin(5*angle)*Math.sin(id*angle));
        });
    }

    public static void rose(Graph graph,double width, double height){
        double angle = 2*Math.PI/graph.getNodes().size();
        double a = 200;
        int k = 2;
        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x(width/2 + a * Math.cos(k*angle) * Math.cos(angle));
            node.getMeta().y(height/2 + a * Math.cos(k*angle) * Math.sin(angle));
        });
    }

    public static void scale(Graph graph, double width, double height) {
        boolean change = true;
        while (change) {
            for (Node node : graph.getNodes().values()) {
                Node.Meta m = node.getMeta();
                m.x(m.x() * 0.9D);
                m.y(m.y() * 0.9D);

                change = !(m.x() > -width / 2 && m.x() < width / 2 && m.y() > -height / 2 && m.y() < height / 2);
            }
        }

        graph.getNodes().forEach((k, node) -> {
            node.getMeta().x(node.getMeta().x() + width / 2);
            node.getMeta().y(node.getMeta().y() + height / 2);
        });

        /*{
            if (m.x() * 1.2D > -width / 2D && m.x() * 1.2D < width / 2D) {
                if (m.y() * 1.2 > -height / 2 && m.y() * 1.2 < height / 2) {
                    m.x(m.x() * 1.2D);
                    m.y(m.y() * 1.2D);
                }
            }
        }*/

    }

    public static void banana(Graph graph, double width, double height) {

        graph.getNodes().forEach((id, node) -> {
            Node.Meta m = node.getMeta();
            m.x(m.x() + width / 2);
            m.y(m.y() + height / 2);
        });

        final double area = width * height;
        double maxDisplacement = Math.sqrt(10000 * area) / 10;
        final double k = Math.sqrt(area / graph.getNodes().size());
        final int iterations = 100;
        final double gravity = 10;
        final double speed_divisior = 800;
        final double speed = 1;

        for(int i = 0; i < iterations; i++) {

            for (Node a : graph.getNodes().values()) {
                Node.Meta aMeta = a.getMeta();
                for (Node b : graph.getNodes().values()) {
                    if(a != b) {
                        Node.Meta bMeta = b.getMeta();
                        double xDistance = aMeta.x() - bMeta.x();
                        double yDistance = aMeta.y() - bMeta.y();
                        double distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));

                        if(distance > 0) {
                            double repulsiveForce = repulsiveForce(k, distance);
                            aMeta.displacementX += xDistance / distance * repulsiveForce;
                            aMeta.displacementY += yDistance / distance * repulsiveForce;
                        }
                    }
                }
            }

            graph.getEdges().values().forEach(edgeList -> {
                edgeList.forEach(edge -> {
                    Node.Meta aMeta = edge.getFrom().getMeta();
                    Node.Meta bMeta = edge.getTo().getMeta();

                    double xDistance = aMeta.x() - bMeta.x();
                    double yDistance = aMeta.y() - bMeta.y();
                    double distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));

                    if(distance > 0) {
                        double attractiveForce = attractiveForce(k, distance);
                        aMeta.displacementX -= xDistance / distance * attractiveForce;
                        aMeta.displacementY -= yDistance / distance * attractiveForce;
                        bMeta.displacementY += xDistance / distance * attractiveForce;
                        bMeta.displacementY += yDistance / distance * attractiveForce;
                    }
                });
            });

            graph.getNodes().forEach((id, node) -> {
                Node.Meta meta = node.getMeta();
                double distance = Math.sqrt(Math.pow(meta.x(), 2) + Math.pow(meta.y(), 2));
                double gravityForce = 0.01F * k * gravity * distance;
                meta.displacementX = gravityForce * meta.x() / distance;
                meta.displacementY = gravityForce * meta.y() / distance;
            });

            graph.getNodes().forEach((id, node) -> {
                Node.Meta meta = node.getMeta();
                meta.displacementX *= speed / speed_divisior;
                meta.displacementY *= speed / speed_divisior;
            });

            graph.getNodes().forEach((id, node) -> {
                Node.Meta meta = node.getMeta();
                double xDistance = meta.x();
                double yDistance = meta.y();
                double distance = Math.sqrt(Math.pow(meta.x(), 2) + Math.pow(meta.y(), 2));

                if(distance > 0) {
                    double limitedDistance = Math.min(maxDisplacement * (speed / speed_divisior), distance);
                    meta.x(meta.x()+ xDistance / distance * limitedDistance);
                    meta.y(meta.y() + yDistance / distance * limitedDistance);
                }
            });
        }

        int i = 100;
        while (i > 0) {
            graph.getNodes().forEach((id, node) -> {
                Node.Meta m = node.getMeta();
                if (m.x() * 1.2 > -width / 2 && m.x() * 1.2 < width / 2) {
                    m.x(m.x() * 1.2);
                    m.y(m.y() * 1.2);
                } else {
                    m.x(m.x() * 0.8);
                    m.y(m.y() * 0.8);
                }

                if (m.y() * 1.2 > -height / 2 && m.y() * 1.2 < height / 2) {
                    m.x(m.x() * 1.2);
                    m.y(m.y() * 1.2);
                } else {
                    m.x(m.x() * 0.8);
                    m.y(m.y() * 0.8);
                }

            });
            i--;
        }

    }

    private static double attractiveForce(double k, double x) {
        return Math.pow(x, 2) / k;
    }

    private static double repulsiveForce(double k, double x) {
        return Math.pow(k, 2) / x;
    }

}
