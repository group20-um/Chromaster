package edu.um.chromaster;

import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.gui.menu.VerticesEdgesInputScenes;

/**
 * Class that generates the graph for the game
 * Generates either a completely random graph, or with a specified amount of vertices or edges or both
 */
public class RandomGraph {

    private double probability; // probability of creating an edge between two vertices, do not forget to initialize!
    private int LIMIT; //limit of vertices
    private final double PROBEASY=0.1; // maximal probability for game difficulty "easy"
    private final double PROBMEDIUM=0.2; // maximal probability for game difficulty "medium"
    private final double PROBHARD=0.3; // maximal probability for game difficulty "hard"

    private int numberVertices = VerticesEdgesInputScenes.numberOfVertices; //number of vertices (input by player)
    private int numberEdges = VerticesEdgesInputScenes.numberOfEdges; //number of edges (input by player)
    private Graph random; //random Graph that will be returned for playing

    //boolean variables that indicate which features player has specified, will be set to true if user has set them
    private boolean nada=false;
    private boolean vertices=false;
    private boolean edges=false;
    private boolean both=false;

    /**
     * Method that creates the graph for the game based on player input
     * @return random, generated Graph that will be returned for playing
     */
    public Graph getGraph(){
        if(nada) { //check if user wants completely random graph
            System.out.println("Random graph");
            random = setNada(); //set graph
        }
        else if(vertices) { //check if user has set number of vertices
            System.out.println("Player chose number of vertices");
            random = setVertices(numberVertices); //set graph
        }
        else if(edges) { //check if user has set number of edges
            System.out.println("Player chose number of edges");
            random = setEdges(numberEdges); //set graph
        }
        else if(both) { //check if user has set number of vertices and edges
            System.out.println("Player specified number of vertices and edges");
            random = setBoth(numberVertices, numberEdges); //set graph
        }
        return random;
    }

    /**
     * Method that generates a completely random graph
     * @return g, random graph
     */
    public Graph setNada(){

        Graph g=new Graph(); //graph that will be returned

        numberVertices = (int) (Game.random.nextDouble() * (LIMIT - 2))+2; //generate the number of vertices: between 2 vertices and LIMIT

        for(int cnt=0; cnt<numberVertices;cnt++)  //add vertices to graph
            g.addNode(cnt, -1);

        int cntEdges=0; //counter for edges

        for(int a=0; a<numberVertices; a++){ //two for-loops to run through every possible combination of vertices
            for(int c=0; c<numberVertices;c++){
                double x = Game.random.nextDouble(); //create a random double
                if(x<probability || g.getEdges(a).size()==0) { //access if-statement if the generated number is smaller than the probability or if vertex has 0 neighbours (to make sure graph is connected)
                    if(!edgeExists(a,c,g) && a!=c) { //create edge if it does not exist yet and if its not an edge from the vertex to itself
                        g.addEdge(a, c, true);
                        cntEdges++; //increase edge counter
                    }
                }
            }
        }
        numberEdges=cntEdges; //set numberEdges to counted edges
        System.out.println("Vertices: "+numberVertices+" Edges: "+numberEdges);
        return g; //return graph
    }

    /**
     * Method that generates a graph with a given amount of vertices
     * @param vertices number of vertices set by user
     * @return g, graph for game
     */
    public Graph setVertices(int vertices){

        Graph g=new Graph();//graph that will be returned

        numberVertices=vertices; //set numberVertices to given number of vertices

        for(int cnt=0; cnt<numberVertices;cnt++) //add vertices to graph
            g.addNode(cnt, -1);

        int cntEdges=0;//counter for edges

        for(int a=0; a<numberVertices; a++){ //two for-loops to run through every possible combination of vertices
            for(int b=0; b<numberVertices;b++){
                double x = Game.random.nextDouble(); //create a random double
                if(x<probability||g.getEdges(a).size()==0){ //access if-statement if the generated number is smaller than the probability or if vertex has 0 neighbours (to make sure graph is connected)
                    if(!edgeExists(a,b,g)&&a!=b) { //create edge if it does not exist yet and if its not an edge from the vertex to itself
                        g.addEdge(a, b, true);
                        cntEdges++; //increase edge counter
                    }
                }
            }
        }
        numberEdges=cntEdges; //set numberEdges to number of counted edges
        System.out.println("Vertices: "+numberVertices+" Edges: "+numberEdges);
        return g; //return graph
    }

