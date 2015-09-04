/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.service.MembersService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportMembersController extends AbstractCustomDialogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportMembersController.class);

    @FXML
    private HBox hboxTest;

    @Autowired
    private MembersService membersService;

    @FXML
    private Parent parent;

    @FXML
    private GridPane top;
    @FXML
    private Label labelFileName;
    @FXML
    private CheckBox cbLastname;
    @FXML
    private TextField tfLastname;
    @FXML
    private CheckBox cbFirstname;
    @FXML
    private TextField tfFirstname;
    @FXML
    private CheckBox cbStudentNumber;
    @FXML
    private TextField tfStudentNumber;
    @FXML
    private CheckBox cbBirthday;
    @FXML
    private TextField tfBirthday;
    @FXML
    private CheckBox cbEmail;
    @FXML
    private TextField tfEmail;
    @FXML
    private CheckBox cbPhone;
    @FXML
    private TextField tfPhone;
    @FXML
    private CheckBox cbDegree;
    @FXML
    private TextField tfDegree;
    @FXML
    private CheckBox cbOption;
    @FXML
    private TextField tfOption;

    @FXML
    private TableView<MemberDto> tableView;
    @FXML
    private TableColumn<MemberDto, Boolean> selectCol;
    @FXML
    private ToolBar toolBar;
    private ObservableList<MemberDto> data;

    private File file;

    @Override
    protected void initialize() {

        top.setVisible(true);
        top.setManaged(true);

        tableView.setVisible(false);
        tableView.setManaged(false);
        toolBar.setVisible(false);
        toolBar.setManaged(false);

        selectCol.setCellValueFactory(new PropertyValueFactory<>("selected"));
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
        selectCol.setEditable(true);
        tableView.setEditable(true);

        manageDrop(tfStudentNumber, cbStudentNumber);
        manageDrop(tfBirthday, cbBirthday);
        manageDrop(tfLastname, cbLastname);
        manageDrop(tfFirstname, cbFirstname);
        manageDrop(tfEmail, cbEmail);
        manageDrop(tfPhone, cbPhone);
        manageDrop(tfDegree, cbDegree);
        manageDrop(tfOption, cbOption);
    }

    private void manageDrop(TextField textField, CheckBox checkBox) {
        textField.setOnDragOver(event -> {
            /* data is dragged over the target */
            /* accept it only if it is not dragged from the same node
            /* and if it has a string data */
            if (event.getGestureSource() != textField
                    && event.getDragboard().hasString()
                    && checkBox.isSelected()) {
            /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.MOVE);
            }

            event.consume();
        });

        textField.setOnDragEntered(event -> {
            /* the drag-and-drop gesture entered the target */
            /* show to the user that it is an actual gesture target */
            if (event.getGestureSource() != textField && event.getDragboard().hasString()) {
//                    tfStudentNumber.setFill(Color.GREEN);
            }

            event.consume();
        });

        textField.setOnDragDropped(event -> {
            /* data dropped */
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

        if (file != null) {
            labelFileName.setText(file.getName());
            String filename = file.getName();
            String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
            if (!extension.equals("csv")
                    && !extension.equals("xls")
                    && !extension.equals("xlsx")) {
                showError("global.warning.header", "Format du fichier incorrect");
            }
        } else {
            labelFileName.setText(null);
        }
    }

    public void testHeader() {
        if (file == null) {
            return; // fixme
        }

        try (final InputStream is = new FileInputStream(file);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
             final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader().withSkipHeaderRecord())) {

            final Map<String, Integer> headerMap = parser.getHeaderMap();
            hboxTest.getChildren().clear();

            headerMap.keySet().forEach(s -> {
                final Text source = new Text(s);

                source.setOnMouseEntered(e -> source.setCursor(Cursor.HAND));
                source.setOnMousePressed(e -> {
                    source.setMouseTransparent(true);
                    source.setCursor(Cursor.CLOSED_HAND);
                });
                source.setOnMouseReleased(e -> {
                    source.setMouseTransparent(false);
                    source.setCursor(Cursor.DEFAULT);
                });

                source.setOnDragDetected(event -> {
                    /* drag was detected, start a drag-and-drop gesture*/
                    /* allow any transfer mode */
                    Dragboard db = source.startDragAndDrop(TransferMode.MOVE);

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
            });

//            final long recordNumber = parser.getRecordNumber(); todo à exploiter ?

        } catch (Exception e) {
            LOGGER.error("Error during import", e);
            showError("global.error.header", ExceptionUtils.getMessage(e));
        }
    }

    public void importMembers() {
        if (file == null) {
            return; // fixme
        }

        Map<String, String> mapping = getMapping();

        data = FXCollections.observableArrayList();
        try (final InputStream is = new FileInputStream(file);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader().withSkipHeaderRecord())) {

            MemberDto memberDto;
            for (CSVRecord line : parser) {
                memberDto = new MemberDto();

                String studentNumber = line.get(mapping.get("student_number"));
                memberDto.setStudentNumber(studentNumber);
                if (StringUtils.isNotEmpty(studentNumber)) {
                    memberDto.setLastname(line.get(mapping.get("last_name")));
                    memberDto.setFirstname(line.get(mapping.get("first_name")));

                    if (StringUtils.isBlank(memberDto.getFirstname()) && StringUtils.isBlank(memberDto.getLastname())) {
                        continue;
                    }

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

    private Map<String, String> getMapping() {
        Map<String, String> mapping = new HashMap<>();

        if (cbStudentNumber.isSelected() && StringUtils.isNoneBlank(tfStudentNumber.getText())) {
            mapping.put("student_number", tfStudentNumber.getText());
        }

        if (cbLastname.isSelected() && StringUtils.isNoneBlank(tfLastname.getText())) {
            mapping.put("last_name", tfLastname.getText());
        }

        if (cbFirstname.isSelected() && StringUtils.isNoneBlank(tfFirstname.getText())) {
            mapping.put("first_name", tfFirstname.getText());
        }

        if (cbDegree.isSelected() && StringUtils.isNoneBlank(tfDegree.getText())) {
            mapping.put("degree", tfDegree.getText());
        }

        if (cbOption.isSelected() && StringUtils.isNoneBlank(tfOption.getText())) {
            mapping.put("option", tfOption.getText());
        }

        if (cbEmail.isSelected() && StringUtils.isNoneBlank(tfEmail.getText())) {
            mapping.put("email", tfEmail.getText());
        }

        if (cbPhone.isSelected() && StringUtils.isNoneBlank(tfPhone.getText())) {
            mapping.put("phone", tfPhone.getText());
        }

        return mapping;
    }


    public void saveSelectedData() {
        membersService.saveAll(data.filtered(MemberDto::getSelected));
        close();
    }
}
