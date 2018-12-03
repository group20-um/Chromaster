package edu.um.chromaster;

import edu.um.chromaster.event.EventHandler;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.RandomGraph;
import edu.um.chromaster.gui.GameElement;
import edu.um.chromaster.modes.FirstGameMode;
import edu.um.chromaster.modes.GameMode;
import edu.um.chromaster.modes.SecondGameMode;
import edu.um.chromaster.modes.ThirdGameMode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Game extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    //----
    public final static Random random = new Random(1); //TODO same seed to ease debugging efforts
    private static Game instance;

    //---
    private ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(2);
    private final EventHandler eventHandler = new EventHandler();
    private GameMode gameMode = null;

    public static Game getInstance() {
        return instance;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public ScheduledThreadPoolExecutor getSchedule() {
        return this.schedule;
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
        this.gameMode = new ThirdGameMode(graph);

        GameElement graphGameElement = new GameElement(primaryStage, graph, gameMode);
        Scene scene = new Scene(graphGameElement, 1280, 720, true, SceneAntialiasing.BALANCED);
        scene.getStylesheets().add("res/style.css");
        graphGameElement.getStyleClass().add("game");

        primaryStage.setMinWidth(680);
        primaryStage.setMinHeight(480);
        // TODO sample mouse-click to node
        primaryStage.setTitle("Chromaster");
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            graphGameElement.changeWindowSize(Math.max(newValue.doubleValue(), 680), primaryStage.getHeight());
        });
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            graphGameElement.changeWindowSize(primaryStage.getWidth(), Math.max(newValue.doubleValue(), 480));
        });
        primaryStage.setScene(scene);

        primaryStage.show();

        //--- stylesheet

    }


}
