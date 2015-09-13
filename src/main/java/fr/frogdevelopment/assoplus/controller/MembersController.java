/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.components.controls.MaskHelper;
import fr.frogdevelopment.assoplus.dto.DegreeDto;
import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.dto.OptionDto;
import fr.frogdevelopment.assoplus.service.DegreeService;
import fr.frogdevelopment.assoplus.service.MembersService;
import fr.frogdevelopment.assoplus.service.OptionsService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
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
    private TableColumn<MemberDto, String> colStudentNumber;
    @FXML
    private TableColumn<MemberDto, String> colLastname;
    @FXML
    private TableColumn<MemberDto, String> colFirstname;
    @FXML
    private TableColumn<MemberDto, String> colPhone;
    @FXML
    private TableColumn<MemberDto, String> colBirthday;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn<MemberDto, String> colDegree;
    @FXML
    private TableColumn<MemberDto, String> colOption;
    @FXML
    private TableColumn<MemberDto, Boolean> colSubscription;
    @FXML
    private TableColumn<MemberDto, Boolean> colAnnals;

    @FXML
    private HBox hbTop;
    @FXML
    private TextField tfStudentNumber;
    @FXML
    private TextField tfLastname;
    @FXML
    private TextField tfFirstname;
    @FXML
    private TextField tfBirthday;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfDegree;
    @FXML
    private TextField tfOption;
    @FXML
    private TextField tfPhone;
    @FXML
    private ChoiceBox<String> cbSubscription;
    @FXML
    private ChoiceBox<String> cbAnnals;

    @FXML
    private Button editBtn;

    private FilteredList<MemberDto> filteredData;
    private DateTimeFormatter dateFormatter;
    private Map<String, OptionDto> mapOptions;
    private Map<String, DegreeDto> mapDegrees;
    private ObservableList<MemberDto> data;

    @Override
    protected void initialize() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all filteredData).
        data = FXCollections.observableArrayList(membersService.getAll());
        filteredData = new FilteredList<>(data, p -> true);

        // 2. Set the filter Predicate whenever the filters changes.
        initFilters();

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<MemberDto> sortedData = filteredData.sorted();

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableView.setItems(sortedData);

        tableView.setEditable(true);

        tableView.setRowFactory(param -> {
            TableRow<MemberDto> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !tableRow.isEmpty()) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        MemberDto selectedItem = tableView.getSelectionModel().getSelectedItem();
                        editBtn.setDisable(selectedItem == null);
                    } else if (event.getButton() == MouseButton.SECONDARY) {
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
                } else if (event.getClickCount() == 2
                        && !tableRow.isEmpty()
                        && !(event.getTarget() instanceof CheckBoxTableCell)) {
                    updateMember();
                }
            });
            return tableRow;
        });

        dateFormatter = DateTimeFormatter.ofPattern(getMessage("global.date.format"));

        colBirthday.setCellValueFactory(param -> {
            SimpleStringProperty property = new SimpleStringProperty();
            final LocalDate birthday = param.getValue().getBirthday();
            if (birthday != null) {
                property.setValue(dateFormatter.format(birthday));
            }

            return property;
        });

        mapDegrees = degreeService.getAll().stream().collect(Collectors.toMap(DegreeDto::getCode, dto -> dto));
        colDegree.setCellValueFactory(param -> {
            final String degreeCode = param.getValue().getDegreeCode();
            String value;
            if (mapDegrees.containsKey(degreeCode)) {
                value = mapDegrees.get(degreeCode).getLabel();
            } else {
                value = degreeCode;
            }

            return new SimpleStringProperty(value);
        });

        mapOptions = optionsService.getAll().stream().collect(Collectors.toMap(OptionDto::getCode, dto -> dto));
        colOption.setCellValueFactory(param -> {
            final String optionCode = param.getValue().getOptionCode();
            String value;
            if (mapOptions.containsKey(optionCode)) {
                value = mapOptions.get(optionCode).getLabel();
            } else {
                value = optionCode;
            }

            return new SimpleStringProperty(value);
        });

        colSubscription.setCellFactory(param -> new CheckBoxTableCell<>(index -> {
            final MemberDto memberDto = filteredData.get(index);
            memberDto.subscriptionProperty().addListener((obs, wasSelected, isSelected) -> {
                membersService.updateData(memberDto);
            });
            return memberDto.subscriptionProperty();
        }));
        colSubscription.setEditable(true);
        colAnnals.setCellFactory(param -> new CheckBoxTableCell<>(index -> {
            final MemberDto memberDto = filteredData.get(index);
            memberDto.annalsProperty().addListener((obs, wasSelected, isSelected) -> {
                membersService.updateData(memberDto);
            });
            return memberDto.annalsProperty();
        }));
        colAnnals.setEditable(true);

        initFilters();
    }

    private static final Map<ObservableValue<? extends String>, Function<MemberDto, String>> GET_VALUES = new HashMap<>();
    private static final Map<ObservableValue<? extends String>, String> INPUTS = new HashMap<>();

    private void initFilters() {
        tfStudentNumber.prefWidthProperty().bind(colStudentNumber.widthProperty());
        addFilter(tfStudentNumber, MemberDto::getStudentNumber);

        tfLastname.prefWidthProperty().bind(colLastname.widthProperty());
        addFilter(tfLastname, MemberDto::getLastname);

        tfFirstname.prefWidthProperty().bind(colFirstname.widthProperty());
        addFilter(tfFirstname, MemberDto::getFirstname);

        tfEmail.prefWidthProperty().bind(colEmail.widthProperty());
        addFilter(tfEmail, MemberDto::getEmail);

        MaskHelper.addMask(tfPhone, MaskHelper.MASK_PHONE);
        tfPhone.prefWidthProperty().bind(colPhone.widthProperty());
        addFilter(tfPhone, MemberDto::getPhone);

        MaskHelper.addMask(tfBirthday, MaskHelper.MASK_DATE);
        tfBirthday.prefWidthProperty().bind(colBirthday.widthProperty());
        addFilter(tfBirthday, dto -> {
            if (dto.getBirthday() == null) {
                return "";
            }
            return dto.getBirthday().format(dateFormatter);
        });

        tfDegree.prefWidthProperty().bind(colDegree.widthProperty());
        addFilter(tfDegree, dto -> {
            if (mapDegrees.containsKey(dto.getDegreeCode())) {
                return mapDegrees.get(dto.getDegreeCode()).getLabel();
            } else {
                return dto.getDegreeCode();
            }
        });

        tfOption.prefWidthProperty().bind(colOption.widthProperty());
        addFilter(tfOption, dto -> {
            if (mapOptions.containsKey(dto.getOptionCode())) {
                return mapOptions.get(dto.getOptionCode()).getLabel();
            } else {
                return dto.getOptionCode();
            }
        });

        String yes = getMessage("global.yes");
        String no = getMessage("global.no");
        List<String> tmp = Arrays.asList(
                yes,
                no,
                getMessage("global.none"));

        cbSubscription.setItems(FXCollections.observableArrayList(tmp));
        cbSubscription.prefWidthProperty().bind(colSubscription.widthProperty());
        GET_VALUES.put(cbSubscription.valueProperty(), dto -> String.valueOf(dto.getSubscription()));
        cbSubscription.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (yes.equals(newValue)) {
                newValue = String.valueOf(true);
            } else if (no.equals(newValue)) {
                newValue = String.valueOf(false);
            } else {
                newValue = "";
            }

            INPUTS.put(observable, newValue);
            applyFilters(observable);
        });

        cbAnnals.prefWidthProperty().bind(colAnnals.widthProperty());
        cbAnnals.setItems(FXCollections.observableArrayList(tmp));
        GET_VALUES.put(cbAnnals.valueProperty(), dto -> String.valueOf(dto.getAnnals()));
        cbAnnals.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (yes.equals(newValue)) {
                newValue = String.valueOf(true);
            } else if (no.equals(newValue)) {
                newValue = String.valueOf(false);
            } else {
                newValue = "";
            }

            INPUTS.put(observable, newValue);
            applyFilters(observable);
        });
    }

    private void addFilter(TextField textField, Function<MemberDto, String> getValue) {

        GET_VALUES.put(textField.textProperty(), getValue);

        textField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            INPUTS.put(observable, newValue);

            applyFilters(observable);
        });
    }

    private void applyFilters(ObservableValue<? extends String> observable) {
        filteredData.setPredicate(dto -> {
            boolean result = true;
            for (Map.Entry<ObservableValue<? extends String>, String> entry : INPUTS.entrySet()) {
                // Current field is empty, it's ok
                result &= (StringUtils.isBlank(entry.getValue()) && entry.getKey().equals(observable))
                        || GET_VALUES.get(entry.getKey()).apply(dto).toLowerCase().contains(entry.getValue().toLowerCase());
            }

            return result;
        });
    }

    private void removeMember(MemberDto selectedItem) {
        membersService.deleteData(selectedItem);
        data.remove(selectedItem);
    }

    public void addMember() {
        Stage dialog = openDialog("/fxml/member.fxml", new Consumer<MemberController>() {
            @Override
            public void accept(MemberController memberController) {
                memberController.newData(data);
            }
        });

        dialog.setTitle(getMessage("member.create.title"));
        dialog.setWidth(330);
        dialog.setHeight(350);
        dialog.setResizable(false);

        dialog.show();
    }

    public void updateMember() {
        Stage dialog = openDialog("/fxml/member.fxml", new Consumer<MemberController>() {
            @Override
            public void accept(MemberController memberController) {
                memberController.updateData(data, tableView.getSelectionModel().getSelectedIndex());
            }
        });

        dialog.setTitle(getMessage("member.update.title"));
        dialog.setWidth(330);
        dialog.setHeight(350);
        dialog.setResizable(false);

        dialog.show();
    }

    public void exportCSV() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV, XLS, XLSX", "*.csv", "*.xls", "*.xlsx"));
        File file = fileChooser.showSaveDialog(getParent());
        if (file == null) {
            return;
        }

        String[] headers = {
                getMessage("member.student.number"),
                getMessage("member.lastname"),
                getMessage("member.firstname"),
                getMessage("member.birthday"),
                getMessage("member.email"),
                getMessage("member.degree"),
                getMessage("member.option"),
                getMessage("member.phone")
        };

        CSVFormat csvFormat = CSVFormat.EXCEL
                .withDelimiter(';')
                .withHeader(headers);

        try (final OutputStream outputStream = new FileOutputStream(file);
             final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
             final CSVPrinter csvPrinter = new CSVPrinter(out, csvFormat)) {

            filteredData.stream().map(MemberDto::toCSV).forEach(csv -> {
                try {
                    csvPrinter.printRecord(csv);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
            showInformation("export.success");
        } catch (IOException e) {
            showError(e);
        }

    }

    public void showHideFilters() {
        hbTop.setVisible(!hbTop.isVisible());
        hbTop.setManaged(!hbTop.isManaged());
    }
}
