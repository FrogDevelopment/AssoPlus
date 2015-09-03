/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.service.MembersService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
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

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportMembersController extends AbstractCustomDialogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportMembersController.class);

    @Autowired
    private MembersService membersService;

    @FXML
    private Parent parent;

    @FXML
    private GridPane top;
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
    }

    public void importMembers() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(getMessage("member.import.title"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV, XLS, XLSX", "*.csv", "*.xls", "*.xlsx"));
        File file = fileChooser.showOpenDialog(getParent());

        if (file != null) {
            String filename = file.getName();
            String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

            if (extension.equals("csv")
                    || extension.equals("xls")
                    || extension.equals("xlsx")) {
                importMembers(file);
            } else {
                showError("global.warning.header", "Format du fichier incorrect");
            }
        }
    }


    private void importMembers(File file) {
        Map<String, String> mapping = getMapping();

        char delimiter = ',';

        final CSVFormat csvFormat = CSVFormat.EXCEL
                .withHeader()
                .withSkipHeaderRecord()
                .withDelimiter(delimiter);

        data = FXCollections.observableArrayList();
        try (final InputStream is = new FileInputStream(file);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
             final CSVParser parser = new CSVParser(reader, csvFormat)) {

            MemberDto memberDto;
            for (CSVRecord line : parser) {
                memberDto = new MemberDto();

                String studentNumber = line.get(mapping.get("student_number"));
                memberDto.setStudentNumber(studentNumber);
                if (StringUtils.isNotEmpty(studentNumber)) {
                    memberDto.setLastname(line.get(mapping.get("last_name")));
                    memberDto.setFirstname(line.get(mapping.get("first_name")));

                    if (mapping.containsKey("first_name")) {
                    } else {
                        memberDto.setFirstname("");
                    }

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

    public void importData(ActionEvent actionEvent) {
        membersService.saveAll(data);
    }
}
