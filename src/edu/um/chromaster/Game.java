package edu.um.chromaster;

import edu.um.chromaster.event.EventHandler;
import edu.um.chromaster.gui.Intro;
import edu.um.chromaster.gui.game.GameElement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * The Game class is the main class and coordinates a lot of the game's features. Providing relevant pieces of information
 * to all parts of the game.
 */
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

    //--- The scheduler is used across the board to run tasks async to make the UX as smooth as possible.
    private ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(4);

    //--- The event handler is responsible for the event-driven parts of the system.
    private final EventHandler eventHandler = new EventHandler();

    //--- The game element holds the current game screen (graph etc...)
    private GameElement gameElement = null;
    private Stage stage;

    private static Media sound=new Media(new File("src/res/Music.mp3").toURI().toString());
    private static MediaPlayer player=new MediaPlayer(sound);

    /**
     * The instance of the Game class (singleton pattern).
     * @return The game instance.
     */
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

    public static void playMusic(){
        player.play();
    }

    public static void stopMusic(){
        player.stop();
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
        primaryStage.setTitle("Chromaster");

        // resize all elements properly if the user changes the size of the window.
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

        // shutdown the JVM and scheduler
        primaryStage.setOnCloseRequest(e -> {
            schedule.shutdownNow();
            primaryStage.close();
            System.exit(0);
        });

        //---
        primaryStage.setResizable(false);
        primaryStage.setScene(intro.getAssociatedScene());
        Intro.playMusic();
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
