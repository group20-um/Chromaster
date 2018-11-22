package edu.um.chromaster.graph;

import edu.um.chromaster.graph.Node.Edge;
import java.util.*;
import java.util.stream.Collectors;

public class Graph implements Cloneable {

    private Map<Integer, Node> nodes = new HashMap<>();
    private Map<Integer, Map<Integer, Edge>> edges = new HashMap<>();

    private int chromaticNumber = -1;

    private int minNodeId = Integer.MAX_VALUE;
    private int maxNodeId = Integer.MIN_VALUE;

    public Graph() {}

    public int getChromaticNumber() {
        return this.chromaticNumber;
    }

    public void setChromaticNumber(int chromaticNumber) {
        this.chromaticNumber = chromaticNumber;
    }

    public void reset() {
        this.nodes.values().forEach(e -> e.setValue(-1));
    }

    public void addNode(int id, int value) {
        minNodeId = Math.min(id, minNodeId);
        maxNodeId = Math.max(id, maxNodeId);
        if(!(this.nodes.containsKey(id))) {
            this.nodes.put(id, new Node(this, id, value));
        }
    }

    public void addEdge(int from, int to, boolean bidirectional) {
        if(!(this.edges.containsKey(from))) {
            this.edges.put(from, new HashMap<>());
        }
        this.edges.get(from).put(to, new Edge(this.getNode(from), this.getNode(to)));
        if(bidirectional) {
            addEdge(to, from, false);
        }
    }

    public Node getNode(int i) {
        if(this.nodes.containsKey(i)) {
            return this.nodes.get(i);
        }
        throw new IllegalArgumentException();
    }

    public Node getNextAvailableNode(Node start) {
        for(int i = start.getId() + 1; i <= maxNodeId; i++) {
            if(this.nodes.containsKey(i)) {
                return this.nodes.get(i);
            }
        }
        return null;
    }

    public Edge getEdge(int from, int to) {
        return this.getEdgeMap(from).get(to);
    }

    public Map<Integer, Edge> getEdgeMap(int node) {
        return this.edges.getOrDefault(node, Collections.emptyMap());
    }

    public List<Edge> getEdges(int node) {
        return new ArrayList<>(this.getEdgeMap(node).values());
    }

    public Map<Integer, Node> getNodes() {
        return this.nodes;
    }

    public Map<Integer, Map<Integer, Edge>> getEdges() {
        return this.edges;
    }

    public int getMaxNodeId() {
        return maxNodeId;
    }

    public int getMinNodeId() {
        return minNodeId;
    }

    @Override
    public Graph clone() {
        Graph clone = new Graph();
        this.nodes.forEach((k, v) -> clone.addNode(k, v.getValue()));
        this.edges.forEach((k, v) -> v.forEach((to, edge) -> clone.addEdge(edge.getFrom().getId(), edge.getTo().getId(), true)));
        return clone;
    }
}
