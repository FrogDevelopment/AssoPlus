/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
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

import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.service.MembersService;

import java.util.function.Consumer;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MembersController extends AbstractCustomController {

    @Autowired
    private MembersService membersService;

    @FXML
    private TableView<MemberDto> tableView;
    @FXML
    private TableColumn<MemberDto, Boolean> subsciptionCol;
    @FXML
    private TableColumn<MemberDto, Boolean> annalsCol;

    private ObservableList<MemberDto> data;

    @Override
    protected void initialize() {
        data = FXCollections.observableArrayList(membersService.getAll());
        tableView.setItems(data);

        tableView.setRowFactory(param -> {
            TableRow<MemberDto> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !tableRow.isEmpty()) {
                    updateMember();
                }

                if (event.getButton() == MouseButton.SECONDARY) {
                    final ContextMenu contextMenu = new ContextMenu();
                    MenuItem deleteItem = new MenuItem(getMessage("global.delete"));

                    MenuItem updateItem = new MenuItem(getMessage("global.update"));
                    updateItem.setOnAction(e -> updateMember());
                    contextMenu.getItems().add(updateItem);

                    deleteItem.setOnAction(e -> {
                        MemberDto selectedItem = tableView.getSelectionModel().getSelectedItem();
                        // FIXME
                        showYesNoDialog(String.format(getMessage("global.confirm.delete"), "l'étudiant " + selectedItem.getStudentNumber()), o -> removeMember(selectedItem));
                    });
                    contextMenu.getItems().add(deleteItem);

                    // only display context menu for non-null items:
                    tableRow.contextMenuProperty().bind(
                            Bindings.when(Bindings.isNotNull(tableRow.itemProperty()))
                                    .then(contextMenu)
                                    .otherwise((ContextMenu) null)
                    );
                }
            });
            return tableRow;
        });


        subsciptionCol.setCellFactory(CheckBoxTableCell.forTableColumn(subsciptionCol));
        annalsCol.setCellFactory(CheckBoxTableCell.forTableColumn(annalsCol));
    }

    private void removeMember(MemberDto selectedItem) {
        membersService.deleteData(selectedItem);
        data.remove(selectedItem);
    }

    public void addMember() {
        Stage dialog = openDialog("/fxml/member.fxml", new Consumer<MemberController>() {
            @Override
            public void accept(MemberController memberController) {
                memberController.newData(tableView.getItems());
            }
        });

        dialog.setTitle(getMessage("member.create.title"));
        dialog.setWidth(330);
        dialog.setHeight(375);
        dialog.setResizable(false);

        dialog.show();
    }

    private void updateMember() {
        Stage dialog = openDialog("/fxml/member.fxml", new Consumer<MemberController>() {
            @Override
            public void accept(MemberController memberController) {
                memberController.updateData(tableView.getItems(), tableView.getSelectionModel().getSelectedIndex());
            }
        });

        dialog.setTitle(getMessage("member.update.title"));
        dialog.setWidth(330);
        dialog.setHeight(375);
        dialog.setResizable(false);

        dialog.show();
    }

    public void manageDegrees() {
        Stage dialog = openDialog("/fxml/degrees.fxml");
        dialog.setTitle(getMessage("member.degrees"));
        dialog.setWidth(550);
        dialog.setHeight(400);

        dialog.show();
    }
}
