

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import GitHubFiles.FirstGameMode;
import GitHubFiles.Game;
import GitHubFiles.GameMode;
import GitHubFiles.Graph;
import GitHubFiles.GraphDrawer;
import GitHubFiles.GraphElement;
import GitHubFiles.GraphGameElement;
import GitHubFiles.Node;
import GitHubFiles.SecondGameMode;
import GitHubFiles.ThirdGameMode;
import GitHubFiles.GraphElement.BackgroundType;
import GitHubFiles.GraphElement.RenderType;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PlayScene {
	
	Button back;
	Label difficulty;
	Label gameMode;
	

	public static Scene createPlayScene(Stage window) {
    	
    	BorderPane borderPane = new BorderPane();
    	
    	HBox topBar = new HBox();
		Button back = new Button("Go back");
		back.setOnAction(e -> window.setScene(DifficultyScene.createDifficultyScene(window)));
		Button rules = new Button("Rules");
		rules.setOnAction(e -> RulesBox.display());
		topBar.getChildren().addAll(back, rules);
		topBar.setPadding(new Insets(2,2,2,2));
		borderPane.setTop(topBar);
		
		
        RandomGraph aGraph = new RandomGraph();
        
        if (ChosenDifficulty.easy) aGraph.setPEasy();
        else if (ChosenDifficulty.medium) aGraph.setPMedium();
        else if (ChosenDifficulty.hard) aGraph.setPHard();
        else System.out.println("Error: Difficulty isn't selected"); //Shouldn't happen
		
        	
        	
        if (ChosenVerticesOrEdges.vertices) aGraph.setVertices(true);
		else if (ChosenVerticesOrEdges.verticesAndEdges) aGraph.setBoth(true);
		else if (ChosenVerticesOrEdges.edges) aGraph.setEdges(true);
		else if (ChosenVerticesOrEdges.random) aGraph.setNada(true);
		else System.out.println("Error: The way the graph is generated isn't selected"); //Shouldn't happen
        
        aGraph.setLIMIT(30);
        Graph graph = aGraph.getGraph();
        GameMode gameMode = new FirstGameMode(graph);
        
        
        if(ChosenGameMode.chooseGameMode1) {
        	gameMode = new FirstGameMode(graph);
        } else if (ChosenGameMode.chooseGameMode2) {
        	gameMode = new SecondGameMode(graph, 300);
        } else if (ChosenGameMode.chooseGameMode3) {
        	gameMode = new ThirdGameMode(graph);
        }
        
        
        GraphGameElement graphGameElement = new GraphGameElement(window, graph, gameMode);
        Scene scene = new Scene(graphGameElement, -1, -1, true, SceneAntialiasing.BALANCED);
       
        
        return scene;
        
        
    }	

}
