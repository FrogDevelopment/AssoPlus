/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.book.service;

import fr.frogdevelopment.assoplus.core.service.AbstractService;
import fr.frogdevelopment.assoplus.book.dto.BookDto;
import fr.frogdevelopment.assoplus.book.entity.Book;
import org.springframework.stereotype.Service;

@Service("booksService")
public class BooksServiceImpl extends AbstractService<Book, BookDto> implements BooksService {

    @Override
    protected BookDto createDto(Book bean) {
        BookDto dto = new BookDto();
        dto.setId(bean.getId());
        dto.setTitle(bean.getTitle());
        dto.setEditor(bean.getEditor());
        dto.setIsbn(bean.getIsbn());
        dto.setPrice(bean.getPrice());

        return dto;
    }

    @Override
    protected Book createBean(BookDto dto) {
        Book bean = new Book();
        bean.setId(dto.getId());
        bean.setTitle(dto.getTitle());
        bean.setEditor(dto.getEditor());
        bean.setIsbn(dto.getIsbn());
        bean.setPrice(dto.getPrice());

        return bean;
    }

}
