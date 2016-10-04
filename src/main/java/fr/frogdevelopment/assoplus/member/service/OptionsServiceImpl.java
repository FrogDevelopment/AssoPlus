/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.member.dao.OptionDao;
import fr.frogdevelopment.assoplus.member.dto.Option;

import java.util.Collection;
import java.util.List;

@Service("optionsService")
public class OptionsServiceImpl implements OptionsService {

    @Autowired
    protected OptionDao optionDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Option> getAll() {
        return optionDao.getAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveData(Option option) {
        optionDao.save(option);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(Collection<Option> options) {
        optionDao.saveAll(options);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateData(Option option) {
        optionDao.update(option);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveOrUpdateAll(Collection<Option> options) {
        optionDao.saveOrUpdateAll(options);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteData(Option option) {
        optionDao.delete(option);
    }

}
