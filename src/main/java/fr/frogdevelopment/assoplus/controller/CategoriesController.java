/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.dto.CategoryDto;
import fr.frogdevelopment.assoplus.service.CategoriesService;

import java.net.URL;
import java.util.ResourceBundle;

import static fr.frogdevelopment.assoplus.components.controls.Validator.validate;
import static fr.frogdevelopment.assoplus.components.controls.Validator.validateNotBlank;

@Controller("categoriesController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CategoriesController implements Initializable {

    private ResourceBundle resources;

    @FXML
    private Label lbError;
    @FXML
    private TextField txtLabel;
    @FXML
    private TextField txtCode;
    @FXML
    private TableView<CategoryDto> tableView;
    @FXML
    private TableColumn<CategoryDto, String> columnCode;
    @FXML
    private TableColumn<CategoryDto, String> columnLabel;
    @FXML
    private TableColumn<CategoryDto, String> columnAction;

    @Autowired
    private CategoriesService categoriesService;

    private ObservableList<CategoryDto> dtos;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        dtos = categoriesService.getAllData();
        tableView.setEditable(true);
        tableView.setItems(dtos);

        columnCode.setCellFactory(TextFieldTableCell.forTableColumn());
//        columnCode.setCellFactory(p -> new EditingCell());
        columnCode.setOnEditCommit(event -> getDto(event).setCode(event.getNewValue()));

        columnLabel.setCellFactory(TextFieldTableCell.forTableColumn());
//        columnLabel.setCellFactory(p -> new EditingCell());
        columnLabel.setOnEditCommit(event -> getDto(event).setLabel(event.getNewValue()));


        columnAction.setCellFactory(param -> {
            final TableCell<CategoryDto, String> cell = new TableCell<CategoryDto, String>() {
                @Override
                public void updateItem(String value, boolean empty) {
                    super.updateItem(value, empty);

                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        final VBox vbox = new VBox(5);
                        vbox.setAlignment(Pos.CENTER);
                        Image image = new Image(getClass().getResourceAsStream("/img/delete_16.png"));
                        Button button = new Button("", new ImageView(image));
                        button.setOnAction(event -> removeItem(getTableRow().getIndex()));
                        vbox.getChildren().add(button);
                        setGraphic(vbox);
                    }
                }
            };
            return cell;
        });
    }

    protected CategoryDto getDto(TableColumn.CellEditEvent<CategoryDto, String> event) {
        return event.getTableView().getItems().get(event.getTablePosition().getRow());
    }

    public void onSave(Event event) {
        categoriesService.saveOrUpdateAll(dtos);
        close(event);
    }

    public void onCancel(Event event) {
        close(event);
    }

    private void close(Event event) {
        Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Event.fireEvent(window, new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public void onAddCategory() {

        boolean isOk = validateNotBlank(txtCode);
        isOk &= validateNotBlank(txtLabel);

        if (isOk) {
            CategoryDto dto = new CategoryDto();
            dto.setCode(txtCode.getText());
            dto.setLabel(txtLabel.getText());

            boolean validate = validate(() -> !dtos.contains(dto), "global.error.msg.already.present", txtCode);

            if (validate) {
                dtos.add(dto);
                txtCode.clear();
                txtLabel.clear();
                txtCode.requestFocus();

                lbError.setManaged(false);
                lbError.setVisible(false);
                lbError.setText("");
            } else {
                lbError.setManaged(true);
                lbError.setVisible(true);
                lbError.setText(resources.getString("global.error.msg.already.present"));
            }
        }
    }

    private void removeItem(int index) {
        CategoryDto item = tableView.getItems().get(index);
        if (item == null) {
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setHeaderText(resources.getString("global.warning.title"));
        dialog.setContentText(String.format(resources.getString("global.confirm.delete"), item.toString()));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.NO);

        dialog.showAndWait()
                .filter(response -> response == ButtonType.YES)
                .ifPresent(response -> {
                    dtos.remove(item);
                    if (item.getId() != 0) {
                        categoriesService.deleteData(item);
                    }
                });
    }


    class EditingCell extends TableCell<CategoryDto, String> {

        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText(getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(null);
            }

        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    commitEdit(textField.getText());
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }
    }
}
