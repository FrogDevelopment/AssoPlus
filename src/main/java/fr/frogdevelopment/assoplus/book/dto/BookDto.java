/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.book.dto;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import fr.frogdevelopment.assoplus.core.dto.Entity;

public class BookDto implements Entity {

    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleStringProperty title = new SimpleStringProperty("");
    private final SimpleStringProperty editor = new SimpleStringProperty("");
    private final SimpleStringProperty isbn = new SimpleStringProperty("");
    private final SimpleDoubleProperty price = new SimpleDoubleProperty();

    @Override
    public Integer getId() {
        return id.get();
    }

    @Override
    public boolean isToDelete() {
        return false;
    }

    @Override
    public void setToDelete(boolean toDelete) {

    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getEditor() {
        return editor.get();
    }

    public SimpleStringProperty editorProperty() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor.set(editor);
    }

    public String getIsbn() {
        return isbn.get();
    }

    public SimpleStringProperty isbnProperty() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }
}
