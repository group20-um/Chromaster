package edu.um.chromaster;

import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ChromaticNumber {

    public static Graph graph;
    private static ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(2);

    //---
    private final static long TIME_LIMIT_EXACT = TimeUnit.SECONDS.toNanos(60);
    private final static long TIME_LIMIT_LOWER = TimeUnit.SECONDS.toNanos(10);
    private final static long TIME_LIMIT_UPPER = TimeUnit.SECONDS.toNanos(10);

    public enum Type {
        UPPER,
        LOWER,
        EXACT
    }

    public static void computeAsync(Type type, Graph graph, Consumer<Integer> consumer) {
        CompletableFuture.supplyAsync(() -> compute(type, graph, false), schedule).thenAccept(consumer);
    }

    public static int compute(Type type, Graph graph, boolean runTimeBound) {
        graph.reset();

        switch (type) {

            case LOWER: return runTimeBound ? limitedTimeLowerBound(graph) : lowerBound(graph);
            case UPPER: return runTimeBound ? limitedTimeUpper(graph) : upperBound(graph);
            case EXACT: return runTimeBound ? limitedTimeExactTest(graph) : exactTest(graph, false);

        }
        throw new IllegalStateException();
    }

    //---
    private static int limitedTimeExactTest(Graph graph) {
        return timeBoundMethodExecution(new MethodRunnable() {
            @Override
            public void run() {
                this.setResult(exactTest(graph, true));
            }
        }, TIME_LIMIT_EXACT);
    }

    private static int limitedTimeLowerBound(Graph graph) {
        int result = timeBoundMethodExecution(new MethodRunnable() {
            @Override
            public void run() {
                this.setResult(lowerBound(graph));
            }
        }, TIME_LIMIT_LOWER);

        if(result == -1) {
            result = basicLowerBound(graph);
        }

        return result;
    }

    private static int basicLowerBound(Graph graph) {
        int tmp = graph.getEdges().entrySet().stream().mapToInt(e -> e.getValue().size()).min().getAsInt();
        return (tmp == 1) ? 2 : tmp;
    }

    private static int limitedTimeUpper(Graph graph) {
        return timeBoundMethodExecution(new MethodRunnable() {
            @Override
            public void run() {
                this.setResult(upperBound(graph));
            }
        }, TIME_LIMIT_UPPER);
    }

    // --- EXACT SECTION ---
    private static int exactTest(Graph graph, boolean runTimeBound) {
        //---
        int upper = runTimeBound ? limitedTimeUpper(graph) : upperBound(graph);
        AtomicInteger lower = new AtomicInteger(runTimeBound ? limitedTimeLowerBound(graph) : basicLowerBound(graph));
        if(!runTimeBound) {
            int finalUpper = upper;
            CompletableFuture.supplyAsync(() -> lowerBound(graph)).thenAccept((result) -> {
                synchronized (lower) {
                    lower.set(result);
                    System.out.printf("<Exact Test> Updated lower bound: %d%n", lower.get());
                    System.out.printf("<Exact Test> Range: [%d..%d]%n", lower.get(), finalUpper);
                }
            });
        }

        System.out.printf("<Exact Test> Range: [%d..%d]%n", lower.get(), upper);

        if(lower.get() == upper) {
            ChromaticNumber.graph = graph;
            return lower.get();
        }

        upper--;
        graph.reset();

        Graph result = graph.clone();
        while(exact(graph, upper)) {
            System.out.printf("<Exact Test> The graph CAN be coloured with %d colours.%n", upper);
            result = graph.clone();
            graph.reset();

            if(upper == lower.get()) {
                upper--;
                break;
            }
            upper--;

        }

        ChromaticNumber.graph = result;

        final int exact = upper+1;
        System.out.println("<Exact Test> Exact: " + exact);
        return exact;
    }

    private static boolean exact(Graph graph, int colours) {
        return exact(graph, colours, graph.getNode(graph.getMinNodeId()), 0, 0);
    }

    private static boolean exact(Graph graph, int color_nb, Node node, int level, int maxl) {
        maxl = Math.max(maxl, level);

        //--- Are all nodes coloured? If so, we are done.
        if(graph.getNodes().values().stream().noneMatch(e -> e.getValue() == -1)) {
            return true;
        }

        //--- Check this note for all colours
        for(int c = 1; c <= color_nb; c++) {
            if(exactIsColourAvailable(graph, node, c)) {
                node.setValue(c);

                Node next = graph.getNextAvailableNode(node);
                if(next == null || exact(graph, color_nb, next, level + 1, maxl)) {
                    return true;
                }

                node.setValue(-1);
            }
        }

        return false;
    }

    private static boolean exactIsColourAvailable(Graph graph, Node node, int colour) {
        return graph.getEdges(node.getId()).stream().noneMatch(e -> e.getTo().getValue() == colour);
    }

    // --- UPPER BOUND SECTION ---
    private static int upperBound(Graph graph) {
        return upperBoundIterative(graph);
    }

    private static int simpleUpperBound(Graph graph) {
        return graph.getEdges().values().stream().mapToInt(List::size).max().getAsInt() + 1;
    }

    private static int upperBoundIterative(Graph graph) {
        //--- Build unvisited map ordered by degree of nodes descending
        Stack<Node> unvisited = graph.getNodes().values().stream()
                .sorted(Comparator.comparingInt(o -> graph.getEdges(o.getId()).size()))
                .collect(Collectors.toCollection(Stack::new));
        int max = 0;
        while (!unvisited.isEmpty()){
            Node node = unvisited.pop();

            //--- What colours does its neighbours have?
            List<Node.Edge> edges = graph.getEdges(node.getId());
            List<Integer> colours = edges.stream()
                    .filter(edge -> edge.getTo().getValue() != -1)
                    .map(edge -> edge.getTo().getValue())
                    .collect(Collectors.toList());

            //--- No colours -> first node being visited in the graph
            if (colours.isEmpty()) {
                node.setValue(0);
            }
            //--- At least one colour -> not the first node anymore
            else {

                //--- "Highest"  value/colour adjacent to the node
                final int maxColour = colours.stream().max(Comparator.naturalOrder()).get();

                int colour = 0; // Lowest value we can chose for a valid colour

                //--- try to ideally find an existing colour that we can reuse
                while (colour <= maxColour) {
                    if (!colours.contains(colour)) {
                        break;
                    }
                    colour++;
                }

                node.setValue(colour);
                max = Math.max(max, colour);

            }

        }

        return max + 1;

    }

    private static int simpleGreedyUpperBound(Graph graph) {
        HashMap<Integer, Node> unvisited = new LinkedHashMap<>();
        Map.Entry<Integer, Node> entry = graph.getNodes().entrySet().stream().findFirst().get();
        unvisited.put(entry.getKey(), entry.getValue());

        int max = 0;
        while (!unvisited.isEmpty()){
            // is this (too) slow?
            Node node = unvisited.values().stream().findFirst().get();
            unvisited.remove(node.getId());

            //--- What colours does its neighbours have?
            List<Node.Edge> edges = graph.getEdges(node.getId());
            List<Integer> colours = edges.stream()
                    .filter(edge -> edge.getTo().getValue() != -1)
                    .map(edge -> edge.getTo().getValue())
                    .collect(Collectors.toList());

            //--- No colours -> first node being visited in the graph
            if (colours.isEmpty()) {
                node.setValue(0);
            }
            //--- At least one colour -> not the first node anymore
            else {

                //--- "Highest"  value/colour adjacent to the node
                final int maxColour = colours.stream().max(Comparator.naturalOrder()).get();

                int colour = 0; // Lowest value we can chose for a valid colour

                //--- try to ideally find an existing colour that we can reuse
                while (colour <= maxColour) {
                    if (!colours.contains(colour)) {
                        break;
                    }
                    colour++;
                }

                node.setValue(colour);
                max = Math.max(max, colour);

            }

            //--- call for neighbour nodes & figure out the "highest" value/colour used
            for (Node.Edge edge : edges) {
                if (edge.getTo().getValue() == -1) {
                    Node e = edge.getTo();
                    unvisited.put(e.getId(), e);
                }
            }

        }

        return max + 1;

    }


    //--- LOWER BOUND --

    private static int lowerBound(Graph graph) {
        return bronKerbosch(graph, new ArrayList<>(), new ArrayList<>(graph.getNodes().values()), new ArrayList<>());
    }
    private static int bronKerbosch(Graph graph, List<Node> _R, List<Node> _P, List<Node> _X) {
        int max = Integer.MIN_VALUE;
        if(_P.isEmpty() && _X.isEmpty()) {
            max = Math.max(max, _R.size());
        }

        Iterator<Node> nodeIterator = _P.iterator();
        while (nodeIterator.hasNext()) {

            //---
            Node node = nodeIterator.next();
            List<Node> neighbours = graph.getEdges(node.getId()).stream().map(Node.Edge::getTo).collect(Collectors.toList());

            //---
            List<Node> dR = new ArrayList<>(_R);
            dR.add(node);

            List<Node> dP = _P.stream().filter(neighbours::contains).collect(Collectors.toList());
            List<Node> dX = _X.stream().filter(neighbours::contains).collect(Collectors.toList());

            max = Math.max(bronKerbosch(graph, dR, dP, dX), max);

            //---
            nodeIterator.remove();
            _X.add(node);
        }

        return max;
    }

    //--- Utility
    public static int timeBoundMethodExecution(MethodRunnable runnable, final long timeInMilliseconds) {
        Thread thread = new Thread(runnable);
        thread.start();
        long time = System.nanoTime();
        long countdown = time + timeInMilliseconds;

        // TODO replace busy waiting
        while (runnable.getResult() == -1 && time < countdown) {
            System.out.print(""); //
            time = System.nanoTime();
        }
        //thread.interrupt();

        return runnable.getResult();

    }

    private static abstract class MethodRunnable implements Runnable {

        private int result = -1;

        @Override
        public abstract void run();

        public void setResult(int result) {
            this.result = result;
        }

        public int getResult() {
            return this.result;
        }

    }

}
