/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.book.service;

import org.springframework.stereotype.Service;

import fr.frogdevelopment.assoplus.book.dto.BookDto;

import java.util.Collection;
import java.util.List;

@Service("booksService")
public class BooksServiceImpl implements BooksService {

    @Override
    public List<BookDto> getAll() {
        return null;
    }

    @Override
    public void saveData(BookDto dto) {

    }

    @Override
    public void saveAll(Collection<BookDto> dtos) {

    }

    @Override
    public void updateData(BookDto dto) {

    }

    @Override
    public void saveOrUpdateAll(Collection<BookDto> dtos) {

    }

    @Override
    public void deleteData(BookDto dto) {

    }
}
