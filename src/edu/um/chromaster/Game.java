package edu.um.chromaster;

import edu.um.chromaster.event.EventHandler;
import edu.um.chromaster.gui.stuff.MainScene;
import edu.um.chromaster.modes.GameMode;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Game extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    //----
    public final static Random random = new Random(); //TODO same seed to ease debugging efforts
    private static Game instance;

    //---
    private ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(12);
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


        Scene mainScene = MainScene.createMainScene(primaryStage);
        mainScene.getStylesheets().add("res/style.css");


        primaryStage.setMinWidth(680);
        primaryStage.setMinHeight(480);
        // TODO sample mouse-click to node
        primaryStage.setTitle("Chromaster");
        /*primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            graphGameElement.changeWindowSize(Math.max(newValue.doubleValue(), 680), primaryStage.getHeight());
        });
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            graphGameElement.changeWindowSize(primaryStage.getWidth(), Math.max(newValue.doubleValue(), 480));
        });*/
        primaryStage.setScene(mainScene);

        primaryStage.show();

        //--- stylesheet

    }


    public void createNewGame(GameMode gameMode) {



    }
}
