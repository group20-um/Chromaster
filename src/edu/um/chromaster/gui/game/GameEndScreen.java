package edu.um.chromaster.gui.game;

import edu.um.chromaster.Game;
import edu.um.chromaster.event.events.GameEndEvent;
import edu.um.chromaster.gui.menu.MainScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * The GameEndScreen is presented to the user when they either won or lost.
 */
public class GameEndScreen extends BorderPane {

    public GameEndScreen() {
        this.setVisible(false);
    }

    /**
     * Called to turn it visible.
     * @param event The associated event so the screen knows what it has to display depending on the outcome.
     */
    public void execute(GameEndEvent event) {
        if (event.isWin()) {
            ImageView w=new ImageView(new Image("res/wow.gif"));
            ImageView r=new ImageView(new Image("res/rainbow.gif"));

            Label text=new Label(" You won! ");
            text.setFont(new Font("Arial", 100));
            text.setTextFill(Color.CORNFLOWERBLUE);

            this.setCenter(text);
            this.setTop(w);
            this.setBottom(r);

            setAlignment(text, Pos.CENTER);
            setAlignment(w, Pos.CENTER);
            setAlignment(r, Pos.BOTTOM_CENTER);

        } else {
            ImageView background = new ImageView("res/youTried.gif");
            Button againButton = new Button("Play Again");
            {
                againButton.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
                againButton.setFont(new Font("Arial", 50));
                againButton.setOnAction(e -> Game.getInstance().setScene(MainScene.createMainScene(Game.getInstance().getStage())));
            }
            Label text = new Label(" Game Over! ");
            {
                text.setFont(new Font("Arial", 100));
                text.setTextFill(Color.CORNFLOWERBLUE);
            }

            this.setTop(text);
            this.setCenter(againButton);
            this.setBottom(background);

            setAlignment(background, Pos.BOTTOM_CENTER);
            setAlignment(text, Pos.TOP_CENTER);
        }


        this.setBackground(new Background(new BackgroundFill(Color.rgb(0,0,36), CornerRadii.EMPTY, Insets.EMPTY)));
        this.setVisible(true);
    }

}
