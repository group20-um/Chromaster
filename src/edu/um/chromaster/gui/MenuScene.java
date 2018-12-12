package edu.um.chromaster.gui;

import edu.um.chromaster.gui.game.GraphElement;
import edu.um.chromaster.gui.menu.boxes.RulesBox;
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
            borderPane.setTop(topBar);
        }

        {
            VBox grid = new VBox(15);
            //grid.setHgap(10);
            //grid.setVgap(10);
            grid.setPadding(new Insets(0, 10, 0, 10));
            grid.setAlignment(Pos.CENTER);

            for(int i = 0; i < elements.size(); i++) {
                if(elements.get(i) instanceof Button) {
                    elements.get(i).getStyleClass().add("menu-button");
                }
                grid.getChildren().add(elements.get(i));
            }

            borderPane.setCenter(grid);
        }

        return createBasicParent(borderPane);
    }

}
