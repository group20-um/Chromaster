package edu.um.chromaster;

import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.RandomGraph;
import edu.um.chromaster.gui.GraphElement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TestRandomGraph extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        RandomGraph graph = new RandomGraph();
        graph.setPMedium();
        System.out.println(graph.getProbability());
        graph.setNada(true);
        Graph newGraph = graph.getGraph();
        GraphElement g = new GraphElement(newGraph, GraphElement.RenderType.SPIRAL);
        g.render();

        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane);
        stackPane.getChildren().add(g);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
