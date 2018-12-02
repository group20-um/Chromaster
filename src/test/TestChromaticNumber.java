package test;

import edu.um.chromaster.ChromaticNumber;
import edu.um.chromaster.graph.Graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class TestChromaticNumber {

    public static void main(String[] args) throws IOException {
        new TestChromaticNumber();
    }

    private TestChromaticNumber() throws IOException {

        File dir = new File("src/test/graphs");
        for(File f : dir.listFiles()) {

            if(!f.getName().endsWith("06.txt")) {
                continue;
            }

            Graph graph = new Graph();
            List<String> lines = Files.readAllLines(Paths.get(f.getAbsolutePath()));
            AtomicInteger exact = new AtomicInteger(-1);
            lines.forEach(l -> {
                if(l.startsWith("VERTICES") || l.startsWith("EDGES")) {
                    return;
                }

                if(l.startsWith("EXACT")) {
                    exact.set(Integer.valueOf(l.split(" = ")[1]));
                    return;
                }

                String[] e = l.split(" ");
                int a = Integer.valueOf(e[0]);
                int b = Integer.valueOf(e[1]);

                if(graph.getNode(a) == null) {
                    graph.addNode(a, -1);
                }
                if(graph.getNode(b) == null) {
                    graph.addNode(b, -1);
                }

                graph.addEdge(a, b, true);

            });

            submit(f.getName(), graph, exact.get());
        }

    }

    public void submit(String name, Graph graph, int exact) {
        //ChromaticNumber.Result result = ChromaticNumber.compute(ChromaticNumber.Type.EXACT, graph, true);
        //System.out.println(String.format("%s> Got %d, expected %d", name, result.getExact(), exact));

        ChromaticNumber.computeAsync(ChromaticNumber.Type.EXACT, graph, new Consumer<ChromaticNumber.Result>() {
            @Override
            public void accept(ChromaticNumber.Result result) {
                System.out.println(String.format("%s: %d> Got %d, expected %d", name, graph.hashCode(), result.getExact(), exact));
            }
        });
    }

}
