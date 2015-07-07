/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.bean.Member;
import fr.frogdevelopment.assoplus.bean.SchoolYear;
import fr.frogdevelopment.assoplus.dao.SchoolYearDao;
import fr.frogdevelopment.assoplus.dto.MemberDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.*;
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

	public MemberDto createDTO(Member member) {
		MemberDto memberDto = new MemberDto();
		memberDto.setId(member.getId());
		memberDto.setStudentNumber(member.getStudentNumber());
		memberDto.setLastname(member.getLastname());
		memberDto.setFirstname(member.getFirstname());
		memberDto.setBirthday(member.getBirthday());
		memberDto.setEmail(member.getEmail());
		memberDto.setLicence(member.getLicence());
		memberDto.setOption(member.getOption());
		memberDto.setPhone(member.getPhone());
		memberDto.setAddress(member.getAddress());
		memberDto.setPostalCode(member.getPostalCode());
		memberDto.setCity(member.getCity());

		return memberDto;
	}

	public Member createBean(MemberDto memberDto) {
		Member member = new Member();
		member.setId(memberDto.getId());
		member.setStudentNumber(memberDto.getStudentNumber());
		member.setLastname(memberDto.getLastname());
		member.setFirstname(memberDto.getFirstname());
		member.setBirthday(memberDto.getBirthday());
		member.setEmail(memberDto.getEmail());
		member.setLicence(memberDto.getLicence());
		member.setOption(memberDto.getOption());
		member.setPhone(memberDto.getPhone());
		member.setAddress(memberDto.getAddress());
		member.setPostalCode(memberDto.getPostalCode());
		member.setCity(memberDto.getCity());

		return member;
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
