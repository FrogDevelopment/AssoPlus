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
import java.util.List;

@Service
public class MembersServiceImpl implements MembersService {

    @Autowired
    private MemberDao memberDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Member> getAll() {
        return memberDao.getAll();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveData(Member member) {
        memberDao.save(member);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAll(Collection<Member> members) {
        memberDao.saveAll(members);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateData(Member member) {
        memberDao.update(member);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveOrUpdateAll(Collection<Member> members) {
        memberDao.saveOrUpdateAll(members);
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
