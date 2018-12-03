package edu.um.chromaster;

import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;

import java.util.Map;

public class GraphDrawer {

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
                edgeList.forEach((to, edge) -> {
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


    // http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.13.8444&rep=rep1&type=pdf
    public static void test(Graph graph, double width, double height) {

        final double area = width * height;
        final double k = Math.sqrt(area / graph.getNodes().size());
        double t = width / 10D;
        final int iterations = 100;

        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x((Game.random.nextDouble() * width));
            node.getMeta().y((Game.random.nextDouble() * height));
            assert !Double.isNaN(node.getMeta().x());
            assert !Double.isNaN(node.getMeta().y());
        });

        for (int i = 0; i < iterations; i++) {

            System.out.println("iteration: " + i);

            // calculate repulsive force
            for (Node v : graph.getNodes().values()) {
                Node.Meta vM = v.getMeta();
                vM.displacementX = 0;
                vM.displacementY = 0;

                for (Node.Edge e : graph.getEdges(v.getId())) {
                    if (v != e.getTo()) {
                        Node.Meta uM = e.getTo().getMeta();
                        double deltaX = vM.x() - uM.x();
                        double deltaY = vM.y() - uM.y();

                        assert (deltaX == deltaX);
                        assert (deltaY == deltaY);

                        //
                        double a = (deltaX / Math.abs(deltaX)) * repulsiveForce(k, Math.abs(deltaX));
                        double b = (deltaY / Math.abs(deltaY)) * repulsiveForce(k, Math.abs(deltaY));

                        assert !Double.isNaN(a);
                        assert !Double.isNaN(b);
                        vM.displacementX += a;
                        vM.displacementY += b;

                        assert !Double.isNaN(vM.displacementX);
                        assert !Double.isNaN(vM.displacementY);

                    }
                }
            }

            // calculate attractive forces
            for (Map<Integer, Node.Edge> eList : graph.getEdges().values()) {
                for (Node.Edge e : eList.values()) {
                    double deltaX = e.getFrom().getMeta().x() - e.getTo().getMeta().x();
                    double deltaY = e.getFrom().getMeta().y() - e.getTo().getMeta().y();

                    double a = (deltaX / Math.abs(deltaX)) * attractiveForce(k, Math.abs(deltaX));
                    double b = (deltaY / Math.abs(deltaY)) * attractiveForce(k, Math.abs(deltaY));

                    assert !Double.isNaN(a);
                    assert !Double.isNaN(b);

                    e.getFrom().getMeta().displacementX -= a;
                    e.getFrom().getMeta().displacementY -= b;

                    e.getTo().getMeta().displacementX += a;
                    e.getTo().getMeta().displacementY += b;

                    assert !Double.isNaN(e.getFrom().getMeta().displacementX);
                    assert !Double.isNaN(e.getFrom().getMeta().displacementY);
                    assert !Double.isNaN(e.getTo().getMeta().displacementX);
                    assert !Double.isNaN(e.getTo().getMeta().displacementY);
                }
            }


            // limit the maximum displacement
            for (Node v : graph.getNodes().values()) {
                Node.Meta vM = v.getMeta();

                double a = vM.x() + (vM.displacementX / Math.abs(vM.displacementX)) * t;
                double b = vM.y() + (vM.displacementY / Math.abs(vM.displacementY)) * t;
                assert !Double.isNaN(a);
                assert !Double.isNaN(b);


                //vM.x(Math.min(Math.max(a, 1), height));
                //vM.y(Math.min(Math.max(b, 1), width));

                vM.x(a);
                vM.y(b);

                assert !Double.isNaN(vM.x());
                assert !Double.isNaN(vM.y());

            }

            t -= (width / 10D / iterations);

        }


        System.out.println(t);
    }

    public static void test2(Graph graph, double width, double height) {

        double w = 1;
        double h = 1;

        double area = w*h;
        double k = Math.sqrt(area / graph.getNodes().size());

        graph.getNodes().values().forEach(e -> {
            e.getMeta().x(w * Math.random());
            e.getMeta().y(h * Math.random());
        });

        int iterations = 5;
        double t = w/1;
        double dt = t / (iterations + 1);

        for(int i = 0; i < iterations; i++) {

            for(Node v : graph.getNodes().values()) {
                v.getMeta().displacementX = 0;
                v.getMeta().displacementY = 0;

                for(Node u : graph.getNodes().values()) {
                    if(v != u) {
                        double deltaX = v.getMeta().x() - u.getMeta().x();
                        double deltaY = v.getMeta().y() - u.getMeta().y();

                        double delta = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

                        if(delta != 0) {
                            double d = repulsiveForce(k, delta) / delta;
                            v.getMeta().displacementX += deltaX * d;
                            v.getMeta().displacementY += deltaY * d;
                        }
                    }
                }
            }

            for(Map<Integer, Node.Edge> edgeList : graph.getEdges().values()) {
                for(Node.Edge e : edgeList.values()) {
                    Node v = e.getFrom();
                    Node u = e.getTo();

                    double deltaX = v.getMeta().x() - u.getMeta().x();
                    double deltaY = v.getMeta().y() - u.getMeta().y();

                    double delta = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

                    if(delta != 0) {
                        double d = attractiveForce(k, delta) / delta;
                        double ddx = deltaX * d;
                        double ddy = deltaY * d;

                        v.getMeta().displacementX += -ddx;
                        u.getMeta().displacementX += +ddx;
                        v.getMeta().displacementY += -ddy;
                        u.getMeta().displacementY += +ddy;
                    }
                }
            }

            for(Node v : graph.getNodes().values()) {
                double disp = Math.sqrt(Math.pow(v.getMeta().displacementX, 2) + Math.pow(v.getMeta().displacementY, 2));

                if(disp != 0) {
                    double d = Math.min(disp,t) /disp;
                    double x = v.getMeta().x() + v.getMeta().displacementX * d;
                    double y = v.getMeta().y() + v.getMeta().displacementY * d;

                    x = Math.min(w, Math.max(0, x)) - w/2D;
                    y = Math.min(h, Math.max(0, y)) - h/2D;

                    v.getMeta().x(Math.min(Math.sqrt(w*w/4D-y*y), Math.max(-Math.sqrt(w*w/4D-y*y), x)) + w/2D);
                    v.getMeta().y(Math.min(Math.sqrt(h*h/4D-x*x), Math.max(-Math.sqrt(h*h/4D-x*x), y)) + h/2D);
                }
            }

            t -= dt;
        }

        graph.getNodes().values().forEach(e -> {
            e.getMeta().x(e.getMeta().x() * width);
            e.getMeta().y(e.getMeta().y() * height);
        });

    }

    private static double attractiveForce(double k, double x) {
        double y = Math.pow(x, 2) / k;
        assert y != 0 && !Double.isNaN(y);
        return y;
    }

    private static double repulsiveForce(double k, double x) {
        double y = Math.pow(k, 2) / x;
        assert y != 0 && !Double.isNaN(y);
        return y;
    }


}
