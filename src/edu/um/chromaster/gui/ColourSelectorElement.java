package edu.um.chromaster.gui;

import edu.um.chromaster.Game;
import edu.um.chromaster.event.events.SelectColourEvent;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class ColourSelectorElement extends ListView<Color> {

    public ColourSelectorElement() {
        this.setItems(FXCollections.observableArrayList(
                Color.RED,
                Color.GREEN,
                Color.BLUE
        ));
        this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.setOnMouseClicked(event -> {
            Game.getEventHandler().trigger(new SelectColourEvent(ColourSelectorElement.this.getSelectionModel().getSelectedItem()));
        });

        this.setCellFactory(lv -> new ListCell<Color>() {
            @Override
            protected void updateItem(Color item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText(null);
                    setTextFill(null);
                } else {
                    setBackground(new Background(new BackgroundFill(item, null, null)));
                }
            }
        });
    }

}
