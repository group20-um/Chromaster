package edu.um.chromaster;

import edu.um.chromaster.event.EventHandler;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.RandomGraph;
import edu.um.chromaster.gui.GameElement;
import edu.um.chromaster.modes.FirstGameMode;
import edu.um.chromaster.modes.GameMode;
import edu.um.chromaster.modes.SecondGameMode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;

import java.util.Random;

public class Game extends Application {

    public final static Random random = new Random(1); //TODO same seed to ease debugging efforts

    private final static EventHandler eventHandler = new EventHandler();
    private static Game instance;

    private GameMode gameMode = null;

    public static void main(String[] args) {
        launch(args);
    }

    public static Game getInstance() {
        return instance;
    }

    public static EventHandler getEventHandler() {
        return eventHandler;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    @Override
    public void start(Stage primaryStage) {
        instance = this;

        //---

        RandomGraph g = new RandomGraph();
        g.setLIMIT(10);
        g.setPHard();
        g.setNada(true);

        System.out.println(g.getProbability());
        Graph graph = g.getGraph();
        this.gameMode = new SecondGameMode(graph, 120);

        GameElement graphGameElement = new GameElement(primaryStage, graph, gameMode);
        Scene scene = new Scene(graphGameElement, 1280, 720, true, SceneAntialiasing.BALANCED);
        scene.getStylesheets().add("res/style.css");

        // TODO sample mouse-click to node
        primaryStage.setTitle("Chromaster");
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> graphGameElement.changeWindowSize(newValue.doubleValue(), primaryStage.getHeight()));
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> graphGameElement.changeWindowSize(primaryStage.getWidth(), newValue.doubleValue()));
        primaryStage.setScene(scene);

        primaryStage.show();

        //--- stylesheet

    }


}
