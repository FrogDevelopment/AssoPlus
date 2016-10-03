/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.core.service.AbstractService;
import fr.frogdevelopment.assoplus.member.dao.MemberDao;
import fr.frogdevelopment.assoplus.member.dto.MemberDto;
import fr.frogdevelopment.assoplus.member.entity.Member;

@Service
public class MembersServiceImpl extends AbstractService<Member, MemberDto> implements MembersService {

    @Autowired
    private MemberDao memberDao;

    @Override
    protected MemberDto createDto(Member bean) {
        MemberDto memberDto = new MemberDto();
        memberDto.setId(bean.getId());
        memberDto.setStudentNumber(bean.getStudentNumber());
        memberDto.setLastname(bean.getLastname());
        memberDto.setFirstname(bean.getFirstname());
        memberDto.setBirthday(bean.getBirthday());
        memberDto.setEmail(bean.getEmail());
        memberDto.setDegreeCode(bean.getDegreeCode());
        memberDto.setOptionCode(bean.getOptionCode());
        memberDto.setPhone(bean.getPhone());
        memberDto.setSubscription(bean.getSubscription());
        memberDto.setAnnals(bean.getAnnals());

        return memberDto;
    }

    @Override
    protected Member createBean(MemberDto dto) {
        Member member = new Member();
        member.setId(dto.getId());
        member.setStudentNumber(dto.getStudentNumber());
        member.setLastname(dto.getLastname());
        member.setFirstname(dto.getFirstname());
        if (dto.getBirthday() != null) {
            member.setBirthday(dto.getBirthday().toString());
        }
        member.setEmail(dto.getEmail());
        member.setDegreeCode(dto.getDegreeCode());
        member.setOptionCode(dto.getOptionCode());
        member.setPhone(dto.getPhone());
        member.setSubscription(dto.getSubscription());
        member.setAnnals(dto.getAnnals());

        return member;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSubscription(MemberDto dto) {
        memberDao.updateSubscription(dto.getId(), dto.getSubscription());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateAnnals(MemberDto dto) {
        memberDao.updateAnnals(dto.getId(), dto.getAnnals());
    }

}
