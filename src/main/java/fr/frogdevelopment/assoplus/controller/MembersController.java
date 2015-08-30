/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.service.MembersService;
import fr.frogdevelopment.assoplus.utils.ApplicationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.function.Consumer;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MembersController extends AbstractCustomController {

    @Autowired
    private MembersService membersService;

    @FXML
    private TableView<MemberDto> table;

    @FXML
    private Button btnCreateOrUpdate;

    private MemberDto selectedItem;

    @Override
    protected void initialize() {
        ObservableList<MemberDto> data = FXCollections.observableArrayList(membersService.getAll());
        table.setItems(data);

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, dto) -> {
            selectedItem = dto;
            if (selectedItem != null) {
                btnCreateOrUpdate.setText(getMessage("member.update.title"));
            } else {
                btnCreateOrUpdate.setText(getMessage("member.create.title"));
            }
        });
    }

    public void importMembers(MouseEvent event) {
        Window parent = getParent(event);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(getMessage("member.import.title"));
        File file = fileChooser.showOpenDialog(parent);

        if (file != null) {
            membersService.importMembers(file);
        }
    }

    public void manageMember(MouseEvent event) {
        Window parent = getParent(event);

        Stage dialog = ApplicationUtils.openDialog(parent, "/fxml/members/member.fxml", new Consumer<MemberController>() {
            @Override
            public void accept(MemberController memberController) {
                memberController.setData(table.getItems(), selectedItem);
            }
        });

        if (selectedItem != null) {
            dialog.setTitle(getMessage("member.update.title"));
        } else {
            dialog.setTitle(getMessage("member.create.title"));
        }

        dialog.setWidth(450);
        dialog.setHeight(450);

//        dialog.setOnCloseRequest(event1 -> setLicences());

        dialog.show();
    }

    public void manageDegrees(MouseEvent event) {
        Window parent = getParent(event);

        Stage dialog = ApplicationUtils.openDialog(parent, "/fxml/members/degrees.fxml");
        dialog.setTitle(getMessage("member.degrees"));
        dialog.setWidth(450);
        dialog.setHeight(450);

        dialog.show();
    }
}
