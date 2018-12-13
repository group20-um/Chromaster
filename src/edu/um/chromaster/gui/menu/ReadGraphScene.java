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

    private static Graph graph;

    /**
     * Creates the scene in which the user enters a file
     * @param window
     * @return the created scene
     */
    public static Scene createReadGraphScene(Stage window){

        Label label=new Label("Enter path of your file here!");

        while(true) {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(window);

            while (file == null || !file.isFile()) {
                label.setText("The selected entity is not a file.");
                file = fileChooser.showOpenDialog(window);
            }


            ReadGraph.Result r = ReadGraph.read(file.getAbsolutePath());
            label.setText(r.getMessage());

            if(!(r.isError())) {
                graph = r.getGraph();
                break;
            }
        }

        Button enter=new Button("Let's go!");
        enter.setOnAction(event -> {
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
