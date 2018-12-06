package edu.um.chromaster.gui;

import edu.um.chromaster.Game;
import edu.um.chromaster.gui.stuff.MainScene;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Intro extends FlowPane {

    private Scene scene;
    private FlowPane flowPane = new FlowPane();

    public Intro() {
        this.setup();
        this.scene = new Scene(flowPane, 1280, 720);
    }

    public Scene getAssociatedScene() {
        return this.scene;
    }

    private void setup() {
        Label empty=new Label(" ");
        Label c = new Label("C");
        Label h = new Label("H");
        Label r1 = new Label("R");
        Label o = new Label("O");
        Label m = new Label("M");
        Label a = new Label("A");
        Label s = new Label("S");
        Label t = new Label("T");
        Label e = new Label("E");
        Label r2 = new Label("R");

        Font f= new Font("Arial", 100);
        empty.setFont(f);
        c.setTextFill(Color.rgb(200, 0,100));
        c.setFont(f);
        h.setTextFill(Color.rgb(0,100,100));
        h.setFont(f);
        r1.setTextFill(Color.YELLOW);
        r1.setFont(f);
        o.setTextFill(Color.rgb(150,0,0));
        o.setFont(f);
        m.setTextFill(Color.rgb(50,150,0));
        m.setFont(f);
        a.setTextFill(Color.GOLD);
        a.setFont(f);
        s.setTextFill(Color.rgb(50, 50, 150));
        s.setFont(f);
        t.setTextFill(Color.PURPLE);
        t.setFont(f);
        e.setTextFill(Color.rgb(0,100,50));
        e.setFont(f);
        r2.setTextFill(Color.rgb(150, 0, 100));
        r2.setFont(f);
        flowPane.getChildren().addAll(empty, c, h, r1, o, m, a, s, t, e, r2);

        ImageView iv = new ImageView(new Image("res/unicorn.gif", 1280, 720, false, true));
        iv.setPreserveRatio(false);
        flowPane.getChildren().add(iv);
        flowPane.setBackground(new Background(new BackgroundFill(Color.rgb(0,0,36), CornerRadii.EMPTY, Insets.EMPTY)));
        flowPane.setOnMouseClicked(event -> {
            System.out.println("test#1");
            Game.getInstance().setScene(MainScene.createMainScene(Game.getInstance().getStage()));
        });
    }

}
