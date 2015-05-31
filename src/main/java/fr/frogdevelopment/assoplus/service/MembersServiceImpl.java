package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.bean.Member;
import fr.frogdevelopment.assoplus.dto.MemberDTO;
import org.springframework.stereotype.Service;

@Service("memberService")
public class MembersServiceImpl extends AbstractService<Member, MemberDTO> implements MembersService {

	protected MemberDTO createDTO(Member member) {
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setId(member.getId());
		memberDTO.setStudentNumber(member.getStudentNumber());
		memberDTO.setLastname(member.getLastname());
		memberDTO.setFirstname(member.getFirstname());
		memberDTO.setBirthday(member.getBirthday());
		memberDTO.setEmail(member.getEmail());
		memberDTO.setLicence(member.getLicence());
		memberDTO.setOption(member.getOption());
		memberDTO.setPhone(member.getPhone());
		memberDTO.setAddress(member.getAddress());
		memberDTO.setPostalCode(member.getPostalCode());
		memberDTO.setCity(member.getCity());
		memberDTO.setFeePaid(member.isFeePaid());

		return memberDTO;
	}

	protected Member createBean(MemberDTO memberDTO) {
		Member member = new Member();
		member.setId(memberDTO.getId());
		member.setStudentNumber(memberDTO.getStudentNumber());
		member.setLastname(memberDTO.getLastname());
		member.setFirstname(memberDTO.getFirstname());
		member.setBirthday(memberDTO.getBirthday());
		member.setEmail(memberDTO.getEmail());
		member.setLicence(memberDTO.getLicence());
		member.setOption(memberDTO.getOption());
		member.setPhone(memberDTO.getPhone());
		member.setAddress(memberDTO.getAddress());
		member.setPostalCode(memberDTO.getPostalCode());
		member.setCity(memberDTO.getCity());
		member.setFeePaid(memberDTO.getFeePaid());

		return member;
	}
}
