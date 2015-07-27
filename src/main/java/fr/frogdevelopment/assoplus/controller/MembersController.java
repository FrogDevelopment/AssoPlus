/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.controller;

import fr.frogdevelopment.assoplus.components.controls.MaskHelper;
import fr.frogdevelopment.assoplus.dto.LicenceDto;
import fr.frogdevelopment.assoplus.dto.MemberDto;
import fr.frogdevelopment.assoplus.dto.OptionDto;
import fr.frogdevelopment.assoplus.service.LicencesService;
import fr.frogdevelopment.assoplus.service.MembersService;
import fr.frogdevelopment.assoplus.service.OptionsService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller("membersController")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MembersController implements Initializable {

	private static final Logger LOGGER = LoggerFactory.getLogger(MembersController.class);

	private ResourceBundle bundle;

	@Autowired
	private MembersService membersService;

	@Autowired
	private LicencesService licencesService;


	@Autowired
	private OptionsService optionsService;

	@FXML
	private VBox vbTop;
	@FXML
	private Button btnShowHide;

	@FXML
	private VBox vbLeft;

	@FXML
	private TextField txtStudentNumber;
	@FXML
	private TextField txtLastname;
	@FXML
	private TextField txtFirstname;
	@FXML
	private DatePicker dpBirthday;
	@FXML
	private TextField txtEmail;
	@FXML
	public ComboBox<LicenceDto> cbLicence;
	@FXML
	public ComboBox<OptionDto> cbOption;
	@FXML
	private TextField txtPhone;
	@FXML
	private TextField txtAddress;
	@FXML
	private TextField txtPostalCode;
	@FXML
	private TextField txtCity;

	@FXML
	private TableView<MemberDto> table;

	private ObservableList<MemberDto> data;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bundle = resources;
		data = membersService.getAllData();
		table.setItems(data);

		MaskHelper.addMaskPhone(txtPhone);

		MaskHelper.addMaskDate(dpBirthday);
		dpBirthday.setPromptText("jj/mm/aaaa");
		dpBirthday.setConverter(new StringConverter<LocalDate>() {
			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});

		Set<LicenceDto> licenceDtos = licencesService.getAllOrderedByCode();
		cbLicence.setItems(FXCollections.observableArrayList(licenceDtos));
		cbLicence.setVisibleRowCount(4);
		cbLicence.setConverter(new StringConverter<LicenceDto>() {

			private final Map<String, LicenceDto> _cache = new HashMap<>();

			@Override
			public String toString(LicenceDto item) {
				_cache.put(item.getLabel(), item);
				return item.getLabel();
			}

			@Override
			public LicenceDto fromString(String label) {
				return _cache.get(label);
			}
		});

//		cbLicence.setCellFactory(new Callback<ListView<LicenceDto>, ListCell<LicenceDto>>() {
//			@Override
//			public ListCell<LicenceDto> call(ListView<LicenceDto> param) {
//				return new ListCell<LicenceDto>() {
//					{
//						super.setPrefWidth(100);
//					}
//
//					@Override
//					public void updateItem(LicenceDto item, boolean empty) {
//						super.updateItem(item, empty);
//						if (item != null) {
//							setText(item.getLabel());
//						} else {
//							setText(null);
//						}
//					}
//				};
//			}
//		});

//		cbLicence.setOnAction(event -> cbOption.setItems(FXCollections.observableArrayList(cbLicence.getSelectionModel().getSelectedItem().getOptions())));
//		cbLicence.setOnAction(event -> {cb.fil});

		Set<OptionDto> optionDtos = optionsService.getAllOrderedByCode();
		cbOption.setItems(FXCollections.observableArrayList(optionDtos));
		cbOption.setConverter(new StringConverter<OptionDto>() {

			private final Map<String, OptionDto> _cache = new HashMap<>();

			@Override
			public String toString(OptionDto item) {
				_cache.put(item.getLabel(), item);
				return item.getLabel();
			}

			@Override
			public OptionDto fromString(String label) {
				return _cache.get(label);
			}
		});
	}

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public void saveData() {

		boolean isOk = true;

		if (StringUtils.isBlank(txtStudentNumber.getText())) {
			txtStudentNumber.setStyle("-fx-border-color: red");
			isOk = false;
		} else {
			txtStudentNumber.setStyle("-fx-border-color: null");
		}

		if (StringUtils.isBlank(txtLastname.getText())) {
			txtLastname.setStyle("-fx-border-color: red");
			isOk = false;
		} else {
			txtLastname.setStyle("-fx-border-color: null");
		}

		if (StringUtils.isBlank(txtFirstname.getText())) {
			txtFirstname.setStyle("-fx-border-color: red");
			isOk = false;
		} else {
			txtFirstname.setStyle("-fx-border-color: null");
		}

		if (dpBirthday.getValue() == null) {
			dpBirthday.setStyle("-fx-border-color: red");
			isOk = false;
		} else {
			dpBirthday.setStyle("-fx-border-color: null");
		}
		if (StringUtils.isBlank(txtEmail.getText())) {
			txtEmail.setStyle("-fx-border-color: red");
			isOk = false;
		} else {
			txtEmail.setStyle("-fx-border-color: null");
		}
		if (cbLicence.getValue() == null) {
			cbLicence.setStyle("-fx-border-color: red");
			isOk = false;
		} else {
			cbLicence.setStyle("-fx-border-color: null");
		}
		if (cbOption.getValue() == null) {
			cbOption.setStyle("-fx-border-color: red");
			isOk = false;
		} else {
			cbOption.setStyle("-fx-border-color: null");
		}


		if (isOk) {
			MemberDto member = new MemberDto();
			member.setStudentNumber(Integer.parseInt(txtStudentNumber.getText()));
			member.setLastname(txtLastname.getText());
			member.setFirstname(txtFirstname.getText());
			member.setBirthday(dpBirthday.getValue().format(dateFormatter));
			member.setEmail(txtEmail.getText());
			member.setLicenceCode(cbLicence.getValue().getCode());
			member.setOptionCode(cbOption.getValue().getCode());
			member.setPhone(txtPhone.getText());
			member.setAddress(txtAddress.getText());
			member.setPostalCode(txtPostalCode.getText());
			member.setCity(txtCity.getText());

			membersService.saveData(member);

			data.add(member);
		}
	}

	public void importMembers() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Importer des adhérents");
		File file = fileChooser.showOpenDialog(vbLeft.getScene().getWindow());

		if (file != null) {
			membersService.importMembers(file);
		}
	}


	public void showHideMember() {
		final boolean isVisible = vbTop.isVisible();
		vbTop.setManaged(!isVisible);
		vbTop.setVisible(!isVisible);

		btnShowHide.setText(isVisible ? "Montrer" : "Cacher");
	}
}
