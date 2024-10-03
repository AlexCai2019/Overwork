package me.ac.overwork.frontend;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import java.util.regex.Pattern;

sealed class TextFieldFilter extends DocumentFilter permits HourTextFieldFilter, MinuteSecondFieldFilter, ColorFieldFilter
{
	protected final Pattern regex; //要檢查的正規表示式

	protected TextFieldFilter(String regexString)
	{
		regex = Pattern.compile(regexString);
	}

	protected boolean isValid(String content)
	{
		return regex.matcher(content).matches();
	}

	@Override
	public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException
	{
		Document document = fb.getDocument();
		String content = new StringBuilder(document.getText(0, document.getLength()))
				.insert(offset, text)
				.toString();

		if (isValid(content)) //是數字 通過
			super.insertString(fb, offset, text, attrs);
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
	{
		Document document = fb.getDocument();
		String content = new StringBuilder(document.getText(0, document.getLength()))
				.replace(offset, offset + length, text)
				.toString();

		if (isValid(content)) //是數字 通過
			super.replace(fb, offset, length, text, attrs);
	}

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException
	{
		Document document = fb.getDocument();
		String content = new StringBuilder(document.getText(0, document.getLength()))
				.delete(offset, offset + length)
				.toString();

		if (isValid(content)) //是數字 通過
			super.remove(fb, offset, length);
	}
}

final class HourTextFieldFilter extends TextFieldFilter
{
	HourTextFieldFilter()
	{
		super("\\d{0,5}"); //0到5個數字
	}
}

final class MinuteSecondFieldFilter extends TextFieldFilter
{
	MinuteSecondFieldFilter()
	{
		super("([0-5]?\\d)?"); //空字串 或0~59
	}
}

final class ColorFieldFilter extends TextFieldFilter
{
	ColorFieldFilter()
	{
		super("[0-9A-Fa-f]{0,6}"); //以#開頭 0到6個hex數字
	}
}