/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.components.controls;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class MaskHelper {

	private static final char MASK_CHARACTER_TO_REPLACE = '#';

	public static final String MASK_PHONE = "## ## ## ## ##";
	public static final String MASK_DATE = "##/##/####";

	/**
	 * Adds a static mask to the specified text field.
	 *
	 * @param textField the text field.
	 * @param mask      the mask to apply.
	 *                  Example of usage: addMask(txtPhone, {@link MaskHelper#MASK_PHONE});
	 */
	public static void addMask(final TextField textField, final String mask) {
		textField.setPromptText(mask);

		final int maxLenght = mask.length();

		final char[] charsMask = mask.toCharArray();

		textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
			String currentText = textField.getText();

			// text limiter
			if (currentText.length() > maxLenght) {
				textField.setText(currentText.substring(0, maxLenght));
				return;
			}

			StringBuilder sb = applyMask(maxLenght, charsMask, currentText);

			textField.setText(sb.toString());
		});
	}

	private static StringBuilder applyMask(int maxLenght, char[] charsMask, String currentText) {
		StringBuilder sb = new StringBuilder();
		char[] charsText = currentText.toCharArray();
		int max = charsText.length;
		int i;
		char cMask;
		for (i = 0; i < max; i++) {
			cMask = charsMask[i];
			if (cMask == MASK_CHARACTER_TO_REPLACE) {
				sb.append(charsText[i]);
			} else {
				sb.append(cMask);
			}
		}

		if (i < maxLenght) {
			cMask = charsMask[i];
			while (cMask != MASK_CHARACTER_TO_REPLACE) {
				sb.append(cMask);
				cMask = charsMask[++i];
			}
		}
		return sb;
	}

	public static void addMaskPhone(final TextField textField) {
		addMask(textField, MASK_PHONE);
	}

	public static void addMaskDate(final DatePicker datePicker) {
		addMask(datePicker.getEditor(), MASK_DATE);
	}

}
