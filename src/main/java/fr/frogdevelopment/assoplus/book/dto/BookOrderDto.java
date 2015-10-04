/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.book.dto;

import fr.frogdevelopment.assoplus.core.dto.Dto;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

public class BookOrderDto implements Dto {

    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleIntegerProperty bookId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty memberId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty quantity = new SimpleIntegerProperty();
    private final SimpleDoubleProperty deposit = new SimpleDoubleProperty();
    private final SimpleObjectProperty<LocalDate> dateOrder = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<LocalDate> dateReceipt = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<LocalDate> dateDelivery = new SimpleObjectProperty<>();

    @Override
    public Integer getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getBookId() {
        return bookId.get();
    }

    public SimpleIntegerProperty bookIdProperty() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId.set(bookId);
    }

    public int getMemberId() {
        return memberId.get();
    }

    public SimpleIntegerProperty memberIdProperty() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId.set(memberId);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public double getDeposit() {
        return deposit.get();
    }

    public SimpleDoubleProperty depositProperty() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit.set(deposit);
    }

    public LocalDate getDateOrder() {
        return dateOrder.get();
    }

    public SimpleObjectProperty<LocalDate> dateOrderProperty() {
        return dateOrder;
    }

    public void setDateOrder(LocalDate dateOrder) {
        this.dateOrder.set(dateOrder);
    }

    public void setDateOrder(String dateOrder) {
        if (StringUtils.isNotBlank(dateOrder)) {
            this.dateOrder.set(LocalDate.parse(dateOrder));
        }
    }

    public LocalDate getDateReceipt() {
        return dateReceipt.get();
    }

    public SimpleObjectProperty<LocalDate> dateReceiptProperty() {
        return dateReceipt;
    }

    public void setDateReceipt(LocalDate dateReceipt) {
        this.dateReceipt.set(dateReceipt);
    }

    public void setDateReceipt(String dateReceipt) {
        if (StringUtils.isNotBlank(dateReceipt)) {
            this.dateReceipt.set(LocalDate.parse(dateReceipt));
        }
    }

    public LocalDate getDateDelivery() {
        return dateDelivery.get();
    }

    public SimpleObjectProperty<LocalDate> dateDeliveryProperty() {
        return dateDelivery;
    }

    public void setDateDelivery(LocalDate dateDelivery) {
        this.dateDelivery.set(dateDelivery);
    }

    public void setDateDelivery(String dateDelivery) {
        if (StringUtils.isNotBlank(dateDelivery)) {
            this.dateDelivery.set(LocalDate.parse(dateDelivery));
        }
    }
}
