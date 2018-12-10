package edu.um.chromaster.graph;

import edu.um.chromaster.ChromaticNumber;
import edu.um.chromaster.graph.Node.Edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A graph class is a data structure that represents a graph.
 */
public class Graph implements Cloneable {

    private Map<Integer, Node> nodes = new HashMap<>();
    private Map<Integer, Map<Integer, Edge>> edges = new HashMap<>();

    private ChromaticNumber.Result chromaticNumberResult = new ChromaticNumber.Result(null,-1, -1, -1, false);

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
     * Sets all {@link Node#getId()} values to -1.
     */
    public void reset() {
        this.nodes.values().forEach(e -> e.setValue(-1));
    }

    /**
     * Adds a new node with an id and a certain value if it does not already exist, if it already exists, it will be ignored.
     * @param id The id of the new node.
     * @param value The value associated with the new node.
     * @return true, if the node was added, false, if the node already exists.
     */
    public boolean addNode(int id, int value) {
        if(!(this.nodes.containsKey(id))) {
            minNodeId = Math.min(id, minNodeId);
            maxNodeId = Math.max(id, maxNodeId);
            this.nodes.put(id, new Node(this, id, value));
            this.edges.put(id, new HashMap<>());
            return true;
        }
        return false;
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

        if(this.getNode(from) == null) {
            throw new IllegalArgumentException("The from node does not exist.");
        }

        if(this.getNode(to) == null) {
            throw new IllegalArgumentException("The to node does not exist.");
        }

        this.edges.get(from).put(to, new Edge(this.getNode(from), this.getNode(to)));
        if(bidirectional) {
            addEdge(to, from, false);
        }
    }

    /**
     * Gets the node with id 'id'.
     * @param id The node of the id to retrieve.
     * @return Returns the {@link Node} reference associated with the provided id, or null, if the node does not exist.
     */
    public Node getNode(int id) {
        return this.nodes.get(id);
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
     * @return The edge, if the edge between 'from' to 'to' exists, otherwise null.
     */
    public Edge getEdge(int from, int to) {
        return this.getEdgeMap(from).get(to);
    }

    /**
     * Returns a Map containing all {@link Edge}s starting at the provided node.
     * @param node The node the edges start at.
     * @return Never null, a map where the key is the id of the node the edge goes to, and the value of the Edge.
     */
    public Map<Integer, Edge> getEdgeMap(int node) {
        return this.edges.get(node);
    }

    /**
     * Returns a list of all associated edges.
     * @param node The node the edges are supposed to be associated with.
     * @return Never null, a list with all edges where the provided node is the source.
     */
    public List<Edge> getEdges(int node) {
        return new ArrayList<>(this.getEdgeMap(node).values());
    }

    /**
     * All nodes of the map.
     * @return Never null
     */
    public Map<Integer, Node> getNodes() {
        return this.nodes;
    }

    /**
     * All edges of the map.
     * @return Never null.
     */
    public Map<Integer, Map<Integer, Edge>> getEdges() {
        return this.edges;
    }

    /**
     * The max. value that is used as a node id in this graph.
     * @return
     */
    public int getMaxNodeId() {
        return maxNodeId;
    }

    /**
     * The min. value that is used as a node id in this graph.
     * @return
     */
    public int getMinNodeId() {
        return minNodeId;
    }

    @Override
    public Graph clone() {
        Graph clone = new Graph();
        for (Map.Entry<Integer, Node> entry : this.nodes.entrySet()) {
            Integer key = entry.getKey();
            Node value = entry.getValue();
            clone.addNode(key, value.getValue());
        }
        for (Map.Entry<Integer, Map<Integer, Edge>> entry : this.edges.entrySet()) {
            Integer k = entry.getKey();
            Map<Integer, Edge> v = entry.getValue();
            for (Map.Entry<Integer, Edge> e : v.entrySet()) {
                Integer to = e.getKey();
                Edge edge = e.getValue();
                System.out.println(edge.getFrom() + "<from");
                System.out.println(edge.getTo() + "<to");
                clone.addEdge(edge.getFrom().getId(), edge.getTo().getId(), true);
            }
        }
        return clone;
    }

}
