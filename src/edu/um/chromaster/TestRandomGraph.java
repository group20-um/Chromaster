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
        graph.setNada(true);
        System.out.println(graph.getProbability());


        Graph newGraph = graph.getGraph();
        GraphElement g = new GraphElement(newGraph, GraphElement.RenderType.SHELL, GraphElement.BackgroundType.COLOUR);
        g.render();

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(g);

        Scene scene = new Scene(stackPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
