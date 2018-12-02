package edu.um.chromaster.gui;

import edu.um.chromaster.Game;
import edu.um.chromaster.event.events.SelectColourEvent;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.colorchooser.ColorSelectionModel;

public class ColourSelectorElement extends ListView<Color> {

    public ColourSelectorElement(Stage stage) {
        this.setItems(FXCollections.observableArrayList(
                Color.RED,
                Color.GREEN,
                Color.BLUE
        ));

        this.getStyleClass().add("color-selector-element");

        Color initColor = this.getItems().get(0);
        this.setStyle(String.format("-fx-border-width: 5; -fx-border-color: rgb(%.0f, %.0f, %.0f);", initColor.getRed() * 255, initColor.getGreen() * 255, initColor.getBlue() * 255));

        this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        this.setOrientation(Orientation.HORIZONTAL);
        this.setMaxSize(1280, 720 * 0.05);
        this.setCellFactory(lv -> {

            ListCell<Color> cell = new ListCell<Color>() {
                @Override
                protected void updateItem(Color item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setBackground(new Background(new BackgroundFill(item, null, null)));
                        getProperties().put("active", true);
                    } else {
                        getProperties().put("active", false);
                    }
                }

            };

            cell.setOnMouseClicked(event -> {
                //--- Clicking on a cell, if it is empty the user is prompted to create a new colour
                if (cell.isEmpty()) {

                    ColorPicker colorPicker = new ColorPicker();
                    colorPicker.setId(ColorPicker.STYLE_CLASS_SPLIT_BUTTON);
                    colorPicker.setOnAction(e -> {
                        if(this.getItems().contains(colorPicker.getValue())) {
                            Alert alert = new Alert(Alert.AlertType.WARNING, "You cannot create a colour that is already in your list.");
                            alert.showAndWait();
                            event.consume();
                            colorPicker.setValue(null);
                        }
                    });
                    final Stage dialog = new Stage();
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.initOwner(stage);

                    Scene dialogScene = new Scene(colorPicker, 300, 300);
                    dialog.setScene(dialogScene);
                    dialog.show();

                    dialog.setOnCloseRequest((e) -> {

                        if(colorPicker.getValue() == null || this.getItems().contains(colorPicker.getValue())) {
                            event.consume();
                            return;
                        }

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to add this colour to your list?");
                        alert.showAndWait().ifPresent(buttonType -> {
                            if(buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                                Color newColour = colorPicker.getValue();
                                Game.getEventHandler().trigger(new SelectColourEvent(newColour));
                                this.setStyle(String.format("-fx-border-width: 5; -fx-border-color: rgb(%.0f, %.0f, %.0f);", newColour.getRed() * 255, newColour.getGreen() * 255, newColour.getBlue() * 255));
                                ColourSelectorElement.this.getItems().add(colorPicker.getValue());
                            }
                        });


                    });
                } else {
                    Game.getEventHandler().trigger(new SelectColourEvent(cell.getItem()));
                    this.getSelectionModel().select(cell.getIndex());
                    this.setStyle(String.format("-fx-border-width: 5; -fx-border-color: rgb(%.0f, %.0f, %.0f);", cell.getItem().getRed() * 255, cell.getItem().getGreen() * 255, cell.getItem().getBlue() * 255));
                }

            });

            return cell;

        });
    }

}
