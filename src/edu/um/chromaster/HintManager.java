package edu.um.chromaster;

import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;

import java.util.*;
import java.util.stream.Collectors;

public class HintManager {

    private HintManager() {
    }

    public static Node highestDegree(Graph graph) {
        return graph.getNode(graph.getEdges().entrySet().stream().max(Comparator.comparingInt(o -> o.getValue().size())).get().getKey());
    }

    public static Node maxNeighboursColoured(Graph graph) {
        return graph.getNode(graph.getEdges().entrySet().stream().max(Comparator.comparingInt(o -> (int) o.getValue().stream().filter(e -> e.getTo().getValue() != -1).count())).get().getKey());
    }

    public static List<List<Node>> cliqueDetector(Graph graph) {
        List<List<Node>> cliques = new LinkedList<>();
        bronKerbosch(graph, cliques, new ArrayList<>(), new ArrayList<>(graph.getNodes().values()), new ArrayList<>());
        return cliques;
    }

    private static List<List<Node>> bronKerbosch(Graph graph, List<List<Node>> cliques, List<Node> _R, List<Node> _P, List<Node> _X) {
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

            bronKerbosch(graph, cliques, dR, dP, dX);

            //---
            nodeIterator.remove();
            _X.add(node);
        }

        return cliques;
    }

}
