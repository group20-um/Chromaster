package edu.um.chromaster;


import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        //
        int type = 1;

        Graph graph = new Graph();
        if(type == 0) {

            if(args.length == 0) {
                System.out.println("Debug: No file path provided!");
                return;
            }

            String fileName = args[0];
            System.out.println("########### READ FROM FILE ###########");
            try {
                List<String> lines = Files.readAllLines(Paths.get(fileName));
                Set<Integer> nodes = new HashSet<>();
                Set<int[]> edges = new HashSet<>();

                int lineNumber = 1;
                for (final String line : lines) {
                    if (!line.startsWith("VERTICES") && !line.startsWith("EDGES") && !line.startsWith("//")) {
                        String[] split = line.split(" ");

                        //--- Error
                        if (split.length != 2) {
                            System.out.println(String.format("Debug %s:%d >> %s", fileName, lineNumber, String.format("Malformed edge line: %s", line)));
                        }

                        int from = Integer.parseInt(split[0]);
                        int to = Integer.parseInt(split[1]);
                        nodes.add(from);
                        nodes.add(to);
                        edges.add(new int[]{from, to});
                    }

                    lineNumber++;
                }

                nodes.forEach(id -> graph.addNode(id, -1));
                edges.forEach(edge -> graph.addEdge(edge[0], edge[1], true));

                System.out.printf("Debug: Graph (%s) parsed with %d vertices and %d edges.%n", fileName, nodes.size(), edges.size());

            } catch (IOException e) {
                System.out.println(String.format("Debug %s:-1 >> %s", fileName, String.format("The file could not (!) be read. (%s)", e.getMessage())));
                System.exit(0);
                e.printStackTrace();
            }

        } else if(type == 1) {

            final int nodes = 100;
            IntStream.range(0, nodes).forEach(i -> graph.addNode(i, -1));
            Random random = new Random();

            for(int from = 0; from < nodes; from++) {
                for(int to = 0; to < nodes; to++) {
                    if (from != to && random.nextDouble() < .8) {
                        graph.addEdge(from, to, true);
                    }
                }
            }

            //--- Create file
            StringBuilder builder = new StringBuilder();
            graph.getEdges().forEach((k, v) -> v.forEach(edge -> builder.append(String.format("%s %s%n", k, edge.getTo().getId()))));


            /*try {
                File file = new File("data/" + System.currentTimeMillis() + ".txt");
                if(file.createNewFile()) {
                    FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath());
                    fileOutputStream.write(builder.toString().getBytes(Charset.forName("UTF-8")));
                    fileOutputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/

        }
        long now = System.currentTimeMillis();
        //System.out.println(ChromaticNumber.compute(ChromaticNumber.Type.UPPER, graph, false));
        //System.out.println(ChromaticNumber.compute(ChromaticNumber.Type.LOWER, graph, true));
        //ChromaticNumber.compute(ChromaticNumber.Type.EXACT, graph, false);
        System.out.printf("Time to execute: %dms%n", (System.currentTimeMillis() - now));


    }

    private static void generateGephiFile(Graph graph) {
        //--- Gephi

        if(false) {
            try {
                List<Color> colors = new LinkedList<Color>() {{
                    this.add(new Color(0xBA8B02));
                    this.add(new Color(0x3d72b4));
                    this.add(new Color(0x135058));
                    this.add(new Color(0xdc2430));
                    this.add(new Color(0x7b4397));
                    this.add(new Color(0xFFA17F));
                    this.add(new Color(0xFF6FCF));
                    this.add(new Color(0xFFFF60));
                    this.add(new Color(0x00FF80));
                    this.add(new Color(0xFFFFFF));
                    this.add(new Color(0x153B50));
                    this.add(new Color(0x2a0845));
                }};

                StringBuilder builder = new StringBuilder();
                builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><gexf xmlns=\"http://www.gexf.net/1.2draft\" " +
                        "xmlns:viz=\"http://www.gexf.net/1.1draft/viz\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                        "xsi:schemaLocation=\"http://www.gexf.net/1.2draft http://www.gexf.net/1.2draft/gexf.xsd\" version=\"1.2\">" +
                        "<graph><nodes>");
                ChromaticNumber.graph.getNodes().forEach((k, v) -> {
                    if (colors.size() <= v.getValue()) {
                        return;
                    }
                    Color color = colors.get(v.getValue());
                    builder.append(String.format("<node id=\"%d\" label=\"glossy\"><viz:color r=\"%d\" g=\"%d\" b=\"%d\" /><viz:group value=\"%d\"/></node>",
                            v.getId(),
                            color.getRed(),
                            color.getGreen(),
                            color.getBlue(),
                            color.getRGB()
                    ));
                });
                builder.append("</nodes><edges>");

                int edgeId = 0;
                for (Map.Entry<Integer, List<Node.Edge>> entry : ChromaticNumber.graph.getEdges().entrySet()) {
                    for (Node.Edge edge : entry.getValue()) {
                        builder.append(String.format("<edge id=\"%d\" source=\"%d\" target=\"%d\" />", edgeId, edge.getFrom().getId(), edge.getTo().getId()));
                        edgeId++;
                    }
                }

                builder.append("</edges></graph></gexf>");
                File file = new File("data/gephi.gexf");
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(builder.toString().getBytes(Charset.forName("UTF-8")));
                fileOutputStream.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
