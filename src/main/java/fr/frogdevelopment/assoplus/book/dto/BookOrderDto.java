/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.book.dto;

import fr.frogdevelopment.assoplus.core.dto.Dto;
import fr.frogdevelopment.assoplus.member.dto.MemberDto;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class BookOrderDto implements Dto {

    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleIntegerProperty bookId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty memberId = new SimpleIntegerProperty();
    private final SimpleIntegerProperty quantity = new SimpleIntegerProperty();
    private final SimpleDoubleProperty deposit = new SimpleDoubleProperty();
    private final SimpleStringProperty dateOrder = new SimpleStringProperty();
    private final SimpleStringProperty dateReceipt = new SimpleStringProperty();
    private final SimpleStringProperty dateDelivery = new SimpleStringProperty();

    private final SimpleObjectProperty<BookDto> book = new SimpleObjectProperty<>(this, "book");
    private final SimpleObjectProperty<MemberDto> member = new SimpleObjectProperty<>(this, "member");

    @Override
    public Integer getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    @Override
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

    public String getDateOrder() {
        return dateOrder.get();
    }

    public SimpleStringProperty dateOrderProperty() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder.set(dateOrder);
    }

    public String getDateReceipt() {
        return dateReceipt.get();
    }

    public SimpleStringProperty dateReceiptProperty() {
        return dateReceipt;
    }

    public void setDateReceipt(String dateReceipt) {
        this.dateReceipt.set(dateReceipt);
    }

    public String getDateDelivery() {
        return dateDelivery.get();
    }

    public SimpleStringProperty dateDeliveryProperty() {
        return dateDelivery;
    }

    public void setDateDelivery(String dateDelivery) {
        this.dateDelivery.set(dateDelivery);
    }

    public BookDto getBook() {
        return book.get();
    }

    public SimpleObjectProperty<BookDto> bookProperty() {
        return book;
    }

    public void setBook(BookDto book) {
        this.book.set(book);
    }

    public MemberDto getMember() {
        return member.get();
    }

    public SimpleObjectProperty<MemberDto> memberProperty() {
        return member;
    }

    public void setMember(MemberDto member) {
        this.member.set(member);
    }
}
