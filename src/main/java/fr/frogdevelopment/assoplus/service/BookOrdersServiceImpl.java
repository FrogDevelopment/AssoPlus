/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.dto.BookOrderDto;
import fr.frogdevelopment.assoplus.entities.BookOrder;
import org.springframework.stereotype.Service;

@Service("bookOrdersService")
public class BookOrdersServiceImpl extends AbstractService<BookOrder, BookOrderDto> implements BookOrdersService {

    BookOrderDto createDto(BookOrder bean) {
        BookOrderDto dto = new BookOrderDto();
        dto.setId(bean.getId());
        dto.setBookId(bean.getBookId());
        dto.setMemberId(bean.getMemberId());
        dto.setQuantity(bean.getQuantity());
        dto.setDeposit(bean.getDeposit());
        dto.setDateOrder(bean.getDateOrder());
        dto.setDateReceipt(bean.getDateReceipt());
        dto.setDateDelivery(bean.getDateDelivery());

        return dto;
    }

    BookOrder createBean(BookOrderDto dto) {
        BookOrder bean = new BookOrder();
        bean.setId(dto.getId());
        bean.setBookId(dto.getBookId());
        bean.setMemberId(dto.getMemberId());
        bean.setQuantity(dto.getQuantity());
        bean.setDeposit(dto.getDeposit());
        if (dto.getDateOrder() != null) {
            bean.setDateOrder(dto.getDateOrder().toString());
        }
        if (dto.getDateReceipt() != null) {
            bean.setDateReceipt(dto.getDateReceipt().toString());
        }
        if (dto.getDateDelivery() != null) {
            bean.setDateReceipt(dto.getDateDelivery().toString());
        }

        return bean;
    }

}