    /**
     * Method that generates a graph with a given amount of edges
     * @param edges number of edges set by user
     * @return g, graph for game
     */
    public Graph setEdges(int edges){

        Graph g=new Graph();//graph that will be returned

        numberEdges=edges;//set numberEdges to given number of edges

        int min=(int) (Math.ceil(0.5+Math.sqrt( 0.25+2*numberEdges ))); //compute minimum number of vertices required

        int max= numberEdges+1; //create maximum number of vertices possible

        int vertices;
        vertices=(int)(Game.random.nextDouble()*(max-min+1)+min); //generate number of vertices between min and max
        numberVertices=vertices; //set numberVertices to generated number of vertices

        for(int cnt=0; cnt<vertices;cnt++) //add vertices to graph
            g.addNode(cnt, -1);

        int cntEdges=0; //edge counter
        for(int a=0; a<vertices; a++) { //run through all vertices
            if (g.getEdges(a).size() == 0) { //create edge if the vertex is not connected to graph yet (to make sure graph is connected)
                int b = (int) (Game.random.nextDouble() * numberVertices); //generate a random vertex that the current vertex will be connected to
                if (a!=b) { //create edge if it does not connect to itself
                    g.addEdge(a, b, true);
                    cntEdges++; //increase edge counter
                }
            }
        }
        while(cntEdges<numberEdges){ //create edges until the given number of edges is reaches
            for(int a=0; a<vertices; a++){ //run through all possible combinations of vertices
                for(int b=0; b<vertices;b++){
                    if(Math.random()<probability){ //stochastically create edges
                        if(!edgeExists(a,b,g)&&a!=b) { //create edge if it does not exist yet and if its not an edge from the vertex to itself
                            g.addEdge(a, b, true);
                            cntEdges++; //increase edge counter
                        }
                    }
                }
            }
        }
        System.out.println("Vertices: "+numberVertices+" Edges: "+numberEdges);
        return g; //return graph
    }

    /**
     * Method that generates a graph given a number of vertices and edges
     * @param vertices fixed number of vertices
     * @param edges fixed number of edges
     * @return g, graph for game
     */
    public Graph setBoth(int vertices, int edges){

        Graph g =new Graph(); //graph that will be returned

        //set number of vertices and edges
        numberVertices=vertices;
        numberEdges=edges;

        for(int cnt=0; cnt<vertices;cnt++) //add vertices to graph
            g.addNode(cnt, -1);

        int cntEdges=0;//edge counter

        for(int a=0; a<vertices; a++) { //run through all vertices
            if (g.getEdges(a).size() == 0) { //create edge if the vertex is not connected to graph yet (to make sure graph is connected)
                int b = (int) (Game.random.nextDouble() * numberVertices); //generate a random vertex that the current vertex will be connected to
                if (a!=b) { //create edge if it does not connect to itself
                    g.addEdge(a, b, true);
                    cntEdges++; //increase edge counter
                }
            }
        }
        while(cntEdges<numberEdges){ //create edges until the given number of edges is reaches
            for(int a=0; a<vertices; a++){ //run through all possible combinations of vertices
                for(int b=0; b<vertices;b++){
                    if(Math.random()<probability){ //stochastically create edges
                        if(!edgeExists(a,b,g)&&a!=b) { //create edge if it does not exist yet and if its not an edge from the vertex to itself
                            g.addEdge(a, b, true);
                            cntEdges++; //increase edge counter
                        }
                    }
                }
            }
        }
        System.out.println("Vertices: "+numberVertices+" Edges: "+numberEdges);
        return g;//return graph
    }

    /**
     * Method that checks if an edge exists
     * @param a vertex, one end of edge
     * @param b vertex, other end of edge
     * @param g graph that will be checked for edge
     * @return false if edge does not exist, else return true
     */
    public boolean edgeExists(int a, int b, Graph g){
        if(g.getEdge(a,b)==null){
            return false;
        }
        return true;
    }
    /**
     * Method that generates random probability for chosen difficult "Easy"
     */
    public void setPEasy(){
        probability=Game.random.nextDouble()*(PROBEASY-0.01)+0.01; //generates probability between 0.01 and PROBEASY
    }
    /**
     * Method that generates random probability for chosen difficult "Medium"
     */
    public void setPMedium(){
        probability=Game.random.nextDouble()*(PROBMEDIUM-PROBEASY)+PROBEASY; //generates probability between PROBEASY and PROBMEDIUM
    }
    /**
     * Method that generates random probability for chosen difficult "Hard"
     */
    public void setPHard(){
        probability=Game.random.nextDouble()*(-PROBMEDIUM+PROBHARD)+PROBMEDIUM;//generates probability between PROBMEDIUM and PROBHARD
    }

    /**
     * method to set maxium number of vertices
     * @param limit
     */
    public void setLIMIT(int limit){
        LIMIT=limit;
    }



    //Methods to set boolean variables to true depending on users choice
    public void setBoth(boolean both) {
        this.both = both;
    }
    public void setEdges(boolean edges) {
        this.edges = edges;
    }
    public void setNada(boolean nada) {
        this.nada = nada;
    }
    public void setVertices(boolean vertices) {
        this.vertices = vertices;
    }

}