package edu.um.chromaster.gui.stuff;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GameModeScene {
	
	
	public static Scene createGameModeScene(Stage window) {
		
		Label intro = new Label("Good choice! ;) ");
		Label chooseGraph = new Label("Do you want to play with a random graph or do you want to generate it yourself?");
		Button button1GM = new Button("Play with a random graph");
		Button button2GM = new Button("Generate my own graph");
		
		button1GM.setOnAction(e -> pressedButton1GM(window));
		button2GM.setOnAction(e -> window.setScene(GenerateGraphScene.generateGraphScene(window)));
		
		
		
		GridPane gameModeGrid = new GridPane();
		gameModeGrid.setHgap(10);
		gameModeGrid.setVgap(10);
		gameModeGrid.setPadding(new Insets(0, 10, 0, 10));
		   
		gameModeGrid.add(intro, 2, 2);
		gameModeGrid.add(chooseGraph, 2, 3);
		gameModeGrid.add(button1GM, 2, 4);
		gameModeGrid.add(button2GM, 2, 5);
		   
		gameModeGrid.setAlignment(Pos.CENTER);
		
		HBox topBar = new HBox();
		Button back = new Button("Go back");
		back.setOnAction(e -> window.setScene(MainScene.createMainScene(window)));
		Button rules = new Button("Rules");
		rules.setOnAction(e -> RulesBox.display());
		topBar.getChildren().addAll(back, rules);
		topBar.setPadding(new Insets(2,2,2,2));
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topBar);
		borderPane.setCenter(gameModeGrid);
		borderPane.getStyleClass().add("MainScene-background");
		
		Scene gameModeScene = new Scene(borderPane, 1000, 800);
		gameModeScene.getStylesheets().add("res/style.css");

		return gameModeScene;
	}
	
	public static void pressedButton1GM(Stage window) {
		ChosenVerticesOrEdges.random = true;
		ChosenVerticesOrEdges.vertices = false;
		ChosenVerticesOrEdges.verticesAndEdges = false;
		ChosenVerticesOrEdges.edges = false;
		System.out.println("The player is playing with a random graph");
		window.setScene(DifficultyScene.createDifficultyScene(window));
		
	}

}
