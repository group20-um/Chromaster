package edu.um.chromaster.graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class ColEdge
{
    int u;
    int v;
}


/**
 * Class that reads in a graph from a file
 * Based on the class Steven gave us for block 1.1
 */
public class ReadGraph
{

    public final static boolean DEBUG = true;

    public final static String COMMENT = "//";

    public static Result read( String path )
    {
        if( path.length() < 1 )
        {
            return new Result(null, "Error! No filename specified.", true);
        }

        String inputfile = path;
        //File inputfile=new File(path);

        boolean seen[] = null;

        //! n is the number of vertices in the graph
        int n = -1;

        //! m is the number of edges in the graph
        int m = -1;

        //! e will contain the edges of the graph
        ColEdge e[] = null;

        try 	{
            FileReader fr = new FileReader(inputfile);
            BufferedReader br = new BufferedReader(fr);

            String record = new String();

            //! THe first few lines of the file are allowed to be comments, staring with a // symbol.
            //! These comments are only allowed at the top of the file.

            //! -----------------------------------------
            while ((record = br.readLine()) != null)
            {
                if( record.startsWith("//") ) continue;
                break; // Saw a line that did not start with a comment -- time to start reading the data in!
            }

            if( record.startsWith("VERTICES = ") )
            {
                n = Integer.parseInt( record.substring(11) );
                if(DEBUG) System.out.println(COMMENT + " Number of vertices = "+n);
            }

            seen = new boolean[n+1];

            record = br.readLine();

            if( record.startsWith("EDGES = ") )
            {
                m = Integer.parseInt( record.substring(8) );
                if(DEBUG) System.out.println(COMMENT + " Expected number of edges = "+m);
            }

            e = new ColEdge[m];

            for( int d=0; d<m; d++)
            {
                if(DEBUG) System.out.println(COMMENT + " Reading edge "+(d+1));
                record = br.readLine();
                String data[] = record.split(" ");
                if( data.length != 2 )
                {
                    System.out.println("Error! Malformed edge line: "+record);
                    System.exit(0);
                }
                e[d] = new ColEdge();

                e[d].u = Integer.parseInt(data[0]);
                e[d].v = Integer.parseInt(data[1]);

                seen[ e[d].u ] = true;
                seen[ e[d].v ] = true;

                if(DEBUG) System.out.println(COMMENT + " Edge: "+ e[d].u +" "+e[d].v);

            }

            String surplus = br.readLine();
            if( surplus != null )
            {
                if( surplus.length() >= 2 ) if(DEBUG) System.out.println(COMMENT + " Warning: there appeared to be data in your file after the last edge: '"+surplus+"'");
            }

        }
        catch (IOException ex)
        {
            // catch possible io errors from readLine()
            return new Result(null, "Error! Problem reading file "+inputfile, true);
        }

        for( int x=1; x<=n; x++ )
        {
            if( seen[x] == false )
            {
                if(DEBUG) System.out.println(COMMENT + " Warning: vertex "+x+" didn't appear in any edge : it will be considered a disconnected vertex on its own.");
            }
        }

        //! At this point e[0] will be the first edge, with e[0].u referring to one endpoint and e[0].v to the other
        //! e[1] will be the second edge...
        //! (and so on)
        //! e[m-1] will be the last edge
        //!
        //! there will be n vertices in the graph, numbered 1 to n

        //! INSERT YOUR CODE HERE!

        Graph graph=new Graph(); //create graph object of our Graph class

        for(int i=0; i<n; i++){ //add all vertices to graph
            graph.addNode(i, -1);
        }

        for(int i=0; i<m;i++){ //add edge to graph
            graph.addEdge(e[i].u-1, e[i].v-1, true);
        }

        return new Result(graph, null, false);  //returns graph

    }

    public static class Result {
        private Graph graph;
        private String message;
        private boolean error;

        public Result(Graph graph, String message, boolean error) {
            this.graph = graph;
            this.message = message;
            this.error = error;
        }

        public Graph getGraph() {
            return graph;
        }

        public boolean isError() {
            return error;
        }

        public String getMessage() {
            return message;
        }
    }

}
