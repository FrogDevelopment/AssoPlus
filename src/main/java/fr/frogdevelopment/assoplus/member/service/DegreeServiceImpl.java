/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.member.dao.DegreeDao;
import fr.frogdevelopment.assoplus.member.dto.Degree;

import java.util.Collection;

@Service("licencesService")
public class DegreeServiceImpl implements DegreeService {

    @Autowired
    protected DegreeDao degreeDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Collection<Degree> getAll() {
        return degreeDao.getAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveData(Degree degree) {
        degreeDao.create(degree);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createAll(Collection<Degree> degrees) {
        degreeDao.createAll(degrees);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateData(Degree degree) {
        degreeDao.update(degree);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Collection<Degree> degrees) {
        degreeDao.create(degrees);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteData(Degree degree) {
        degreeDao.delete(degree);
    }

}
