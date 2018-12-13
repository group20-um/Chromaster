package edu.um.chromaster;

import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The class HintManager provides the different hint methods used in the game
 */
public class HintManager {

    private HintManager() {}

    /**
     * The method chromaticNumber returns the exact chromatic number of the graph
     * @param graph the considered graph
     * @return returns the chromatic number of the graph
     */
    public static int chromaticNumber(Graph graph) {
        return graph.getChromaticResult().getExact();
    }

    /**
     * The method highestDegree runs through the graph and gives
     * the Node with the highest degree back
     * @param graph the considered graph
     * @return returns the Node with the highest degree (most neighbours)
     */
    public static Node highestDegree(Graph graph) {
        return graph.getNode(graph.getEdges().entrySet().stream().max(Comparator.comparingInt(o -> o.getValue().size())).get().getKey());
    }

    /**
     * The method maxNeighboursColoured runs through the graph and gives
     * the Node with the most amount of colored neighbours back
     * @param graph the considered graph
     * @return returns the Node with the highest amount of neighbours colored
     */
    public static Node maxNeighboursColoured(Graph graph) {
        return graph.getNode(graph.getEdges().entrySet().stream()
                .max(Comparator.comparingInt(o -> (int) o.getValue().values().stream().filter(e -> e.getTo().getValue() != -1).count())).get().getKey());
    }

    /**
     * The method cliqueDetector9000 runs through the graph and gives back the
     * biggest clique in the graph
     * @param graph the considered graph
     * @return returns a list of Nodes which build the highest clique
     */
    public static List<Node> cliqueDetector9000(Graph graph) {
        List<List<Node>> cliques = new LinkedList<>();
        cliqueDetector9000(graph, cliques, new ArrayList<>(), new ArrayList<>(graph.getNodes().values()), new ArrayList<>());
        return cliques.stream().max(Comparator.comparingInt(List::size)).get();
    }

    /**
     * The method cliqueDetector9000 runs through the graph and gives back a list with all the cliques
     * within the graph
     * @param graph the considered graph
     * @param cliques the list of cliques
     * @param _R set of current Nodes in the clique
     * @param _P set of candidate Nodes
     * @param _X set of excluded Nodes
     * @return gives back the list of cliques
     */
    private static List<List<Node>> cliqueDetector9000(Graph graph, List<List<Node>> cliques, List<Node> _R, List<Node> _P, List<Node> _X) {
        if (_P.isEmpty() && _X.isEmpty()) {
            cliques.add(_R);
        }

        Iterator<Node> nodeIterator = _P.iterator();
        while (nodeIterator.hasNext()) {

            //---
            Node node = nodeIterator.next();
            java.util.List<Node> neighbours = graph.getEdges(node.getId()).stream().map(Node.Edge::getTo).collect(Collectors.toList());

            //---
            java.util.List<Node> dR = new ArrayList<>(_R);
            dR.add(node);

            java.util.List<Node> dP = _P.stream().filter(neighbours::contains).collect(Collectors.toList());
            java.util.List<Node> dX = _X.stream().filter(neighbours::contains).collect(Collectors.toList());

            cliqueDetector9000(graph, cliques, dR, dP, dX);

            //---
            nodeIterator.remove();
            _X.add(node);
        }

        return cliques;
    }

}
