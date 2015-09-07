/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.components.controls.MaskHelper;
import fr.frogdevelopment.assoplus.components.controls.Validator;
import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.service.MembersService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isAnyBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportMembersController extends AbstractCustomDialogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportMembersController.class);

    @Autowired
    private MembersService membersService;

    @FXML
    private Label labelFileName;
    @FXML
    private TextField tfDelimiter;
    @FXML
    private Button btnHeaders;
    @FXML
    private HBox hboxTest;
    @FXML
    private Label lbStudentNumber;
    @FXML
    private TextField tfStudentNumber;
    @FXML
    private Label lbLastname;
    @FXML
    private TextField tfLastname;
    @FXML
    private Label lbFirstname;
    @FXML
    private TextField tfFirstname;
    @FXML
    private Label lbBirthday;
    @FXML
    private TextField tfBirthday;
    @FXML
    private Label lbEmail;
    @FXML
    private TextField tfEmail;
    @FXML
    private Label lbPhone;
    @FXML
    private TextField tfPhone;
    @FXML
    private Label lbDegree;
    @FXML
    private TextField tfDegree;
    @FXML
    private Label lbOption;
    @FXML
    private TextField tfOption;
    @FXML
    private Button btnLoadData;

    @FXML
    private TableView<MemberDto> tableView;
    @FXML
    private TableColumn<MemberDto, Boolean> selectCol;
    @FXML
    private ToolBar toolBar;

    private File file;
    private ObservableList<MemberDto> data;

    @Override
    protected void initialize() {
        tableView.setVisible(false);
        tableView.setManaged(false);
        toolBar.setVisible(false);
        toolBar.setManaged(false);

        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
        CheckBox checkBoxAll = new CheckBox();
        checkBoxAll.setSelected(true);
        checkBoxAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
            data.forEach(dto -> dto.setSelected(newValue));
        });
        selectCol.setGraphic(checkBoxAll);
        tableView.setEditable(true);

        MaskHelper.addTextLimiter(tfDelimiter, 1);
        tfDelimiter.setText(",");

        manageDrop(tfStudentNumber);
        manageDrop(tfLastname);
        manageDrop(tfFirstname);
        manageDrop(tfBirthday);
        manageDrop(tfEmail);
        manageDrop(tfPhone);
        manageDrop(tfDegree);
        manageDrop(tfOption);
    }

    private void manageDrop(TextField textField) {
        textField.setOnDragOver(event -> {
            /* accept it only if it is not dragged from the same node
            /* and if it has a string data */
            if (event.getGestureSource() != textField
                    && event.getDragboard().hasString()) {
            /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.COPY);
            }

            event.consume();
        });

        textField.setOnDragEntered(event -> {
            if (event.getGestureSource() != textField && event.getDragboard().hasString()) {
                textField.setStyle("-fx-border-color: green");
            }

            event.consume();
        });

        textField.setOnDragExited(event -> {
            if (event.getGestureSource() != textField && event.getDragboard().hasString()) {
                textField.setStyle("-fx-border-color: null");
            }

            event.consume();
        });

        textField.setOnDragDropped(event -> {
            /* if there is a string data on dragboard, read it and use it */
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                textField.setText(db.getString());
                success = true;
            }
            /* let the source know whether the string was successfully transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });
    }

    public void selectFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(getMessage("member.import.title"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV, XLS, XLSX", "*.csv", "*.xls", "*.xlsx"));
        file = fileChooser.showOpenDialog(getParent());

        if (file == null) {
            labelFileName.setText(null);
            btnHeaders.setDisable(true);
            btnLoadData.setDisable(true);
        } else {
            labelFileName.setText(file.getName());
            String filename = file.getName();
            String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

            if (extension.equals("csv")
                    || extension.equals("xls")
                    || extension.equals("xlsx")) {
                btnHeaders.setDisable(false);
                btnLoadData.setDisable(false);
            } else {
                showError("global.warning.header", "import.file.incorrect");
            }
        }
    }

    public void findHeaders() {
        if (!Validator.validateNotBlank(tfDelimiter)) {
            showWarning("global.warning.msg.check");
            return;
        }

        char delimiter = tfDelimiter.getText().charAt(0);

        CSVFormat format = CSVFormat.EXCEL
                .withDelimiter(delimiter)
                .withHeader()
                .withSkipHeaderRecord();

        try (final InputStream is = new FileInputStream(file);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
             final CSVParser parser = new CSVParser(reader, format)) {

            final Map<String, Integer> headerMap = parser.getHeaderMap();
            hboxTest.getChildren().clear();
            headerMap.keySet().forEach(this::addDraggableHeader);

//            final long recordNumber = parser.getRecordNumber(); todo à exploiter ?

        } catch (Exception e) {
            LOGGER.error("Error during import", e);
            showError("global.error.header", e);
        }
    }

    private void addDraggableHeader(String header) {
        final Text source = new Text(header);

        source.setOnMouseEntered(e -> source.setCursor(Cursor.OPEN_HAND));
        source.setOnMousePressed(e -> source.setCursor(Cursor.CLOSED_HAND));
        source.setOnMouseReleased(e -> source.setCursor(Cursor.DEFAULT));

        source.setOnDragDetected(event -> {

            /* drag was detected, start a drag-and-drop gesture*/
            /* allow any transfer mode */
            Dragboard db = source.startDragAndDrop(TransferMode.COPY);

            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            db.setDragView(source.snapshot(snapshotParameters, null));

            /* Put a string on a dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString(source.getText());
            db.setContent(content);

            event.consume();
        });

//        source.setOnDragDone(event -> {
//
//            /* the drag and drop gesture ended */
//            /* if the data was successfully moved, clear it */
//            if (event.getTransferMode() == TransferMode.COPY) {
//                source.setText("");
//            }
//            event.consume();
//        });
        hboxTest.getChildren().add(source);

        Separator separator = new Separator(Orientation.VERTICAL);
        hboxTest.getChildren().add(separator);
    }

    public void importMembers() {
        if (!Validator.validateNoneBlank(tfDelimiter, tfStudentNumber, tfLastname, tfFirstname)) {
            showWarning("global.warning.msg.check");
            return;
        }

        Map<String, String> mapping = fetchHeaderMapping();

        char delimiter = tfDelimiter.getText().charAt(0);
        CSVFormat format = CSVFormat.EXCEL
                .withDelimiter(delimiter)
                .withHeader()
                .withSkipHeaderRecord();

        data = FXCollections.observableArrayList();

        try (final InputStream is = new FileInputStream(file);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             final CSVParser parser = new CSVParser(reader, format)) {

            StringBuilder sb = new StringBuilder();
            boolean incorrectHeaderPresent = false;
            final Map<String, Integer> headerMap = parser.getHeaderMap();
            for (String s : mapping.values()) {
                if (headerMap.containsKey(s)) {
                    continue;
                }

                incorrectHeaderPresent = true;
                sb.append("\t- ").append(s).append("\n");
            }

            if (incorrectHeaderPresent) {
                showWarning("import.error.incorrect.header", sb.toString());
                return;
            }

            MemberDto memberDto;
            for (CSVRecord line : parser) {
                memberDto = new MemberDto();

                String studentNumber = line.get(mapping.get("member.student.number"));
                String lastName = line.get(mapping.get("member.lastname"));
                String firstName = line.get(mapping.get("member.firstname"));

                // check if the 3 required fiels are presents
                if (isAnyBlank(studentNumber, lastName, firstName)) {
                    // todo infor user ?
                    continue;
                }

                memberDto.setStudentNumber(studentNumber);
                memberDto.setLastname(lastName);
                memberDto.setFirstname(firstName);

                if (mapping.containsKey("member.degree")) {
                    memberDto.setDegreeCode(line.get(mapping.get("member.degree")).trim());
                } else {
                    memberDto.setDegreeCode("");
                }

                if (mapping.containsKey("member.option")) {
                    memberDto.setOptionCode(line.get(mapping.get("member.option")).trim());
                } else {
                    memberDto.setOptionCode("");
                }

                if (mapping.containsKey("member.birthday")) {
                    memberDto.setBirthday(line.get(mapping.get("member.birthday")).trim());
                } else {
                    memberDto.setBirthday("");
                }

                if (mapping.containsKey("member.email")) {
                    memberDto.setEmail(line.get(mapping.get("member.email")).trim());
                } else {
                    memberDto.setEmail("");
                }

                if (mapping.containsKey("member.phone")) {
                    memberDto.setPhone(line.get(mapping.get("member.phone")).trim());
                }

                memberDto.setSelected(true);
                data.add(memberDto);
            }

        } catch (Exception e) {
            LOGGER.error("Error during import", e);
            showError("global.error.header", e);
        }

        tableView.setItems(data);
        tableView.setVisible(true);
        tableView.setManaged(true);

        toolBar.setVisible(true);
        toolBar.setManaged(true);

        getParent().setHeight(600);
    }

    private Map<String, String> fetchHeaderMapping() {
        Map<String, String> mapping = new HashMap<>();

        // REQUIRED
        mapping.put("member.student.number", tfStudentNumber.getText());
        mapping.put("member.lastname", tfLastname.getText());
        mapping.put("member.firstname", tfFirstname.getText());

        // OPTIONNALS (but better if present)
        if (isNotBlank(tfBirthday.getText())) {
            mapping.put("member.birthday", tfBirthday.getText());
        }

        if (isNotBlank(tfEmail.getText())) {
            mapping.put("member.email", tfEmail.getText());
        }

        if (isNotBlank(tfDegree.getText())) {
            mapping.put("member.degree", tfDegree.getText());
        }

        if (isNotBlank(tfOption.getText())) {
            mapping.put("member.option", tfOption.getText());
        }

        if (isNotBlank(tfPhone.getText())) {
            mapping.put("member.phone", tfPhone.getText());
        }

        return mapping;
    }

    public void saveSelectedData() {

        final Map<String, MemberDto> mapOnBaseByStudentNumber = membersService.getAll()
                .stream()
                .collect(Collectors.toMap(MemberDto::getStudentNumber, Function.identity()));

        Map<String, MemberDto> mapToSaveByStudentNumber = data
                .stream()
                .filter(MemberDto::getSelected)
                .collect(Collectors.toMap(MemberDto::getStudentNumber, Function.identity()));

        mapOnBaseByStudentNumber.keySet().retainAll(mapToSaveByStudentNumber.keySet());

        if (mapOnBaseByStudentNumber.keySet().size() > 0) {
            ButtonType overrideBtn = new ButtonType(getMessage("import.data.override"), ButtonBar.ButtonData.YES);
            ButtonType ignoreBtn = new ButtonType(getMessage("import.data.ignore"), ButtonBar.ButtonData.NO);

            Optional<ButtonType> response = showCustomConfirmation(
                    "import.data.already.present.header",
                    "import.data.already.present.message",
                    overrideBtn, ignoreBtn);

            if (overrideBtn.equals(response.get())) {
                mapOnBaseByStudentNumber.entrySet().forEach(entry -> {
                    MemberDto toSave = mapToSaveByStudentNumber.get(entry.getKey());
                    MemberDto toOverride = entry.getValue();
                    toSave.setId(toOverride.getId());
                    toSave.setSubscription(toOverride.getSubscription());
                    toSave.setAnnals(toOverride.getAnnals());
                });
            } else {
                mapOnBaseByStudentNumber.keySet().forEach(mapToSaveByStudentNumber::remove);
            }
        }

        membersService.saveOrUpdateAll(mapToSaveByStudentNumber.values());

        close();
    }
}
