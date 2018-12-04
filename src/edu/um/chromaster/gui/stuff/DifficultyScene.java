package edu.um.chromaster.gui.stuff;
import edu.um.chromaster.gui.GraphElement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
		
		
		GridPane difficultyGrid = new GridPane();
		difficultyGrid.setHgap(10);
		difficultyGrid.setVgap(10);
		difficultyGrid.setPadding(new Insets(0, 10, 0, 10));
		   
		difficultyGrid.add(howDifficult, 2, 2);
		difficultyGrid.add(easy, 2, 3);
		difficultyGrid.add(medium, 2, 4);
		difficultyGrid.add(hard, 2, 5);
		  
		difficultyGrid.setAlignment(Pos.CENTER);
		
		HBox topBar = new HBox();
		Button back = new Button("Go back");
		back.setOnAction(e -> window.setScene(GameModeScene.createGameModeScene(window)));
		Button rules = new Button("Rules");
		rules.setOnAction(e -> RulesBox.display());
		topBar.getChildren().addAll(back, rules);
		topBar.setPadding(new Insets(2,2,2,2));
		
		BorderPane borderPaneDifficulty = new BorderPane();
		borderPaneDifficulty.setTop(topBar);
		borderPaneDifficulty.setCenter(difficultyGrid);
		borderPaneDifficulty.getStyleClass().add("MainScene-background");
		
		Scene difficultyScene = new Scene(borderPaneDifficulty, 1280, 720);
		difficultyScene.getStylesheets().add("res/style.css");
		return difficultyScene;
	}

}
