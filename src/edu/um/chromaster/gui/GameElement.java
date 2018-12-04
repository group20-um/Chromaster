package edu.um.chromaster.gui;

import edu.um.chromaster.ChromaticNumber;
import edu.um.chromaster.Game;
import edu.um.chromaster.event.EventListener;
import edu.um.chromaster.event.Subscribe;
import edu.um.chromaster.event.events.GameEndEvent;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.modes.GameMode;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class GameElement extends StackPane implements EventListener {

    private BackgroundElement backgroundElement;
    private GraphElement graphElement;
    private ColourSelectorElement colourSelectorElement;

    private GameBar gameBar;
    private GameEndScreen gameEndScreen;


    public GameElement(Stage stage, Graph graph, GameMode gameMode) {
        Game.getInstance().getEventHandler().registerListener(this);
        ChromaticNumber.computeAsync(ChromaticNumber.Type.EXACT, graph.clone(), result -> {
            graph.setChromaticResults(result);
            System.out.println("WOOOT");
            gameBar.testIt(GraphElement.HintTypes.CLIQUE);
            System.out.println("WOOOT#2");
        });

        Game.getInstance().getSchedule().schedule(() -> {
            graphElement.requestLayout();
            this.requestLayout();
        }, (1000 / 60), TimeUnit.MILLISECONDS);


        this.graphElement = new GraphElement(this, graph, GraphElement.RenderType.CIRCLE);
        this.graphElement.backgroundProperty().set(Background.EMPTY);
        this.gameEndScreen = new GameEndScreen();

        this.gameBar = new GameBar(this.graphElement, gameMode.getTime());
        this.colourSelectorElement = new ColourSelectorElement(stage);

        changeWindowSize(1280, 720);

        // distribute graph nodes randomly
        graph.getNodes().forEach((id, node) -> {
            node.getMeta().x((Game.random.nextDouble() * graphElement.getWidth()) - graphElement.getWidth() / 2 + colourSelectorElement.getHeight());
            node.getMeta().y((Game.random.nextDouble() * graphElement.getHeight()) - graphElement.getHeight() / 2);
        });


        graphElement.applyLayout();
        this.graphElement.render();

        //--- have to stay together
        gameMode.start();
        this.backgroundElement = new BackgroundElement(gameMode);

        //---
        this.getChildren().add(this.backgroundElement);
        this.getChildren().add(this.graphElement);
        this.getChildren().add(this.colourSelectorElement);
        this.getChildren().add(this.gameBar);

        StackPane.setAlignment(colourSelectorElement, Pos.TOP_CENTER);
        StackPane.setAlignment(gameBar, Pos.BOTTOM_CENTER);

    }


    public ColourSelectorElement getColourSelectorElement() {
        return this.colourSelectorElement;
    }

    public void changeWindowSize(double width, double height) {
        this.setMinSize(width, height);
        this.setMaxSize(width, height);

        this.graphElement.setMinSize(width, 0.85D * height);
        this.graphElement.setMaxSize(width, 0.85D * height);
        this.graphElement.applyLayout();

        this.colourSelectorElement.setMinSize(width, height * 0.1D);
        this.colourSelectorElement.setMaxSize(width, height * 0.1D);

        this.gameBar.setMinSize(width, height * 0.05D);
        this.gameBar.setMaxSize(width, height * 0.05D);


        if (backgroundElement != null) {
            this.backgroundElement.setMinSize(width, height);
            this.backgroundElement.setMaxSize(width, height);
        }
    }

    @Subscribe
    public void onGameEnd(GameEndEvent event) {
        this.gameEndScreen.execute(event);
        this.gameEndScreen.setVisible(true);
        Platform.runLater(() -> this.getChildren().add(this.gameEndScreen));
    }

}
