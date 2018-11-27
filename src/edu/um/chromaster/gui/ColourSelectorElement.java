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

        Color initColor = this.getItems().get(0);
        this.setStyle(String.format("-fx-border-width: 5; -fx-border-color: rgb(%.0f, %.0f, %.0f);", initColor.getRed() * 255, initColor.getGreen() * 255, initColor.getBlue() * 255));

        this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.setOnMouseClicked(event -> {
            Color selectedColour = ColourSelectorElement.this.getSelectionModel().getSelectedItem();

            if(selectedColour == null) {
                this.setStyle("-fx-border-width: 5; -fx-border-color: white;");

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

                    Color newColour = colorPicker.getValue();
                    this.setStyle(String.format("-fx-border-width: 5; -fx-border-color: rgb(%.0f, %.0f, %.0f);", newColour.getRed() * 255, newColour.getGreen() * 255, newColour.getBlue() * 255));
                    ColourSelectorElement.this.getItems().add(colorPicker.getValue());
                });

                return;
            } else {
                this.setStyle(String.format("-fx-border-width: 5; -fx-border-color: rgb(%.0f, %.0f, %.0f);", selectedColour.getRed() * 255, selectedColour.getGreen() * 255, selectedColour.getBlue() * 255));
            }

            Game.getEventHandler().trigger(new SelectColourEvent(selectedColour));
        });

        this.setOrientation(Orientation.HORIZONTAL);
        this.setMaxSize(1024, 100);
        this.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Color item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setText(null);
                    setTextFill(null);
                    setBackground(null);
                } else {
                    setBackground(new Background(new BackgroundFill(item, null, null)));
                }
            }
        });
    }

}
