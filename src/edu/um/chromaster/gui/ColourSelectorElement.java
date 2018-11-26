package edu.um.chromaster.gui;

import edu.um.chromaster.Game;
import edu.um.chromaster.event.events.SelectColourEvent;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ColourSelectorElement extends ListView<Color> {

    public ColourSelectorElement(Stage stage) {
        this.setItems(FXCollections.observableArrayList(
                Color.RED,
                Color.GREEN,
                Color.BLUE
        ));


        this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.setOnMouseClicked(event -> {
            Color selectedColour = ColourSelectorElement.this.getSelectionModel().getSelectedItem();


            if(selectedColour == null) {
                ColorPicker colorPicker = new ColorPicker();
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);

                Scene dialogScene = new Scene(colorPicker, 300, 200);
                dialog.setScene(dialogScene);
                dialog.show();

                dialog.setOnCloseRequest((e) -> {

                    if(this.getItems().contains(colorPicker.getValue())) {
                        event.consume();
                        return;
                    }

                    ColourSelectorElement.this.getItems().add(colorPicker.getValue());
                });

                return;
            }

            Game.getEventHandler().trigger(new SelectColourEvent(selectedColour));
        });

        this.setOrientation(Orientation.HORIZONTAL);
        this.setMaxSize(1024, 100);
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
