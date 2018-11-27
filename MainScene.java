import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainScene {
	
	public static Scene createMainScene(Stage window) {
		Button button1 = new Button("To the bitter end");
		Button button2 = new Button("Best upper bound in a fixed time frame");
		Button button3 = new Button("Random order");	
		Label welcome = new Label("Welcome to Chromaster!");
		Label start = new Label("Chose your game mode");
		
		
		button1.setOnAction(e -> window.setScene(GameModeScene.createGameModeScene(window)));
		button2.setOnAction(e -> window.setScene(GameModeScene.createGameModeScene(window)));
		button3.setOnAction(e -> window.setScene(GameModeScene.createGameModeScene(window)));
		
		GridPane mainGrid = new GridPane();
		mainGrid.setHgap(10);
		mainGrid.setVgap(10);
		mainGrid.setPadding(new Insets(0, 10, 0, 10));

		mainGrid.add(welcome, 2, 1);
		mainGrid.add(start, 2, 2);
		mainGrid.add(button1, 2, 3);
		mainGrid.add(button2, 2, 4);
		mainGrid.add(button3, 2, 5);
		   
		mainGrid.setAlignment(Pos.CENTER);
		
		HBox topBar = new HBox();
		Button rules = new Button("Rules");
		rules.setOnAction(e -> RulesBox.display());
		topBar.getChildren().addAll(rules);
		topBar.setPadding(new Insets(2,2,2,2));
		
		BorderPane borderPane = new BorderPane();
		borderPane.setTop(topBar);
		borderPane.setCenter(mainGrid);
		
		Scene scene = new Scene(borderPane, 700, 500);
		
		return scene;
	}

}
