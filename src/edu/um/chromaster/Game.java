package edu.um.chromaster;

import edu.um.chromaster.event.EventHandler;
import edu.um.chromaster.gui.game.GameElement;
import edu.um.chromaster.gui.Intro;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Game extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    //----
    /**
     * Testing Seeds:
     *  - 1544349015604 -> Caused the Graph#clone() bug
     *  - 1544528910389 -> Caused the butterfly bug
     */
    private static long seed = 1544535103722L;
    {
        System.out.printf("Random seed %d%n", seed);
    }
    public final static Random random = new Random(seed); //TODO same seed to ease debugging efforts
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
        stage.getIcons().add(new Image("res/icon.jpg"));
        
        Intro intro = new Intro();
        intro.getStylesheets().add("res/style.css");
        primaryStage.setMinWidth(1280);
        primaryStage.setMinHeight(720);
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

        primaryStage.setOnCloseRequest(e -> {
            schedule.shutdownNow();
            primaryStage.close();
            System.exit(0);
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
