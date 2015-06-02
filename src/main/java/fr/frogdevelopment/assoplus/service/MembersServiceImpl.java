package fr.frogdevelopment.assoplus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.frogdevelopment.assoplus.bean.Member;
import fr.frogdevelopment.assoplus.bean.SchoolYear;
import fr.frogdevelopment.assoplus.dao.SchoolYearDao;
import fr.frogdevelopment.assoplus.dto.MemberDtok;

@Service("memberService")
public class MembersServiceImpl extends AbstractService<Member, MemberDtok> implements MembersService {

	@Autowired
	private SchoolYearDao schoolYearDao;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public SchoolYear getCurrentSchoolYear() {
		return schoolYearDao.getLastShoolYear();
	}

	protected MemberDtok createDTO(Member member) {
		MemberDtok memberDto = new MemberDtok();
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
		memberDto.setFeePaid(member.isFeePaid());

		return memberDto;
	}

	protected Member createBean(MemberDtok memberDto) {
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
		member.setFeePaid(memberDto.getFeePaid());

		return member;
	}
}
