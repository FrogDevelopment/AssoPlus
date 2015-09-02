/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.entities.Member;

import java.time.LocalDate;

@Service
public class MembersServiceImpl extends AbstractService<Member, MemberDto> implements MembersService {

    MemberDto createDto(Member bean) {
        MemberDto memberDto = new MemberDto();
        memberDto.setId(bean.getId());
        memberDto.setStudentNumber(bean.getStudentNumber());
        memberDto.setLastname(bean.getLastname());
        memberDto.setFirstname(bean.getFirstname());
        if (StringUtils.isNotBlank(bean.getBirthday())) {
            memberDto.setBirthday(LocalDate.parse(bean.getBirthday()));
        }
        memberDto.setEmail(bean.getEmail());
        memberDto.setDegreeCode(bean.getDegreeCode());
        memberDto.setOptionCode(bean.getOptionCode());
        memberDto.setPhone(bean.getPhone());
        memberDto.setSubscription(1 == bean.getSubscription());
        memberDto.setAnnals(1 == bean.getAnnals());

        return memberDto;
    }

    Member createBean(MemberDto dto) {
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
        member.setSubscription(dto.getSubscription() ? 1 : 0);
        member.setAnnals(dto.getAnnals() ? 1 : 0);

        return member;
    }

}
