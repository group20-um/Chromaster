package edu.um.chromaster.gui;

import com.sun.javafx.scene.control.skin.CustomColorDialog;
import edu.um.chromaster.Game;
import edu.um.chromaster.event.events.SelectColourEvent;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ColourSelectorElement extends ListView<Color> {

    public ColourSelectorElement(Stage stage) {
        this.setItems(FXCollections.observableArrayList(
                Color.RED,
                Color.GREEN,
                Color.BLUE
        ));

        ColorPicker colorPicker = new ColorPicker();

        this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.setOnMouseClicked(event -> {
            Color selectedColour = ColourSelectorElement.this.getSelectionModel().getSelectedItem();


            if(selectedColour == null) {
                CustomColorDialog customColorDialog = new CustomColorDialog(stage);
                customColorDialog.show();

                customColorDialog.setOnSave(() -> {
                    ColourSelectorElement.this.getItems().add(customColorDialog.getCustomColor());
                });

                return;
            }

            Game.getEventHandler().trigger(new SelectColourEvent(selectedColour));
        });

        this.getChildren().add(colorPicker);

        this.setOrientation(Orientation.HORIZONTAL);
        this.setMaxSize(100, 20);
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
