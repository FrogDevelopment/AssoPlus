/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.service.MembersService;
import fr.frogdevelopment.assoplus.utils.ApplicationUtils;

import java.io.File;
import java.util.function.Consumer;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MembersController extends AbstractCustomController {

    @Autowired
    private MembersService membersService;

    @FXML
    private TableView<MemberDto> table;
    @FXML
    private TableColumn<MemberDto, Boolean> actionCol;
    private ObservableList<MemberDto> data;

    @Override
    protected void initialize() {
        data = FXCollections.observableArrayList(membersService.getAll());
        table.setItems(data);

        // define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        actionCol.setCellValueFactory(features -> new SimpleBooleanProperty(features.getValue() != null));

        // create a cell value factory with an add button for each row in the table.
        actionCol.setCellFactory(personBooleanTableColumn -> new ActionCell());
    }

    /** A table cell containing a button for adding a new person. */
    private class ActionCell extends TableCell<MemberDto, Boolean> {
        final HBox hBox = new HBox();

        /**
         * ActionCell constructor
         */
        ActionCell() {
            hBox.setAlignment(Pos.CENTER);

            Button updateBtn = new Button();
            updateBtn.setGraphic(new ImageView(new Image("/img/edit_user_32.png")));
            hBox.getChildren().add(updateBtn);
            updateBtn.setOnAction(event -> {
                table.getSelectionModel().select(getTableRow().getIndex());
                manageMember(event);
            });

            Button deleteBtn = new Button();
            deleteBtn.setGraphic(new ImageView(new Image("/img/remove_user_32.png")));
            hBox.getChildren().add(deleteBtn);
            deleteBtn.setOnAction(event -> showYesNoDialog("todo", o -> removeMember()));
        }

        /** places an add button in the row only if the row is not empty. */
        @Override protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(hBox);
            } else {
                setGraphic(null);
            }
        }
    }

    private void removeMember() {
        MemberDto selectedItem = table.getSelectionModel().getSelectedItem();

        membersService.deleteData(selectedItem);
        data.remove(selectedItem);
    }

    public void importMembers(MouseEvent event) {
        Window parent = getParent(event);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(getMessage("member.import.title"));
        File file = fileChooser.showOpenDialog(parent);

        if (file != null) {
            membersService.importMembers(file);
        }
    }

    public void addMember(Event event) {
        Window parent = getParent(event);

        Stage dialog = ApplicationUtils.openDialog(parent, "/fxml/members/member.fxml", new Consumer<MemberController>() {
            @Override
            public void accept(MemberController memberController) {
                memberController.setData(table.getItems(), null);
            }
        });

        dialog.setTitle(getMessage("member.create.title"));
        dialog.setWidth(450);
        dialog.setHeight(450);

        dialog.show();
    }

    private void manageMember(Event event) {
        Window parent = getParent(event);

        Stage dialog = ApplicationUtils.openDialog(parent, "/fxml/members/member.fxml", new Consumer<MemberController>() {
            @Override
            public void accept(MemberController memberController) {
                memberController.setData(table.getItems(), table.getSelectionModel().getSelectedItem());
            }
        });

        dialog.setTitle(getMessage("member.create.title"));
        dialog.setWidth(450);
        dialog.setHeight(450);

        dialog.show();
    }

    public void manageDegrees(MouseEvent event) {
        Window parent = getParent(event);

        Stage dialog = ApplicationUtils.openDialog(parent, "/fxml/members/degrees.fxml");
        dialog.setTitle(getMessage("member.degrees"));
        dialog.setWidth(450);
        dialog.setHeight(450);

        dialog.show();
    }
}
