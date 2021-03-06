/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.core.dto;

import java.io.Serializable;

public interface Entity extends Serializable {

    void setId(Integer id);

    Integer getId();

    boolean isToDelete();

    void setToDelete(boolean toDelete);
}
