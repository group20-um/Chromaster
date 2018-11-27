
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {
	
	
	//Game modes
	Scene gameModeScene;
	Label intro;
	Label chooseGraph;
	
	//Top bar
	Button back;
	
	//Choose graph
	Scene generateGraphScene;
	Label VorE;
	Button vertices;
	Button edges;
	Button verticesAndEdges;
	
	//Choose difficulty level
	Scene difficultyScene;
	Label howDifficult;
	Button easy;
	Button medium;
	Button hard;
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage window) throws Exception {
		
			
		//Main window
		window.setTitle("Chromaster");
		window.setScene(MainScene.createMainScene(window));
		window.show();
		
		
	}
	
}
