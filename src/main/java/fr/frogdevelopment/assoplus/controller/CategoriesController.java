/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.components.controls.Validator;
import fr.frogdevelopment.assoplus.dto.CategoryDto;
import fr.frogdevelopment.assoplus.service.CategoriesService;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

@Controller("categoriesController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CategoriesController implements Initializable {

    @FXML
    private ResourceBundle resources ;

    @FXML
    private TextField txtLabel;
    @FXML
    private TextField txtCode;
    @FXML
    private TreeTableView<CategoryDto> treeTableView;
    @FXML
    private TreeTableColumn<CategoryDto, String> columnCode;
    @FXML
    private TreeTableColumn<CategoryDto, String> columnLabel;

    @Autowired
    private CategoriesService categoriesService;

    private ObservableList<CategoryDto> dtos;
    private TreeItem<CategoryDto> rootItem;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        initData();

        columnCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        columnCode.setCellFactory(p -> new TextFieldTreeTableCell(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }));

        columnLabel.setCellValueFactory(new TreeItemPropertyValueFactory<>("label"));
        columnLabel.setCellFactory(p -> new TextFieldTreeTableCell(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }));
    }

    private void initData() {
        rootItem = new TreeItem<>(new CategoryDto());

        dtos = categoriesService.getAllData();
        dtos.forEach(dto -> {
            rootItem.getChildren().add(new TreeItem<>(dto));
        });

        rootItem.getChildren().sort(Comparator.comparing(o1 -> o1.getValue().getCode()));
        rootItem.setExpanded(true);
        treeTableView.setRoot(rootItem);
        treeTableView.setShowRoot(false);
        treeTableView.setEditable(true);
    }

    public void onSave() {
        categoriesService.saveOrUpdateAll(dtos);
        initData();
    }

    public void onClose(Event event) {
        close(event);
    }

    private void close(Event event) {
        Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Event.fireEvent(window, new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void onAddCategory() {

        boolean isOk = Validator.validate(txtCode);
        isOk &= Validator.validate(txtLabel);

        if (isOk) {
            CategoryDto dto = new CategoryDto();
            dto.setCode(txtCode.getText());
            dto.setLabel(txtLabel.getText());
            if (dtos.contains(dto)) {
                // fixme
                txtCode.setStyle("-fx-border-color: red");
                Tooltip tooltip = new Tooltip(resources.getString("global.error.already.present"));
                txtCode.setTooltip(tooltip);
                tooltip.setAutoHide(true);
            } else {
                txtCode.setStyle("-fx-border-color: null");
                txtCode.setTooltip(null);

                dtos.add(dto);

                final TreeItem<CategoryDto> newItem = new TreeItem<>(dto);
                rootItem.getChildren().add(newItem);
                rootItem.getChildren().sort(Comparator.comparing(o1 -> o1.getValue().getCode()));
                rootItem.setExpanded(true);

                txtCode.setText(null);
                txtLabel.setText(null);
                txtCode.requestFocus();
            }
        }
    }

    public void onRemoveCategory() {
        final TreeItem<CategoryDto> selectedItem = treeTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setHeaderText(resources.getString("global.warning.title"));
        dialog.setContentText(String.format(resources.getString("global.confirm.delete"),resources.getString("event.category")));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);

        dialog.showAndWait()
                .filter(response -> response == ButtonType.YES)
                .ifPresent(response -> removeCategory(selectedItem));
    }

    private void removeCategory(final TreeItem<CategoryDto> selectedItem) {
        rootItem.getChildren().remove(selectedItem);

        CategoryDto dto = selectedItem.getValue();
        dtos.remove(dto);
        if (dto.getId() != 0) {
            categoriesService.deleteData(dto);
        }
    }
}
