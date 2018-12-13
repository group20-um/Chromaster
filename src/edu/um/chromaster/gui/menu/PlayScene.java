package edu.um.chromaster.gui.menu;


import edu.um.chromaster.Game;
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

	/**
	 * Creates the scene that makes the graph appear
	 * It takes all the previous user's choices into account
	 * @param window
	 * @return the created scene
	 */
	public static Scene createPlayScene(Stage window) {

		RandomGraph aGraph = new RandomGraph();

		if (DifficultyScene.getSelectedDifficulty() == GraphElement.Difficulty.EASY) aGraph.setPEasy();
		else if (DifficultyScene.getSelectedDifficulty() == GraphElement.Difficulty.MEDIUM) aGraph.setPMedium();
		else if (DifficultyScene.getSelectedDifficulty() == GraphElement.Difficulty.HARD) aGraph.setPHard();
		else System.out.println("Error: Difficulty isn't selected"); //Shouldn't happen



		if (ChosenVerticesOrEdges.vertices) aGraph.setVertices(true);
		else if (ChosenVerticesOrEdges.verticesAndEdges) aGraph.setBoth(true);
		else if (ChosenVerticesOrEdges.edges) aGraph.setEdges(true);
		else if (ChosenVerticesOrEdges.random) aGraph.setNada(true);
		else if(ChosenVerticesOrEdges.readIn) aGraph.setEdges(false);
		else System.out.println("Error: The way the graph is generated isn't selected"); //Shouldn't happen

		aGraph.setLIMIT(30); //Maximum amount of nodes if the graph is random
		Graph graph;
		if(ChosenVerticesOrEdges.verticesAndEdges||ChosenVerticesOrEdges.vertices||ChosenVerticesOrEdges.edges||ChosenVerticesOrEdges.random){
			graph = aGraph.getGraph();
		}
		else{
			graph=ReadGraphScene.getGraph(); //Gets the file imported by the user
		}
		GameMode gameMode = null;


		// game length
		long duration = 20;
		if(DifficultyScene.getSelectedDifficulty() == GraphElement.Difficulty.MEDIUM) {
			duration = 12;
		} else if(DifficultyScene.getSelectedDifficulty() == GraphElement.Difficulty.HARD) {
			duration = 6;
		}
		duration *= graph.getNodes().size();
		System.out.printf("Game Length %ds%n", duration);

		if(ChosenGameMode.chooseGameMode1) {
			gameMode = new FirstGameMode(graph, DifficultyScene.getSelectedDifficulty(), TimeUnit.SECONDS.toMillis(duration));
		} else if (ChosenGameMode.chooseGameMode2) {
			gameMode = new SecondGameMode(graph, DifficultyScene.getSelectedDifficulty(), TimeUnit.SECONDS.toMillis(duration));
		} else if (ChosenGameMode.chooseGameMode3) {
			gameMode = new ThirdGameMode(graph, DifficultyScene.getSelectedDifficulty(), TimeUnit.SECONDS.toMillis(duration));
		}

		GameElement graphGameElement = new GameElement(window, graph, gameMode);
		return new Scene(graphGameElement, Game.RESOLUTION_WIDTH, Game.RESOLUTION_HEIGHT, true, SceneAntialiasing.BALANCED);


	}

}
