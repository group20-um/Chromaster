package edu.um.chromaster.graph;

import edu.um.chromaster.ChromaticNumber;
import edu.um.chromaster.graph.Node.Edge;
import java.util.*;
import java.util.stream.Collectors;

public class Graph implements Cloneable {

    private Map<Integer, Node> nodes = new HashMap<>();
    private Map<Integer, Map<Integer, Edge>> edges = new HashMap<>();

    private ChromaticNumber.Result chromaticNumberResult = new ChromaticNumber.Result(-1, -1, -1, false);

    private int minNodeId = Integer.MAX_VALUE;
    private int maxNodeId = Integer.MIN_VALUE;

    /**
     * Creates a new Graph instance.
     */
    public Graph() {}

    /**
     * Returns the results of the calculations and related to the chromatic number.
     * @return Never null, {@link ChromaticNumber.Result#isReady()} is false, if it does not contain any valid results.
     */
    public ChromaticNumber.Result getChromaticResult() {
        return this.chromaticNumberResult;
    }

    /**
     * Update the result of the calculations related to the chromatic number.
     * @param chromaticNumberResult The object that will override the reference of the existing results.
     */
    public void setChromaticResults(ChromaticNumber.Result chromaticNumberResult) {
        this.chromaticNumberResult = chromaticNumberResult;
    }

    /**
     * Sets all {@link Node#id} values to -1.
     */
    public void reset() {
        this.nodes.values().forEach(e -> e.setValue(-1));
    }

    /**
     * Adds a new node with an id and a certain value if it does not already exist, if it already exists, it will be ignored.
     * @param id The id of the new node.
     * @param value The value associated with the new node.
     */
    public void addNode(int id, int value) {
        if(!(this.nodes.containsKey(id))) {
            minNodeId = Math.min(id, minNodeId);
            maxNodeId = Math.max(id, maxNodeId);
            this.nodes.put(id, new Node(this, id, value));
            this.edges.put(id, new HashMap<>());
        }
    }

    /**
     * Adds an edge from node id 'from' to the node with the id 'to'.
     * @param from The {@link Node#getId()} of the node the edge starts at.
     * @param to The {@link Node#getId()} of the node the edge goes to.
     * @param bidirectional If true, it will call {@link Graph#addEdge(int, int, boolean)} with the 'from', and 'id' values
     *                      reversed, and 'bidirectional' set to false.
     */
    public void addEdge(int from, int to, boolean bidirectional) {
        if(!(this.edges.containsKey(from))) {
            this.edges.put(from, new HashMap<>());
        }
        this.edges.get(from).put(to, new Edge(this.getNode(from), this.getNode(to)));
        if(bidirectional) {
            addEdge(to, from, false);
        }
    }

    /**
     * Gets the node with id 'id'.
     * @param id The node of the id to retrieve.
     * @return Returns the {@link Node} reference associated with the provided id.
     * @throws IllegalArgumentException if the provided id does not exist.
     */
    public Node getNode(int id) {
        if(this.nodes.containsKey(id)) {
            return this.nodes.get(id);
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns the next node with an higher id than the provided node.
     * @param start The node where the search starts at.
     * @return Null, if the provided node is the node with id == {@link Graph#getMaxNodeId()}, otherwise the node with
     * the next highest id.
     */
    public Node getNextAvailableNode(Node start) {
        for(int i = start.getId() + 1; i <= maxNodeId; i++) {
            if(this.nodes.containsKey(i)) {
                return this.nodes.get(i);
            }
        }
        return null;
    }

    /**
     * Returns the edge between two nodes.
     * @param from The id of the node where the edge starts.
     * @param to The id of the node where the edge ends.
     * @return Never null, returns the edge between the nodes.
     * @throws IllegalArgumentException if no such edge exists.
     */
    public Edge getEdge(int from, int to) {

        if(!this.edges.containsKey(from) || !this.edges.get(from).containsKey(to)) {
            throw new IllegalArgumentException();
        }

        return this.getEdgeMap(from).get(to);
    }

    /**
     * Returns a Map containing all {@link Edge}s starting at the provided node.
     * @param node The node the edges start at.
     * @return Never null, a map where the key is the id of the node the edge goes to, and the value of the Edge.
     * @throws IllegalArgumentException if
     */
    public Map<Integer, Edge> getEdgeMap(int node) {
        if(!this.edges.containsKey(node)) {
            throw new IllegalArgumentException();
        }

        return this.edges.get(node);
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
