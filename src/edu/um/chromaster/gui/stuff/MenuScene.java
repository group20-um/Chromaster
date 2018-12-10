package edu.um.chromaster.gui.stuff;

import edu.um.chromaster.gui.GraphElement;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.List;

public class MenuScene {

    public static StackPane createBasicParent(Node element) {
        StackPane parent = new StackPane();
        parent.getStylesheets().add("res/style.css");
        parent.setBackground(new Background(new BackgroundImage(new Image("res/menu_background.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        parent.getChildren().add(element);
        return parent;
    }

    public static StackPane createParent(List<Node> elements) {
        return createParent(elements, null);
    }

    public static StackPane createParent(List<Node> elements, GraphElement.Callback<String> callback) {
        BorderPane borderPane = new BorderPane();

        {
            HBox topBar = new HBox();
            if(callback != null) {
                Button back = new Button("Go back");
                back.setOnAction(e -> callback.modify(""));
                back.getStyleClass().add("menu-button");
                topBar.getChildren().add(back);
            }

            Button rules = new Button("Rules");
            rules.getStyleClass().add("menu-button");
            rules.setOnAction(e -> RulesBox.display());
            topBar.getChildren().addAll(rules);
            //topBar.setPadding(new Insets(10, 10, 10, 10));
            borderPane.setTop(topBar);
        }

        {
            GridPane grid = new GridPane();
            grid.setMaxWidth(1280 * 0.623);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(0, 10, 0, 10));
            grid.setAlignment(Pos.CENTER);

            for(int i = 0; i < elements.size(); i++) {
                if(elements.get(i) instanceof Button) {
                    elements.get(i).getStyleClass().add("menu-button");
                }
                grid.add(elements.get(i), 2, i+1);
            }

            borderPane.setCenter(grid);
        }

        return createBasicParent(borderPane);
    }

}
