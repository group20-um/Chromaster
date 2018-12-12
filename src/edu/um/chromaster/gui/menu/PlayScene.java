package edu.um.chromaster.gui.menu;


import edu.um.chromaster.RandomGraph;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.gui.game.GameElement;
import edu.um.chromaster.gui.game.GraphElement;
import edu.um.chromaster.modes.FirstGameMode;
import edu.um.chromaster.modes.GameMode;
import edu.um.chromaster.modes.SecondGameMode;
import edu.um.chromaster.modes.ThirdGameMode;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;


public class PlayScene {

	public static Scene createPlayScene(Stage window) {

		RandomGraph aGraph = new RandomGraph();

		if (DifficultyScene.getSelectedeDifficulty() == GraphElement.Difficulty.EASY) aGraph.setPEasy();
		else if (DifficultyScene.getSelectedeDifficulty() == GraphElement.Difficulty.MEDIUM) aGraph.setPMedium();
		else if (DifficultyScene.getSelectedeDifficulty() == GraphElement.Difficulty.HARD) aGraph.setPHard();
		else System.out.println("Error: Difficulty isn't selected"); //Shouldn't happen



		if (ChosenVerticesOrEdges.vertices) aGraph.setVertices(true);
		else if (ChosenVerticesOrEdges.verticesAndEdges) aGraph.setBoth(true);
		else if (ChosenVerticesOrEdges.edges) aGraph.setEdges(true);
		else if (ChosenVerticesOrEdges.random) aGraph.setNada(true);
		else if(ChosenVerticesOrEdges.readIn) aGraph.setEdges(false);
		else System.out.println("Error: The way the graph is generated isn't selected"); //Shouldn't happen

		aGraph.setLIMIT(30);
		Graph graph;
		if(ChosenVerticesOrEdges.verticesAndEdges||ChosenVerticesOrEdges.vertices||ChosenVerticesOrEdges.edges||ChosenVerticesOrEdges.random){
			graph = aGraph.getGraph();
		}
		else{
			graph=ReadGraphScene.getGraph();
		}
		GameMode gameMode = null;


		// game length
		long duration = 20;
		if(DifficultyScene.getSelectedeDifficulty() == GraphElement.Difficulty.MEDIUM) {
			duration = 12;
		} else if(DifficultyScene.getSelectedeDifficulty() == GraphElement.Difficulty.HARD) {
			duration = 6;
		}
		duration *= graph.getNodes().size();
		System.out.printf("Game Length %ds%n", duration);

		if(ChosenGameMode.chooseGameMode1) {
			gameMode = new FirstGameMode(graph, DifficultyScene.getSelectedeDifficulty(), TimeUnit.SECONDS.toMillis(duration));
		} else if (ChosenGameMode.chooseGameMode2) {
			gameMode = new SecondGameMode(graph, DifficultyScene.getSelectedeDifficulty(), TimeUnit.SECONDS.toMillis(duration));
		} else if (ChosenGameMode.chooseGameMode3) {
			gameMode = new ThirdGameMode(graph, DifficultyScene.getSelectedeDifficulty(), TimeUnit.SECONDS.toMillis(duration));
		}

		GameElement graphGameElement = new GameElement(window, graph, gameMode);
		return new Scene(graphGameElement, 1280, 720, true, SceneAntialiasing.BALANCED);


	}

}
