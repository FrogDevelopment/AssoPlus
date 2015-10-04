/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.book.controller;

import fr.frogdevelopment.assoplus.book.dto.BookDto;
import fr.frogdevelopment.assoplus.book.dto.BookOrderDto;
import fr.frogdevelopment.assoplus.book.service.BookOrdersService;
import fr.frogdevelopment.assoplus.book.service.BooksService;
import fr.frogdevelopment.assoplus.core.controller.AbstractCustomController;
import fr.frogdevelopment.assoplus.member.dto.MemberDto;
import fr.frogdevelopment.assoplus.member.service.MembersService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class BookOrdersController extends AbstractCustomController {

    @Autowired
    private BooksService booksService;

    @Autowired
    private BookOrdersService bookOrdersService;

    @Autowired
    private MembersService membersService;

    @FXML
    private TableView<BookOrderDto> tableView;
    @FXML
    private TableColumn<BookOrderDto, String> colBookTitle;
    @FXML
    private TableColumn<BookOrderDto, String> colBookPrice;
    @FXML
    private TableColumn<BookOrderDto, String> colMemberLastame;
    @FXML
    private TableColumn<BookOrderDto, String> colMemberStudentNumber;
    @FXML
    private TableColumn<BookOrderDto, Number> colQuantity;
    @FXML
    private TableColumn<BookOrderDto, String> colDeposit;
    @FXML
    private TableColumn<BookOrderDto, String> colDateOrder;

    @Override
    protected void initialize() {

        List<BookOrderDto> bookOrderDtos = bookOrdersService.getAll();

        Map<Integer, BookDto> mapBookDtos = booksService.getAll()
                .stream()
                .collect(Collectors.toMap(BookDto::getId, dto -> dto));

        Map<Integer, MemberDto> mapMemberDtos = membersService.getAll()
                .stream()
                .collect(Collectors.toMap(MemberDto::getId, dto -> dto));

        bookOrderDtos.forEach(bookOrderDto -> {
            BookDto bookDto = mapBookDtos.get(bookOrderDto.getBookId());
            bookOrderDto.setBook(bookDto);

            MemberDto memberDto = mapMemberDtos.get(bookOrderDto.getMemberId());
            bookOrderDto.setMember(memberDto);
        });

        colBookTitle.setCellValueFactory(param -> param.getValue().getBook().titleProperty());
        colBookPrice.setCellValueFactory(param -> {
            double price = param.getValue().getBook().getPrice();
            return new SimpleStringProperty(price + "€");
        });

        colMemberLastame.setCellValueFactory(param -> param.getValue().getMember().lastnameProperty());
        colMemberStudentNumber.setCellValueFactory(param -> param.getValue().getMember().studentNumberProperty());

        colQuantity.setCellValueFactory(param -> param.getValue().quantityProperty());
        colDeposit.setCellValueFactory(param -> {
            double price = param.getValue().getDeposit();
            return new SimpleStringProperty(price + "€");
        });
        colDateOrder.setCellValueFactory(param -> param.getValue().dateOrderProperty());

        tableView.setItems(FXCollections.observableArrayList(bookOrderDtos));


    }

}
