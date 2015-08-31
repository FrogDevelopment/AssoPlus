/*
 * Copyright (c) Frog Development 2015.
 */

package fr.frogdevelopment.assoplus.components.controls;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField
{
	@Override
	public void replaceText(int start, int end, String text)
	{
		if (validate(text))
		{
			super.replaceText(start, end, text);
		}
	}

	@Override
	public void replaceSelection(String text)
	{
		if (validate(text))
		{
			super.replaceSelection(text);
		}
	}

	private boolean validate(String text)
	{
		return ("".equals(text) || text.matches("[0-9]"));
	}

	public void setValue(long value) {
		setText(String.valueOf(value));
	}

	public long getValue() {
		return Long.parseLong(getText());
	}
}
