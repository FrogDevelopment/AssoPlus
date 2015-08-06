/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.Main;
import fr.frogdevelopment.assoplus.components.controls.MaskHelper;
import fr.frogdevelopment.assoplus.components.controls.Validator;
import fr.frogdevelopment.assoplus.dto.CategoryDto;
import fr.frogdevelopment.assoplus.dto.EventDto;
import fr.frogdevelopment.assoplus.service.CategoriesService;
import fr.frogdevelopment.assoplus.service.EventsService;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@Controller("eventController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventsController implements Initializable {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

//	private ResourceBundle bundle;

    @Autowired
    private EventsService eventsService;

    @Autowired
    private CategoriesService categoriesService;

    @FXML
    private VBox vbTop;
    @FXML
    private Button btnShowHide;

    @FXML
    private TextField txtTitle;
    @FXML
    private DatePicker dpDate;
    @FXML
    public ComboBox<CategoryDto> cbCategory;
    @FXML
    public TextArea taText;

    @FXML
    private TableView<EventDto> table;

    private ObservableList<EventDto> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//		bundle = resources;
        data = eventsService.getAllData();
        table.setItems(data);

        MaskHelper.addMaskDate(dpDate);
        dpDate.setPromptText("jj/mm/aaaa");
        dpDate.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return DATE_TIME_FORMATTER.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, DATE_TIME_FORMATTER);
                } else {
                    return null;
                }
            }
        });

        cbCategory.setItems(categoriesService.getAllData());
        cbCategory.setVisibleRowCount(4);
        cbCategory.setConverter(new StringConverter<CategoryDto>() {

            private final Map<String, CategoryDto> _cache = new HashMap<>();

            @Override
            public String toString(CategoryDto item) {
                _cache.put(item.getCode(), item);
                return item.getCode();
            }

            @Override
            public CategoryDto fromString(String code) {
                return _cache.get(code);
            }
        });

    }

    public void saveData() {

        boolean isOk = Validator.validate(txtTitle);
        isOk &= Validator.validate(dpDate);
        isOk &= Validator.validate(cbCategory);
        isOk &= Validator.validate(taText);

        if (isOk) {
            EventDto dto = new EventDto();
            dto.setTitle(txtTitle.getText());
            dto.setDate(dpDate.getValue().format(DATE_TIME_FORMATTER));
            dto.setCategoryCode(cbCategory.getValue().getCode());
            dto.setText(taText.getText());

            eventsService.saveData(dto);

            data.add(dto);
        }
    }

    public void showHideEvent() {
        final boolean isVisible = vbTop.isVisible();
        vbTop.setManaged(!isVisible);
        vbTop.setVisible(!isVisible);

        btnShowHide.setText(isVisible ? "Montrer" : "Cacher");
    }

    public void manageCategories(MouseEvent event) {
        Button source = (Button) (event.getSource());
        Window parent = source.getScene().getWindow();

        Parent root = Main.load("/fxml/events/categories.fxml");
        Stage dialog = new Stage(/*StageStyle.TRANSPARENT*/);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(parent);
        dialog.setTitle("test");
        dialog.setScene(new Scene(root, 450, 450));

        dialog.setOnCloseRequest(event1 -> cbCategory.setItems(categoriesService.getAllData()));

        dialog.show();
    }
}
