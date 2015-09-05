/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import fr.frogdevelopment.assoplus.components.controls.Validator;
import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.service.MembersService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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

        selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
        selectCol.setEditable(true);
        tableView.setEditable(true);

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
                event.acceptTransferModes(TransferMode.MOVE);
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
                showError("global.warning.header", "Format du fichier incorrect"); // fixme use bundle
            }
        }
    }

    public void findHeaders() {
        try (final InputStream is = new FileInputStream(file);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
             final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader().withSkipHeaderRecord())) {

            final Map<String, Integer> headerMap = parser.getHeaderMap();
            hboxTest.getChildren().clear();

            headerMap.keySet().forEach(this::addDraggableHeader);

//            final long recordNumber = parser.getRecordNumber(); todo à exploiter ?

        } catch (Exception e) {
            LOGGER.error("Error during import", e);
            showError("global.error.header", ExceptionUtils.getMessage(e));
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
            Dragboard db = source.startDragAndDrop(TransferMode.ANY);

            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            db.setDragView(source.snapshot(snapshotParameters, null));

            /* Put a string on a dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString(source.getText());
            db.setContent(content);

            event.consume();
        });

        source.setOnDragDone(event -> {

            /* the drag and drop gesture ended */
            /* if the data was successfully moved, clear it */
            if (event.getTransferMode() == TransferMode.MOVE) {
                source.setText("");
            }
            event.consume();
        });
        hboxTest.getChildren().add(source);
    }

    public void importMembers() {
        if (!Validator.validateNoneBlank(tfStudentNumber, tfLastname, tfFirstname)) {
            return;
        }

        Map<String, String> mapping = fetchHeaderMapping();

        data = FXCollections.observableArrayList();
        try (final InputStream is = new FileInputStream(file);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader().withSkipHeaderRecord())) {

            MemberDto memberDto;
            for (CSVRecord line : parser) {
                memberDto = new MemberDto();

                String studentNumber = line.get(mapping.get("student_number"));
                String lastName = line.get(mapping.get("last_name"));
                String firstName = line.get(mapping.get("first_name"));

                // check if the 3 required fiels are presents
                if (isAnyBlank(studentNumber, lastName, firstName)) {
                    // todo infor user ?
                    continue;
                }

                memberDto.setStudentNumber(studentNumber);
                memberDto.setLastname(lastName);
                memberDto.setFirstname(firstName);

                if (mapping.containsKey("degree")) {
                    memberDto.setDegreeCode(line.get(mapping.get("degree")));
                } else {
                    memberDto.setDegreeCode("");
                }

                if (mapping.containsKey("option")) {
                    memberDto.setOptionCode(line.get(mapping.get("option")));
                } else {
                    memberDto.setOptionCode("");
                }

                if (mapping.containsKey("email")) {
                    memberDto.setEmail(line.get(mapping.get("email")));
                } else {
                    memberDto.setEmail("");
                }

                if (mapping.containsKey("phone")) {
                    memberDto.setPhone(line.get(mapping.get("phone")));
                }

                memberDto.setSelected(true);
                data.add(memberDto);
            }

        } catch (Exception e) {
            LOGGER.error("Error during import", e);
            showError("global.error.header", ExceptionUtils.getMessage(e));
        }

        tableView.setItems(data);

        tableView.setVisible(true);
        tableView.setManaged(true);
        toolBar.setVisible(true);
        toolBar.setManaged(true);
    }

    private Map<String, String> fetchHeaderMapping() {
        Map<String, String> mapping = new HashMap<>();

        // REQUIRED
        mapping.put("student_number", tfStudentNumber.getText());
        mapping.put("last_name", tfLastname.getText());
        mapping.put("first_name", tfFirstname.getText());

        // OPTIONNALS (but better if present)
        if (isNotBlank(tfDegree.getText())) {
            mapping.put("degree", tfDegree.getText());
        }

        if (isNotBlank(tfOption.getText())) {
            mapping.put("option", tfOption.getText());
        }

        if (isNotBlank(tfEmail.getText())) {
            mapping.put("email", tfEmail.getText());
        }

        if (isNotBlank(tfPhone.getText())) {
            mapping.put("phone", tfPhone.getText());
        }

        return mapping;
    }


    public void saveSelectedData() {
        membersService.saveAll(data.filtered(MemberDto::getSelected));
        close();
    }
}
