package edu.um.chromaster.gui.game;

import edu.um.chromaster.Game;
import edu.um.chromaster.event.events.SelectColourEvent;
import edu.um.chromaster.gui.ColorList;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This element keeps track of all the colours the user can use. It displays them and also integrates a {@link ColorPicker}
 * that allows the user to pick a new colour.
 */
public class ColourSelectorElement extends ListView<Color> {

    public ColourSelectorElement(Stage stage) {
        this.setItems(FXCollections.observableArrayList(ColorList.GRAPH_USER_COLOURS));

        this.getStylesheets().add("res/style.css");
        this.getStyleClass().add("color-selector-element");

        // select default colour
        Color initColor = this.getItems().get(0);
        this.getSelectionModel().select(0);
        Game.getInstance().getEventHandler().trigger(new SelectColourEvent(initColor)); //trigger the event so the colour is actually usable
        updateBorder(initColor);

        this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        this.setOrientation(Orientation.HORIZONTAL);
        this.setPrefSize(Game.RESOLUTION_WIDTH, Game.RESOLUTION_HEIGHT * 0.05);
        this.setCellFactory(lv -> {

            ListCell<Color> cell = new ListCell<Color>() {
                @Override
                protected void updateItem(Color item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null && !empty) {
                        setBackground(new Background(new BackgroundFill(item, null, null)));
                        getProperties().put("active", true);
                    } else {
                        setTextFill(null);
                        setBackground(null);
                        setText(null);
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
                            Alert alert = new Alert(Alert.AlertType.WARNING,
                                    "You cannot create a colour that is already in your list.");
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

                        //--- Confirm the choice of the user.
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to add this colour to your list?");
                        alert.showAndWait().ifPresent(buttonType -> {
                            if(buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                                Color newColour = colorPicker.getValue();
                                Game.getInstance().getEventHandler().trigger(new SelectColourEvent(newColour));
                                updateBorder(newColour);
                                ColourSelectorElement.this.getItems().add(colorPicker.getValue());
                            }
                        });


                    });
                }
                // The cell is not empty, so the user clicked on an existing colour. In this case we will send out an event
                // and update the boroder of the element to indicate the selected element.
                else {
                    Game.getInstance().getEventHandler().trigger(new SelectColourEvent(cell.getItem()));
                    this.getSelectionModel().select(cell.getIndex());
                    updateBorder(cell.getItem());
                }

            });

            return cell;

        });
    }

    private void updateBorder(Color color) {
        this.setStyle(String.format("-fx-border-width: 5; -fx-border-color: rgb(%.0f, %.0f, %.0f);", color.getRed() * 255D,
                color.getGreen() * 255D, color.getBlue() * 255D));
    }

}
