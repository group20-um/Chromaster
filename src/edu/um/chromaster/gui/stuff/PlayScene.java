package edu.um.chromaster.gui.stuff;


import edu.um.chromaster.RandomGraph;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.gui.GameElement;
import edu.um.chromaster.gui.GraphElement;
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
		else System.out.println("Error: The way the graph is generated isn't selected"); //Shouldn't happen

		aGraph.setLIMIT(30);
		Graph graph = aGraph.getGraph();
		GameMode gameMode = null;


		if(ChosenGameMode.chooseGameMode1) {
			gameMode = new FirstGameMode(graph, TimeUnit.SECONDS.toMillis(60));
		} else if (ChosenGameMode.chooseGameMode2) {
			gameMode = new SecondGameMode(graph, TimeUnit.SECONDS.toMillis(10));
		} else if (ChosenGameMode.chooseGameMode3) {
			gameMode = new ThirdGameMode(graph, TimeUnit.SECONDS.toMillis(60));
		}

		GameElement graphGameElement = new GameElement(window, graph, gameMode);
		return new Scene(graphGameElement, window.getWidth(), window.getHeight(), true, SceneAntialiasing.BALANCED);


	}

}
