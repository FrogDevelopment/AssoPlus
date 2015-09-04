/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import javafx.event.Event;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

abstract class AbstractCustomDialogController extends AbstractCustomController {

    protected void close() {
        Window window = getParent();
        Event.fireEvent(window, new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
