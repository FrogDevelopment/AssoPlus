/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.controller;

import javafx.event.Event;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public abstract class AbstractCustomDialogController extends AbstractCustomController {

    protected void close() {
        Stage window = (Stage) getParent();
        Event.fireEvent(child, new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
