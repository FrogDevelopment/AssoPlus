/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.controlsfx.control.NotificationPane;

abstract class CreateUpdateDialogController<E> extends AbstractCustomController {

//    @FXML
//    protected Label lblError;

    @FXML
    protected HBox topBox;

    @FXML
    protected Button btnPrevious;
    @FXML
    protected Button btnSave;
    @FXML
    protected Button btnNew;
    @FXML
    protected Button btnNext;

    protected ObservableList<E> entities;
    protected E entityDto;
    protected int currentIndex;

    void newData(ObservableList<E> data) {
        this.entities = data;
        entityDto = newEntity();

        currentIndex = data.size() - 1;

        setData();

        handlePreviousNextButtons(data);
    }

    abstract protected E newEntity();

    void updateData(ObservableList<E> data, int index) {
        this.entities = data;
        entityDto = data.get(index);
        currentIndex = index;

        setData();

        handlePreviousNextButtons(data);
    }

    private void handlePreviousNextButtons(ObservableList<E> data) {
        btnPrevious.setDisable(currentIndex == 0);
        btnNext.setDisable(currentIndex == data.size() - 1);
    }

    abstract protected void setData();

    public void previousData() {
        if (currentIndex > 0) {
            updateData(entities, --currentIndex);
        }
    }

    public void saveData() {
        if(check()) {
            save();
        }else {
            notifyError();
        }
    }

    public void newData() {
        newData(entities);
    }

    public void nextData() {
        if (currentIndex < entities.size() - 1) {
            updateData(entities, ++currentIndex);
        }
    }

    abstract protected boolean check();
    abstract protected void save();

    private void notifyError() {
        NotificationPane notificationPane = (NotificationPane) child;
        notificationPane.setShowFromTop(false);
        notificationPane.setText(getMessage("global.warning.msg.check"));
        notificationPane.show();
    }
}
