/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.dto.EventDto;
import fr.frogdevelopment.assoplus.service.EventsService;

import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

@Controller("eventsController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventsController extends AbstractCustomController {

    private DateTimeFormatter dateTimeFormatter;

    @Autowired
    private EventsService eventsService;

    @FXML
    private TableView<EventDto> tableView;
    @FXML
    private TableColumn<EventDto, Boolean> colPublished;
    @FXML
    private TableColumn<EventDto, String> colDate;
    @FXML
    private Button btnSave;

    private ObservableList<EventDto> data;

    private EventDto currentData;

    @Override
    public void initialize() {
        dateTimeFormatter = DateTimeFormatter.ofPattern(getMessage("global.date.format"));

        data = FXCollections.observableArrayList(eventsService.getAll());
        tableView.setItems(data);

//        colDate.setCellValueFactory(param -> {
//            SimpleStringProperty property = new SimpleStringProperty();
//            final LocalDate birthday = param.getValue().getDate();
//            if (birthday != null) {
//                property.setValue(dateTimeFormatter.format(birthday));
//            }
//
//            return property;
//        });

        tableView.setRowFactory(param -> {
            TableRow<EventDto> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1
                        && !tableRow.isEmpty()
                        && event.getButton() == MouseButton.SECONDARY) {

                    EventDto selectedItem = tableView.getSelectionModel().getSelectedItem();

                    final ContextMenu contextMenu = new ContextMenu();

                    MenuItem deleteItem = new MenuItem(getMessage("global.delete"));
                    deleteItem.disableProperty().bind(selectedItem.publishedProperty());
                    deleteItem.setOnAction(e -> {
                        // FIXME
                        showYesNoDialog(String.format(getMessage("global.confirm.delete"), "l'évènement " + selectedItem.getTitle()), o -> removeEvent(selectedItem));
                    });
                    contextMenu.getItems().add(deleteItem);

                    MenuItem updateItem = new MenuItem(getMessage(selectedItem.getPublished() ? "global.see" : "global.update"));
                    updateItem.setOnAction(e -> updateEvent());
                    contextMenu.getItems().add(updateItem);


                    MenuItem publishItem = new MenuItem(getMessage("event.publish"));
                    publishItem.disableProperty().bind(selectedItem.publishedProperty());
                    publishItem.setOnAction(e -> publishEvent());
                    contextMenu.getItems().add(publishItem);

                    // only display context menu for non-null items:
                    tableRow.contextMenuProperty().bind(
                            Bindings.when(Bindings.isNotNull(tableRow.itemProperty()))
                                    .then(contextMenu)
                                    .otherwise((ContextMenu) null)
                    );
                } else if (event.getClickCount() == 2
                        && !tableRow.isEmpty()
                        && !(event.getTarget() instanceof CheckBoxTableCell)) {
                    updateEvent();
                }
            });
            return tableRow;
        });

        colPublished.setCellValueFactory(param -> param.getValue().publishedProperty());
        colPublished.setCellFactory(CheckBoxTableCell.forTableColumn(colPublished));
    }

    public void addEvent() {
        Stage dialog = openDialog("/fxml/event.fxml", new Consumer<EventController>() {
            @Override
            public void accept(EventController controller) {
                controller.newData(tableView.getItems());
            }
        });

        dialog.setTitle(getMessage("event.create.title"));
        dialog.setWidth(380);
        dialog.setHeight(350);
        dialog.setResizable(false);

        dialog.show();
    }

    private void updateEvent() {
        Stage dialog = openDialog("/fxml/event.fxml", new Consumer<EventController>() {
            @Override
            public void accept(EventController eventController) {
                eventController.updateData(tableView.getItems(), tableView.getSelectionModel().getSelectedIndex());
            }
        });

        dialog.setTitle(getMessage("event.update.title"));
        dialog.setWidth(380);
        dialog.setHeight(350);
        dialog.setResizable(false);

        dialog.show();
    }

    private void removeEvent(EventDto selectedItem) {
        eventsService.deleteData(selectedItem);
        data.remove(selectedItem);
    }

    private void publishEvent() {
        EventDto selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (eventsService.publishEvent(selectedItem)) {
            selectedItem.setPublished(true);
        }
    }

}
