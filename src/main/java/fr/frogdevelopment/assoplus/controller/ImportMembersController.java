/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.service.MembersService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportMembersController extends AbstractCustomDialogController {

    @Autowired
    private MembersService membersService;

    @FXML
    private TableView<MemberDto> table;

    @Override
    protected void initialize() {

    }

    public void importMembers(MouseEvent event) {
        Window parent = getParent(event);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(getMessage("member.import.title"));
        File file = fileChooser.showOpenDialog(parent);

        if (file != null) {
            importMembers(file);
        }
    }


    private void importMembers(File file) {

        Map<String, String> mapping = new HashMap<>();
        mapping.put("student_number", "NUMERO ETUDIANT");
        mapping.put("last_name", "NOM");
        mapping.put("first_name", "PRENOM");
        mapping.put("degree", "DEGRES");
        mapping.put("option", "OPTION");
        mapping.put("email", "E-MAIL");
        mapping.put("phone", "TELEPHONE");

        char delimiter = ',';

        final CSVFormat csvFormat = CSVFormat.EXCEL
                .withHeader()
                .withSkipHeaderRecord()
                .withDelimiter(delimiter);

        ObservableList<MemberDto> data = FXCollections.observableArrayList();
        try (final InputStream is = new FileInputStream(file);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
             final CSVParser parser = new CSVParser(reader, csvFormat)) {

            parser.forEach(line -> {
                MemberDto memberDto = new MemberDto();

                String studentNumber = line.get(mapping.get("student_number"));
                if (StringUtils.isNotEmpty(studentNumber)) {

                    memberDto.setStudentNumber(studentNumber);

                    if (mapping.containsKey("last_name")) {
                        memberDto.setLastname(line.get(mapping.get("last_name")));
                    } else {
                        memberDto.setLastname("");
                    }

                    if (mapping.containsKey("first_name")) {
                        memberDto.setFirstname(line.get(mapping.get("first_name")));
                    } else {
                        memberDto.setFirstname("");
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

                    data.add(memberDto);
                }
            });

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        table.setItems(data);
    }
}
