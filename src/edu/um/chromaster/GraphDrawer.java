package edu.um.chromaster;

import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;

import java.util.Random;

public class GraphDrawer {

    private final static Random random = new Random();

    private GraphDrawer() {}

    public static void circle(Graph graph,double width, double height){
        double angle = 2*Math.PI/graph.getNodes().size();
        graph.getNodes().forEach((id, node) -> {
            node.getMeta().positionX = width/2 + ((width-200)/2)*Math.cos(id*angle);
            node.getMeta().positionY = height/2 + ((height-100)/2)*Math.sin(id*angle);
        });
    }

    public static void shell(Graph graph,double width, double height){
        double angle = 2*Math.PI/graph.getNodes().size();
        graph.getNodes().forEach((id, node) -> {
            node.getMeta().positionX = width/2 + (((width-200)/2) - ((width-200)/2) * id/graph.getNodes().size())*Math.cos(id*angle);
            node.getMeta().positionY = height/2 + (((height-100)/2) - ((height-200)/2) * id/graph.getNodes().size())*Math.sin(id*angle);
        });
    }

    public static void scale(Graph graph, double width, double height) {
        graph.getNodes().forEach((id, node) -> {
            Node.Meta m = node.getMeta();
            if(m.positionX * 1.2 > -width / 2 && m.positionX * 1.2 < width/2) {
                if(m.positionY * 1.2 > -height / 2 && m.positionY * 1.2 < height/2) {
                    m.positionX *= 1.2;
                    m.positionY *= 1.2;
                }
            } else {
                m.positionX *= 0.8;
                m.positionY *= 0.8;
            }
            m.positionX += width/2;
            m.positionY += height/2;
        });
    }

    public static void banana(Graph graph, double width, double height) {
        final double area = width * height;
        double maxDisplacement = Math.sqrt(10000 * area) / 10;
        final double k = Math.sqrt(area / graph.getNodes().size());
        final int iterations = 100;
        final double gravity = 1;
        final double speed_divisior = 800;
        final double speed = 1;

        for(int i = 0; i < iterations; i++) {

            for (Node a : graph.getNodes().values()) {
                Node.Meta aMeta = a.getMeta();
                for (Node b : graph.getNodes().values()) {
                    if(a != b) {
                        Node.Meta bMeta = b.getMeta();
                        double xDistance = aMeta.positionX - bMeta.positionX;
                        double yDistance = aMeta.positionY - bMeta.positionY;
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

                    double xDistance = aMeta.positionX - bMeta.positionX;
                    double yDistance = aMeta.positionY - bMeta.positionY;
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
                double distance = Math.sqrt(Math.pow(meta.positionX, 2) + Math.pow(meta.positionY, 2));
                double gravityForce = 0.01F * k * gravity * distance;
                meta.displacementX = gravityForce * meta.positionX / distance;
                meta.displacementY = gravityForce * meta.positionY / distance;
            });

            graph.getNodes().forEach((id, node) -> {
                Node.Meta meta = node.getMeta();
                meta.displacementX *= speed / speed_divisior;
                meta.displacementY *= speed / speed_divisior;
            });

            graph.getNodes().forEach((id, node) -> {
                Node.Meta meta = node.getMeta();
                double xDistance = meta.positionX;
                double yDistance = meta.positionY;
                double distance = Math.sqrt(Math.pow(meta.positionX, 2) + Math.pow(meta.positionY, 2));

                if(distance > 0) {
                    double limitedDistance = Math.min(maxDisplacement * (speed / speed_divisior), distance);
                    meta.positionX += xDistance / distance * limitedDistance;
                    meta.positionY += yDistance / distance * limitedDistance;
                }
            });
        }

    }

    private static double attractiveForce(double k, double x) {
        return Math.pow(x, 2) / k;
    }

    private static double repulsiveForce(double k, double x) {
        return Math.pow(k, 2) / x;
    }

}
