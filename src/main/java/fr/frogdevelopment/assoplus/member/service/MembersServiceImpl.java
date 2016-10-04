/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.member.dao.MemberDao;
import fr.frogdevelopment.assoplus.member.dto.Member;

import java.util.Collection;

@Service
public class MembersServiceImpl implements MembersService {

    @Autowired
    private MemberDao memberDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Collection<Member> getAll() {
        return memberDao.getAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveData(Member member) {
        memberDao.create(member);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void createAll(Collection<Member> members) {
        memberDao.createAll(members);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateData(Member member) {
        memberDao.update(member);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void save(Collection<Member> members) {
        memberDao.create(members);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteData(Member member) {
        memberDao.delete(member);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSubscription(Member member) {
        memberDao.updateSubscription(member.getId(), member.getSubscription());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAnnals(Member member) {
        memberDao.updateAnnals(member.getId(), member.getAnnals());
    }

}
