package edu.um.chromaster;

import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;

import java.util.Random;

public class GraphDrawer {

    private final static Random random = new Random();

    private GraphDrawer() {}


    public static void fruchtermanReingold(Graph graph, int width, int height) {
        final int area = width * height;
        double maxDisplacement = Math.sqrt(10000 * area) / 10;
        final double k = Math.sqrt(area / graph.getNodes().size());
        final int iterations = 5;
        final double gravity = 10;
        final double speed_divisior = 800;
        final double speed = 1;

        graph.getNodes().forEach((id, node) -> {
            node.getMeta().positionX = random.nextDouble() * width;
            node.getMeta().positionY = random.nextDouble() * height;
        });

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
