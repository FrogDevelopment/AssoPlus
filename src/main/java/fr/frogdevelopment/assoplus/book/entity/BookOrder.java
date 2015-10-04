/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.book.entity;

import fr.frogdevelopment.assoplus.core.dao.ForeignKey;
import fr.frogdevelopment.assoplus.core.entity.Entity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table(name = "book_order")
public class BookOrder implements Entity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue
    private Integer id;

    @ForeignKey(table = "book")
    @Column(name = "book_id", nullable = false)
    private Integer bookId;

    @ForeignKey(table = "member")
    @Column(name = "member_id", nullable = false)
    private Integer memberId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "deposit", nullable = false)
    private Double deposit;

    @Column(name = "date_order", nullable = false)
    private String dateOrder;

    @Column(name = "date_receipt", nullable = true)
    private String dateReceipt;

    @Column(name = "date_delivery", nullable = true)
    private String dateDelivery;

    // ********************************** \\
    //            Getter & Setter         \\
    // ********************************** \\

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getDateReceipt() {
        return dateReceipt;
    }

    public void setDateReceipt(String dateReceipt) {
        this.dateReceipt = dateReceipt;
    }

    public String getDateDelivery() {
        return dateDelivery;
    }

    public void setDateDelivery(String dateDelivery) {
        this.dateDelivery = dateDelivery;
    }

    // ********************************** \\
    //           Overriden methods        \\
    // ********************************** \\

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        BookOrder bookOrder = (BookOrder) o;

        return new EqualsBuilder()
                .append(bookId, bookOrder.bookId)
                .append(memberId, bookOrder.memberId)
                .append(quantity, bookOrder.quantity)
                .append(dateOrder, bookOrder.dateOrder)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(bookId)
                .append(memberId)
                .append(quantity)
                .append(dateOrder)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("bookId", bookId)
                .append("memberId", memberId)
                .append("quantity", quantity)
                .append("deposit", deposit)
                .append("dateOrder", dateOrder)
                .append("dateReceipt", dateReceipt)
                .append("dateDelivery", dateDelivery)
                .toString();
    }
}
