/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

import fr.frogdevelopment.assoplus.core.controller.AbstractCustomController;
import fr.frogdevelopment.assoplus.core.controls.MaskHelper;
import fr.frogdevelopment.assoplus.member.dto.Member;
import fr.frogdevelopment.assoplus.member.service.MembersService;

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

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MembersController extends AbstractCustomController {

    @Autowired
    private MembersService membersService;

    @FXML
    private TableView<Member> tableView;
    @FXML
    private TableColumn<Member, String> colStudentNumber;
    @FXML
    private TableColumn<Member, String> colLastname;
    @FXML
    private TableColumn<Member, String> colFirstname;
    @FXML
    private TableColumn<Member, String> colPhone;
    @FXML
    private TableColumn<Member, String> colBirthday;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn<Member, String> colDegree;
    @FXML
    private TableColumn<Member, String> colOption;
    @FXML
    private TableColumn<Member, Boolean> colSubscription;
    @FXML
    private TableColumn<Member, Boolean> colAnnals;

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

    private FilteredList<Member> filteredData;
    private DateTimeFormatter dateFormatter;
    private ObservableList<Member> data;

    @Override
    protected void initialize() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all filteredData).
        data = FXCollections.observableArrayList(membersService.getAll());
        filteredData = new FilteredList<>(data, p -> true);

        // 2. Set the filter Predicate whenever the filters changes.
        initFilters();

        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Member> sortedData = filteredData.sorted();

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tableView.setItems(sortedData);

        tableView.setEditable(true);

        tableView.setRowFactory(param -> {
            TableRow<Member> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !tableRow.isEmpty()) {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        Member selectedItem = tableView.getSelectionModel().getSelectedItem();
                        editBtn.setDisable(selectedItem == null);
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        final ContextMenu contextMenu = new ContextMenu();
                        MenuItem deleteItem = new MenuItem(getMessage("global.delete"));
                        deleteItem.setGraphic(new ImageView(new Image("/img/delete_16.png")));
                        deleteItem.setOnAction(e -> {
                            Member selectedItem = tableView.getSelectionModel().getSelectedItem();
                            // FIXME
                            showYesNoDialog(String.format(getMessage("global.confirm.delete"), "l'étudiant " + selectedItem.getStudentNumber()), o -> removeMember(selectedItem));
                        });
                        contextMenu.getItems().add(deleteItem);

                        MenuItem updateItem = new MenuItem(getMessage("global.update"));
                        updateItem.setGraphic(new ImageView(new Image("/img/edit_user_16.png")));
                        updateItem.setOnAction(e -> updateMember());
                        contextMenu.getItems().add(updateItem);

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

        colSubscription.setCellFactory(param -> new CheckBoxTableCell<>(index -> {
            final Member memberDto = filteredData.get(index);
            memberDto.subscriptionProperty().addListener((obs, wasSelected, isSelected) -> Platform.runLater(() -> membersService.updateSubscription(memberDto)));
            return memberDto.subscriptionProperty();
        }));
        colSubscription.setEditable(true);
        colAnnals.setCellFactory(param -> new CheckBoxTableCell<>(index -> {
            final Member memberDto = filteredData.get(index);
            memberDto.annalsProperty().addListener((obs, wasSelected, isSelected) -> Platform.runLater(() -> membersService.updateAnnals(memberDto)));
            return memberDto.annalsProperty();
        }));
        colAnnals.setEditable(true);

        initFilters();
    }

    private static final Map<ObservableValue<? extends String>, Function<Member, String>> GET_VALUES = new HashMap<>();
    private static final Map<ObservableValue<? extends String>, String> INPUTS = new HashMap<>();

    private void initFilters() {
        tfStudentNumber.prefWidthProperty().bind(colStudentNumber.widthProperty());
        addFilter(tfStudentNumber, Member::getStudentNumber);

        tfLastname.prefWidthProperty().bind(colLastname.widthProperty());
        addFilter(tfLastname, Member::getLastname);

        tfFirstname.prefWidthProperty().bind(colFirstname.widthProperty());
        addFilter(tfFirstname, Member::getFirstname);

        tfEmail.prefWidthProperty().bind(colEmail.widthProperty());
        addFilter(tfEmail, Member::getEmail);

        MaskHelper.addMask(tfPhone, MaskHelper.MASK_PHONE);
        tfPhone.prefWidthProperty().bind(colPhone.widthProperty());
        addFilter(tfPhone, Member::getPhone);

        MaskHelper.addMask(tfBirthday, MaskHelper.MASK_DATE);
        tfBirthday.prefWidthProperty().bind(colBirthday.widthProperty());
        addFilter(tfBirthday, dto -> {
            if (dto.getBirthday() == null) {
                return "";
            }
            return dto.getBirthday().format(dateFormatter);
        });

        tfDegree.prefWidthProperty().bind(colDegree.widthProperty());
        addFilter(tfDegree, Member::getDegreeLabel);

        tfOption.prefWidthProperty().bind(colOption.widthProperty());
        addFilter(tfOption, Member::getOptionLabel);

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

    private void addFilter(TextField textField, Function<Member, String> getValue) {

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

    private void removeMember(Member selectedItem) {
        membersService.deleteData(selectedItem);
        data.remove(selectedItem);
    }

    public void addMember() {
        Stage dialog = openDialog("member.fxml", (Consumer<MemberController>) memberController -> memberController.newData(data));

        dialog.setTitle(getMessage("member.create.title"));
        dialog.setWidth(330);
        dialog.setHeight(350);
        dialog.setResizable(false);

        dialog.show();
    }

    public void updateMember() {
        Stage dialog = openDialog("member.fxml", (Consumer<MemberController>) memberController -> memberController.updateData(data, tableView.getSelectionModel().getSelectedIndex()));

        dialog.setTitle(getMessage("member.update.title"));
        dialog.setWidth(330);
        dialog.setHeight(350);
        dialog.setResizable(false);

        dialog.show();
    }

    public void importCSV() {
        Stage dialog = openDialog("import_members.fxml");
        dialog.setTitle(getMessage("import.title"));
        dialog.setWidth(800);
        dialog.setHeight(200);

        dialog.setOnCloseRequest(event -> initialize()); // fixme just refresh data

        dialog.show();
    }

    public void exportCSV() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(getMessage("member.export.title"));
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

            filteredData.stream().map(Member::toCSV).forEach(csv -> {
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

    public void manageDegrees() {
        Stage dialog = openDialog("degrees.fxml");
        dialog.setTitle(getMessage("member.degrees"));
        dialog.setWidth(550);
        dialog.setHeight(400);

        dialog.show();
    }

//    private Animation showAnimation;
//    private Animation hideAnimation;

    public void showHideFilters() {
        hbTop.setVisible(!hbTop.isVisible());
        hbTop.setManaged(!hbTop.isManaged());

        // TODO
//        if (showAnimation == null) {
//            showAnimation = new Transition() {
//                {
//                    setCycleDuration(Duration.millis(250));
//                }
//
//                @Override
//                protected void interpolate(double frac) {
//                    final double curWidth = child.getWidth() * frac;
//                    hbTop.setPrefWidth(curWidth);
//                    hbTop.setTranslateX(-child.getWidth() + curWidth);
//                }
//            };
//        }
//
//        if (hideAnimation == null) {
//            hideAnimation = new Transition() {
//                {
//                    setCycleDuration(Duration.millis(250));
//                }
//
//                @Override
//                protected void interpolate(double frac) {
//                    final double curWidth = child.getWidth() * (1.0 - frac);
//                    hbTop.setPrefWidth(curWidth);
//                    hbTop.setTranslateX(-child.getWidth() + curWidth);
//                }
//            };
//
//            hideAnimation.onFinishedProperty().set(actionEvent -> hbTop.setVisible(false));
//        }
//
//        if (showAnimation.getStatus() == Animation.Status.STOPPED
//                && hideAnimation.getStatus() == Animation.Status.STOPPED) {
//            if (hbTop.isVisible()) {
//                hideAnimation.play();
//            } else {
//                hbTop.setVisible(true);
//                showAnimation.play();
//            }
//        }
    }
}
