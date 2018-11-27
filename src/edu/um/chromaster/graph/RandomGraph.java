package edu.um.chromaster.graph;


public class RandomGraph {

    private double probability; //do not forget to initialize!
    private int LIMIT; //limit vertices
    private final double PROBEASY=0.1;
    private final double PROBMEDIUM=0.2;
    private final double PROBHARD=0.3;

    private int numberVertices;
    private int numberEdges;
    private Graph random;

    private boolean nada=false;
    private boolean vertices=false;
    private boolean edges=false;
    private boolean both=false;


    public Graph getGraph(){
        if(nada) {
            System.out.println("nada");
            random = setNada();
        }
        else if(vertices) {
            System.out.println("vertices");
            random = setVertices(numberVertices);
        }
        else if(edges) {
            System.out.println("edges");
            random = setEdges(numberEdges);
        }
        else if(both) {
            System.out.println("both");
            random = setBoth(numberVertices, numberEdges);
        }
        return random;
    }

    public Graph setNada(){
        Graph g=new Graph();
        while(!(numberVertices>1 &&numberVertices<=LIMIT)) {
            numberVertices = (int) (Math.random() * LIMIT);
        }
        for(int cnt=0; cnt<numberVertices;cnt++)
            g.addNode(cnt, -1);
        int cntEdges=0;
        int cnt=0;
        for(int a=0; a<numberVertices; a++){
            for(int c=0; c<numberVertices;c++){
                cnt++;
                double x=Math.random();
                //System.out.println(x +" "+a);
                if(x<probability || g.getEdges(a).size()==0) {
                    //int b=(int) (Math.random()*numberVertices);
                    //System.out.println("Edge from "+a+" to "+b);
                    if(!edgeExists(a,c,g)&&a!=c) {
                        g.addEdge(a, c, true);
                        cntEdges++;
                    }
                }
            }
        }
        numberEdges=cntEdges;
        System.out.println("XVertices "+numberVertices+" edges: "+numberEdges);
        return g;
    }
    /*
    public Graph completlyRandom(){
        Graph g = new Graph();
        while (!(numberVertices > 1 && numberVertices <= LIMIT)){
            numberVertices = (int) (Math.random() * LIMIT);
        }
        for (int cnt = 0; cnt < numberVertices; cnt++) {
            g.addNode(cnt, -1);
        }
        int cntEdges=0;
        int count=0;
        int maxEdges=numberVertices*(numberVertices-1)/2;
        while(count<=maxEdges){
            if(count<numberVertices){
                for(int a=0; a<numberVertices; a++) {
                    count++;
                    double x = Math.random();
                    if (x < probability || g.getEdges(a).size() == 0) {
                        int b = (int) (Math.random() * numberVertices);
                        g.addEdge(a, b, true);
                        cntEdges++;
                    }
                }
            }
            else{
                count++;
                double x = Math.random();
                int a=(int)(Math.random()*numberVertices);
                if (x < probability || g.getEdges(a).size() == 0) {
                    int b = (int) (Math.random() * numberVertices);
                    g.addEdge(a, b, true);
                    cntEdges++;
                }
            }
        }
        numberEdges=cntEdges;
        return g;
    }
    */
    public Graph setVertices(int vertices){
        Graph g=new Graph();
        numberVertices=vertices;
        for(int cnt=0; cnt<numberVertices;cnt++)
            g.addNode(cnt, -1);
        int cntEdges=0;
        for(int a=0; a<numberVertices; a++){
            for(int b=0; b<numberVertices;b++){
                if(Math.random()<probability||g.getEdges(a).size()==0){
                    if(!edgeExists(a,b,g)&&a!=b) {
                        g.addEdge(a, b, true);
                        cntEdges++;
                    }
                }
            }
        }
        numberEdges=cntEdges;
        System.out.println("Vertices "+numberVertices+" edges: "+numberEdges);
        return g;
    }

    public Graph setEdges(int edges){
        Graph g=new Graph();
        numberEdges=edges;
        int min=(int) (Math.ceil(0.5+Math.sqrt( 0.25+2*numberEdges )));
        int max= numberEdges+1;
        int vertices=0;
        while(!(vertices>=min&&vertices<=max)){
            vertices=(int)(Math.random()*max+1);
        }
        numberVertices=vertices;
        for(int cnt=0; cnt<vertices;cnt++)
            g.addNode(cnt, -1);
        int cntEdges=0;
        while(cntEdges<numberEdges){
            for(int a=0; a<vertices; a++){
                for(int b=0; b<vertices;b++){
                    if(Math.random()<probability||g.getEdges(a).size()==0){
                        if(!edgeExists(a,b,g)&&a!=b) {
                            g.addEdge(a, b, true);
                            cntEdges++;
                        }
                    }
                }
            }
        }
        System.out.println("Vertices "+numberVertices+" edges: "+numberEdges);
        return g;
    }

    public Graph setBoth(int vertices, int edges){
        Graph g =new Graph();
        numberVertices=vertices;
        numberEdges=edges;
        for(int cnt=0; cnt<vertices;cnt++)
            g.addNode(cnt, -1);
        int cntEdges=0;
        while(cntEdges<numberEdges){
            for(int a=0; a<vertices; a++){
                for(int b=0; b<vertices;b++){
                    if(Math.random()<probability||g.getEdges(a).size()==0) { //test if edge already exists!!!
                        if (!edgeExists(a, b, g)&&a!=b) {
                            g.addEdge(a, b, true);
                            cntEdges++;
                        }
                    }
                }
            }
        }
        return g;
    }
    public void setPEasy(){
        while(probability==0||probability>PROBEASY){
            probability=Math.random();
        }
    }
    public void setPMedium(){
        while(!(probability>PROBEASY&&probability<PROBMEDIUM)){
            probability=Math.random();
        }
    }
    public void setPHard(){
        while(!(probability>PROBMEDIUM &&probability<PROBHARD)){
            probability=Math.random();
        }
    }
    public void setLIMIT(int limit){
        LIMIT=limit;
    }

    public int getLIMIT() {
        return LIMIT;
    }

    public Graph getRandom() {
        return random;
    }

    public void setNumberEdges(int numberEdges) {
        this.numberEdges = numberEdges;
    }

    public void setNumberVertices(int numberVertices) {
        this.numberVertices = numberVertices;
    }

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

    public double getProbability() {
        return probability;
    }

    public int getNumberEdges() {
        return numberEdges;
    }

    public int getNumberVertices() {
        return numberVertices;
    }

    public boolean edgeExists(int a, int b, Graph g){
        if(g.getEdge(a,b)==null){
            return false;
        }
        return true;
    }
}
