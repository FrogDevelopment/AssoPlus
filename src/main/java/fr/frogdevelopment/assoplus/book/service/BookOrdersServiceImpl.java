/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.book.service;

import org.springframework.stereotype.Service;

import fr.frogdevelopment.assoplus.book.dto.BookOrderDto;

import java.util.Collection;
import java.util.List;

@Service("bookOrdersService")
public class BookOrdersServiceImpl implements BookOrdersService {


    @Override
    public List<BookOrderDto> getAll() {
        return null;
    }

    @Override
    public void saveData(BookOrderDto dto) {

    }

    @Override
    public void saveAll(Collection<BookOrderDto> dtos) {

    }

    @Override
    public void updateData(BookOrderDto dto) {

    }

    @Override
    public void saveOrUpdateAll(Collection<BookOrderDto> dtos) {

    }

    @Override
    public void deleteData(BookOrderDto dto) {

    }
}
