/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import org.controlsfx.control.NotificationPane;

public abstract class AbstractCreateUpdateDialogController<E> extends AbstractCustomController {

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

    public void newData(ObservableList<E> data) {
        this.entities = data;
        entityDto = newEntity();

        currentIndex = data.size();

        setData();

        btnPrevious.setDisable(currentIndex == 0);
        btnNext.setDisable(currentIndex == entities.size());
        clear();
    }

    abstract protected E newEntity();

    public void updateData(ObservableList<E> data, int index) {
        this.entities = data;
        entityDto = data.get(index);
        currentIndex = index;

        setData();

        btnPrevious.setDisable(currentIndex == 0);
        btnNext.setDisable(currentIndex == entities.size() - 1);
        clear();
    }

    abstract protected void setData();

    public void previousData() {
        if (currentIndex > 0) {
            updateData(entities, --currentIndex);
            clear();
        }
    }

    public void saveData() {
        if (check()) {
            save();
            btnPrevious.setDisable(currentIndex == 0);
            btnNext.setDisable(currentIndex == entities.size() - 1);
        } else {
            notifyError();
        }
    }

    public void newData() {
        newData(entities);
    }

    public void nextData() {
        if (currentIndex < entities.size() - 1) {
            updateData(entities, ++currentIndex);
            clear();
        }
    }

    abstract protected boolean check();

    abstract protected void save();
    abstract protected void clear();

    private void notifyError() {
        NotificationPane notificationPane = (NotificationPane) child;
        notificationPane.setShowFromTop(false);
        ImageView node = new ImageView("/img/dialog-error_16.png");
        notificationPane.show(getMessage("global.warning.msg.check"), node);
    }
}
