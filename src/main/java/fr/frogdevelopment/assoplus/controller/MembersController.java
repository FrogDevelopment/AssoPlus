/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import fr.frogdevelopment.assoplus.utils.ApplicationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.service.MembersService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MembersController implements Initializable {

    private ResourceBundle resources;

    @Autowired
    private MembersService membersService;

    @FXML
    private TableView<MemberDto> table;
    private ObservableList<MemberDto> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resources = resources;

        data = FXCollections.observableArrayList(membersService.getAll());
        table.setItems(data);
    }

    public void importMembers(MouseEvent event) {
        Button source = (Button) (event.getSource());
        Window parent = source.getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(resources.getString("member.import.title"));
        File file = fileChooser.showOpenDialog(parent);

        if (file != null) {
            membersService.importMembers(file);
        }
    }

    public void manageMember(MouseEvent event) {
        Button source = (Button) (event.getSource());
        Window parent = source.getScene().getWindow();

        Stage dialog = ApplicationUtils.openDialog(parent, "/fxml/members/member.fxml", new Consumer<MemberController>() {
            @Override
            public void accept(MemberController memberController) {
                memberController.setData(data, table.getSelectionModel().getSelectedItem());
            }
        });

        dialog.setTitle(resources.getString("member.title"));
        dialog.setWidth(450);
        dialog.setHeight(450);

//        dialog.setOnCloseRequest(event1 -> setLicences());

        dialog.show();
    }

    public void manageDegrees(MouseEvent event) {
        Button source = (Button) (event.getSource());
        Window parent = source.getScene().getWindow();

        Stage dialog = ApplicationUtils.openDialog(parent, "/fxml/members/degrees.fxml");
        dialog.setTitle(resources.getString("member.degrees"));
        dialog.setWidth(450);
        dialog.setHeight(450);

        dialog.show();
    }
}
