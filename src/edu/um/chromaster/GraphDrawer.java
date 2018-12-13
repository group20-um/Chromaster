package edu.um.chromaster;

import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The Graphdrawer class will enable us to choose different representations of the graph
 */
public class GraphDrawer {

    private GraphDrawer() {}

    /**
     * The random method changes the coordinates of the nodes.
     * This is done by changing the values of the node's meta class
     * The method will take random values within the screen dimensions and checks if it does not intersect with
     * another node if it does it will change the values until it finds a suiting position
     * @param graph graph that should get displayed
     * @param width width of the screen
     * @param height height of the screen
     */
    public static void random(Graph graph, double width, double height) {
        graph.getNodes().forEach((id, node) -> {
            final AtomicReference<Double> x = new AtomicReference<>(Game.random.nextDouble() * width);
            final AtomicReference<Double> y = new AtomicReference<>(Game.random.nextDouble() * height);
            x.set(Game.random.nextDouble() * width);
            y.set(Game.random.nextDouble() * height);
            while (graph.getNodes().values().stream().anyMatch(e -> Math.sqrt(Math.pow(x.get() - e.getMeta().x(), 2) + Math.pow(y.get() - e.getMeta().y(), 2)) < Node.Meta.DEFAULT_RADIUS * 3)) {
                x.set(Game.random.nextDouble() * width);
                y.set(Game.random.nextDouble() * height);
            }

            node.getMeta().x(x.get());
            node.getMeta().y(y.get());

        });
    }

    /**
     * The circle method changes the coordinates of the node in such way that
     * they are aligned in a circle
     * The method simply generates a proportional angle for the nodes and takes
     * a radius and then multiplies the radius where the points should be on
     * and then takes the mathematical formula to apply this idea
     * This is done by changing the values of the node's meta class
     * @param graph graph that should get displayed
     * @param width width of the screen
     * @param height height of the screen
     */
    public static void circle(Graph graph,double width, double height){
        double angle = 2*Math.PI/graph.getNodes().size();
        double r = Math.min(width, height)/2D;
        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x(width*4/3 + (r*Math.cos(id*angle) -  r/2));
            node.getMeta().y(height/2 + (r*Math.sin(id*angle)));
        });
    }

    /**
     * The shell method changes the coordinates of the node in such way that
     * they are aligned in a shell
     * The method simply generates a proportional angle for the nodes and takes
     * a radius and then multiplies the radius (which becomes smaller) where the points should be on
     * and then takes the mathematical formula to apply this idea
     * This is done by changing the values of the node's meta class
     * @param graph graph that should get displayed
     * @param width width of the screen
     * @param height height of the screen
     */
    public static void shell(Graph graph,double width, double height){
        double angle = 2*Math.PI/graph.getNodes().size();
        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x(width/2 + (((width-200)/2) - ((width-200)/2) * id/graph.getNodes().size())*Math.cos(id*angle));
            node.getMeta().y(height/2 + (((height-100)/2) - ((height-200)/2) * id/graph.getNodes().size())*Math.sin(id*angle));
        });
    }

    /**
     * The archemedianSpiral method changes the coordinates of the node in such way that
     * they are aligned in a archemedianSpiral (this method is similar to the shell method)
     * The method simply generates a proportional angle for the nodes and takes
     * a radius and then multiplies the radius (which becomes smaller) where the points should be on
     * and then takes the mathematical formula to apply this idea
     * This is done by changing the values of the node's meta class
     * @param graph graph that should get displayed
     * @param width width of the screen
     * @param height height of the screen
     */
    public static void archemedianSpiral(Graph graph, double width, double height){
        double angle = 4*Math.PI/graph.getNodes().size();
        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x(width/2 + (((width-200)/2) - ((width-200)/2) * id/graph.getNodes().size())*Math.cos(id*angle));
            node.getMeta().y(height/2 + (((height-100)/2) - ((height-200)/2) * id/graph.getNodes().size())*Math.sin(id*angle));
        });
    }

    /**
     * The butterfly method changes the coordinates of the node in such way that
     * they are aligned in a butterfly
     * The method uses the polar form of the function and changes it to cartesian coordinates
     * and putting it into the node's coordinates
     * This is done by changing the values of the node's meta class
     * @param graph graph that should get displayed
     * @param width width of the screen
     * @param height height of the screen
     */
    public static void butterfly(Graph graph, double width, double height){
        graph.getNodes().forEach((id, node) -> {
            double theta = id*(2*Math.PI/graph.getNodes().size());
            double r = (1-Math.cos(theta)*Math.sin(3*theta)) * 250;
            double x = Math.cos(theta)*r;
            double y = Math.sin(theta)*r;
            node.getMeta().x(width/2 + x);
            node.getMeta().y(height/2D + y);
        });
        scale(graph, width, height);
    }

    /**
     * The valentineCurve method changes the coordinates of the node in such way that
     * they are aligned in a starshaped form (by using the specific equation we used)
     * The method uses the polar form of the function and changes it to cartesian coordinates
     * and putting it into the node's coordinates
     * This is done by changing the values of the node's meta class
     * @param graph graph that should get displayed
     * @param width width of the screen
     * @param height height of the screen
     */
    public static void valentineCurve(Graph graph, double width, double height){
        graph.getNodes().forEach((id, node) -> {
            double theta = id*(2*Math.PI/graph.getNodes().size());
            double r = (4+Math.cos(6*theta)) * 50;
            double x = Math.cos(theta)*r;
            double y = Math.sin(theta)*r;
            node.getMeta().x(width/2 + x);
            node.getMeta().y(height/2D + y);
        });
        scale(graph, width, height);
    }

    /**
     * The scale method changes the coordinates of the node in such way that
     * they are just scaled by a specific factor
     * This is done by changing the values of the node's meta class
     * @param graph graph that should get displayed
     * @param width width of the screen
     * @param height height of the screen
     */
    public static void scale(Graph graph, double width, double height) {

        double max_x = graph.getNodes().values().stream().mapToDouble(e -> e.getMeta().x()).max().getAsDouble();
        double max_y = graph.getNodes().values().stream().mapToDouble(e -> e.getMeta().y()).max().getAsDouble();

        graph.getNodes().values().forEach(e -> {
            e.getMeta().x((e.getMeta().x() / max_x) * width * 0.9D);
            e.getMeta().y((e.getMeta().y() / max_y) * height * 0.9D);
        });

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
