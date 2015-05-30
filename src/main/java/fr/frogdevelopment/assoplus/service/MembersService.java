package fr.frogdevelopment.assoplus.service;

import fr.frogdevelopment.assoplus.dto.MemberDTO;
import javafx.collections.ObservableList;

public interface MembersService {

	ObservableList<MemberDTO> getAllData();

	void saveData(MemberDTO memberDTO);

	void updateData(MemberDTO memberDTO);

	void deleteData(MemberDTO memberDTO);
}
