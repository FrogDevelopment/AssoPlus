/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import fr.frogdevelopment.assoplus.entities.Member;
import fr.frogdevelopment.assoplus.entities.SchoolYear;
import fr.frogdevelopment.assoplus.dao.SchoolYearDao;
import fr.frogdevelopment.assoplus.dto.MemberDto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Set;

@Service("memberService")
public class MembersServiceImpl extends AbstractService<Member, MemberDto> implements MembersService {

    @Autowired
    private SchoolYearDao schoolYearDao;

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public SchoolYear getCurrentSchoolYear() {
        return schoolYearDao.getLastShoolYear();
    }

    MemberDto createDto(Member bean) {
        return MemberDto.createDto(bean);
    }

    Member createBean(MemberDto dto) {
        return MemberDto.createBean(dto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void importMembers(File file) {

        try (final InputStream is = new FileInputStream(file);
             final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
             final CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader().withSkipHeaderRecord().withDelimiter(','))) {

            Set<Member> members = new HashSet<>();
            parser.forEach(line -> {
                Member member = new Member();

                String studentNumber = line.get("NUMERO ETUDIANT");
                if (!StringUtils.isEmpty(studentNumber)) {
                    member.setStudentNumber(Integer.valueOf(studentNumber));
                    member.setLastname(line.get("NOM"));
                    member.setFirstname(line.get("PRENOM"));
                    member.setLicence(line.get("DEGRES"));
                    member.setOption(line.get("OPTION"));
                    member.setPhone(line.get("TELEPHONE"));
                    member.setEmail(line.get("E-MAIL"));

                    members.add(member);
                }
            });

            dao.saveAll(members);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
