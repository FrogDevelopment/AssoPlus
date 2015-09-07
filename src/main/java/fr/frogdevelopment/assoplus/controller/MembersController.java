/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
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

import fr.frogdevelopment.assoplus.dto.DegreeDto;
import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.dto.OptionDto;
import fr.frogdevelopment.assoplus.service.DegreeService;
import fr.frogdevelopment.assoplus.service.MembersService;
import fr.frogdevelopment.assoplus.service.OptionsService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MembersController extends AbstractCustomController {

    @Autowired
    private MembersService membersService;

    @Autowired
    private DegreeService degreeService;

    @Autowired
    private OptionsService optionsService;

    @FXML
    private TableView<MemberDto> tableView;
    @FXML
    private TableColumn<MemberDto, String> birthdayCol;
    @FXML
    private TableColumn<MemberDto, String> degreeCol;
    @FXML
    private TableColumn<MemberDto, String> optionCol;
    @FXML
    private TableColumn<MemberDto, Boolean> subscriptionCol;
    @FXML
    private TableColumn<MemberDto, Boolean> annalsCol;

    private ObservableList<MemberDto> data;

    @Override
    protected void initialize() {
        data = FXCollections.observableArrayList(membersService.getAll());
        tableView.setItems(data);
        tableView.setEditable(true);

        tableView.setRowFactory(param -> {
            TableRow<MemberDto> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1
                        && !tableRow.isEmpty()
                        && event.getButton() == MouseButton.SECONDARY) {
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
                } else if (event.getClickCount() == 2
                        && !tableRow.isEmpty()
                        && !(event.getTarget() instanceof CheckBoxTableCell)) {
                    updateMember();
                }
            });
            return tableRow;
        });

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(getMessage("global.date.format"));

        birthdayCol.setCellValueFactory(param -> {
            SimpleStringProperty property = new SimpleStringProperty();
            final LocalDate birthday = param.getValue().getBirthday();
            if (birthday != null) {
                property.setValue(dateFormatter.format(birthday));
            }

            return property;
        });

        final Map<String, DegreeDto> mapDegrees = degreeService.getAll().stream().collect(Collectors.toMap(DegreeDto::getCode, dto -> dto));
        degreeCol.setCellValueFactory(param -> {
            final String degreeCode = param.getValue().getDegreeCode();
            String value;
            if (mapDegrees.containsKey(degreeCode)) {
                value = mapDegrees.get(degreeCode).getLabel();
            } else {
                value = degreeCode;
            }

            return new SimpleStringProperty(value);
        });

        final Map<String, OptionDto> mapOptions = optionsService.getAll().stream().collect(Collectors.toMap(OptionDto::getCode, dto -> dto));
        optionCol.setCellValueFactory(param -> {
            final String optionCode = param.getValue().getOptionCode();
            String value;
            if (mapOptions.containsKey(optionCode)) {
                value = mapOptions.get(optionCode).getLabel();
            } else {
                value = optionCode;
            }

            return new SimpleStringProperty(value);
        });

        subscriptionCol.setCellFactory(param -> new CheckBoxTableCell<>(index -> {
            final MemberDto memberDto = data.get(index);
            memberDto.subscriptionProperty().addListener((obs, wasSelected, isSelected) -> {
                membersService.updateData(memberDto);
            });
            return memberDto.subscriptionProperty();
        }));
        subscriptionCol.setEditable(true);
        annalsCol.setCellFactory(param -> new CheckBoxTableCell<>(index -> {
            final MemberDto memberDto = data.get(index);
            memberDto.annalsProperty().addListener((obs, wasSelected, isSelected) -> {
                membersService.updateData(memberDto);
            });
            return memberDto.annalsProperty();
        }));
        annalsCol.setEditable(true);
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
