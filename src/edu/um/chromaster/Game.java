package edu.um.chromaster;

import edu.um.chromaster.event.EventHandler;
import edu.um.chromaster.gui.GameElement;
import edu.um.chromaster.gui.Intro;
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
    private GameElement gameElement = null;
    private Stage stage;

    public static Game getInstance() {
        return instance;
    }

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public GameElement getGameElement() {
        return this.gameElement;
    }

    public ScheduledThreadPoolExecutor getSchedule() {
        return this.schedule;
    }

    @Override
    public void start(Stage primaryStage) {
        instance = this;
        this.stage = primaryStage;


        Intro intro = new Intro();
        intro.getStylesheets().add("res/style.css");
        primaryStage.setMinWidth(680);
        primaryStage.setMinHeight(480);
        // TODO sample mouse-click to node
        primaryStage.setTitle("Chromaster");
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(gameElement != null) {
                gameElement.changeWindowSize(Math.max(newValue.doubleValue(), 680), primaryStage.getHeight());
            }
        });
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            if(gameElement != null) {
                gameElement.changeWindowSize(primaryStage.getWidth(), Math.max(newValue.doubleValue(), 480));
            }
        });

        primaryStage.setResizable(false);
        primaryStage.setScene(intro.getAssociatedScene());
        primaryStage.show();

    }

    public void setScene(Scene scene) {
        this.stage.setScene(scene);
    }

    public void setGameElement(GameElement gameElement) {
        this.gameElement = gameElement;
    }

    public Stage getStage() {
        return this.stage;
    }

}
