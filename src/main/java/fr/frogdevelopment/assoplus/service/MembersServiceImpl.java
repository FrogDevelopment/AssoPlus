package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.dao.MemberDao;
import fr.frogdevelopment.assoplus.bean.Member;
import fr.frogdevelopment.assoplus.dto.MemberDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MembersServiceImpl implements MembersService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MembersServiceImpl.class);

	@Autowired
	private MemberDao memberDao;

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ObservableList<MemberDTO> getAllData() {
		ObservableList<MemberDTO> data = FXCollections.observableArrayList();

		List<Member> members = memberDao.getAll();

		members.forEach(member -> data.add(createDTO(member)));

		return data;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveData(MemberDTO memberDTO) {
		memberDao.save(createBean(memberDTO));
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void updateData(MemberDTO memberDTO) {
		memberDao.update(createBean(memberDTO));
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteData(MemberDTO memberDTO) {
		memberDao.delete(createBean(memberDTO));
	}

	private MemberDTO createDTO(Member member) {
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setId(member.getId());
		memberDTO.setNumber(member.getNumber());
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

		return memberDTO;
	}

	private Member createBean(MemberDTO memberDTO) {
		Member member = new Member();
		member.setId(memberDTO.getId());
		member.setNumber(memberDTO.getNumber());
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

		return member;
	}

}
