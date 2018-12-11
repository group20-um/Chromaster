package edu.um.chromaster.gui.menu;

import edu.um.chromaster.Game;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.ReadGraph;
import edu.um.chromaster.gui.MenuScene;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;


public class ReadGraphScene {

    private static String path;
    private static Graph graph;

    public static Scene createReadGraphScene(Stage window){

        Label label=new Label("Enter path of your file here!");

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(window);

        if(!file.isFile()) {
            label.setText("The selected entity is not a file.");
        }

        Button enter=new Button("Let's go!");

        enter.setOnAction(event -> {

            path = file.getAbsolutePath();
            graph= ReadGraph.read(path);
            ChosenVerticesOrEdges.random = false;
            ChosenVerticesOrEdges.vertices = false;
            ChosenVerticesOrEdges.verticesAndEdges = false;
            ChosenVerticesOrEdges.edges = false;
            ChosenVerticesOrEdges.readIn = true;
            System.out.println("The player is playing with a Graph read in from a File");
            window.setScene(PlayScene.createPlayScene(window));

        });

        return new Scene(MenuScene.createParent(Arrays.asList(label, enter), (a) -> {
            window.setScene(MainScene.createMainScene(Game.getInstance().getStage()));
        }), 1280, 720);

    }

    public static Graph getGraph() {
        return graph;
    }
}
