import GitHubFiles.Graph;
import GitHubFiles.GraphDrawer;


public class TestRandomGraph {
    public static void main(String[] args) {
        RandomGraph graph=new RandomGraph();
        graph.setPMedium();
        System.out.println(graph.getProbability());
        graph.setLIMIT(50);
        graph.setBoth(true);
        graph.setBoth(40,100);
        System.out.println("Probs: "+graph.getProbability());
        Graph newGraph=graph.getGraph();
        //GraphDrawer drawer=new GraphDrawer();
        //sdrawer.shell(newGraph, 400, 400);
        int edges=0;
        for(int i=0; i<newGraph.getNodes().size();i++){
            edges+=newGraph.getEdges(i).size();
        }
        //System.out.println("Number vertices: "+newGraph.getNodes().size()+" number edges: "+newGraph.getEdges().size());
        System.out.println(edges);
    }
}

