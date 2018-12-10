package edu.um.chromaster.gui.stuff;
import edu.um.chromaster.gui.GraphElement;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Arrays;

public class DifficultyScene {
	
	private static Button easy, medium, hard;
	private static GraphElement.Difficulty selectedeDifficulty = GraphElement.Difficulty.EASY;

	public static GraphElement.Difficulty getSelectedeDifficulty() {
		return selectedeDifficulty;
	}

	public static Scene createDifficultyScene(Stage window) {
		Label howDifficult = new Label("How difficult do you want your graph to be?");
		easy = new Button("Easy");
		medium = new Button("Medium");
		hard = new Button("Hard, I'm a Chromaster");
		
		easy.setOnAction(e -> {
			selectedeDifficulty = GraphElement.Difficulty.EASY;
			window.setScene(PlayScene.createPlayScene(window));
		});
		medium.setOnAction(e -> {
			selectedeDifficulty = GraphElement.Difficulty.MEDIUM;
			window.setScene(PlayScene.createPlayScene(window));
		});
		hard.setOnAction(e -> {
			selectedeDifficulty = GraphElement.Difficulty.HARD;
			window.setScene(PlayScene.createPlayScene(window));
		});

		Scene difficultyScene = new Scene(MenuScene.createParent(Arrays.asList(howDifficult, easy, medium, hard), (a) -> {
			window.setScene(GameModeScene.createGameModeScene(window));
		}), -1, -1);
		difficultyScene.getStylesheets().add("res/style.css");
		return difficultyScene;
	}

}
