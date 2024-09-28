package me.ac.overwork.frontend;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import java.util.regex.Pattern;

sealed class TimeTextFieldFilter extends DocumentFilter permits MinuteSecondFieldFilter
{
	private final Pattern numbersRegex;

	TimeTextFieldFilter()
	{
		numbersRegex = Pattern.compile("\\d{0,5}");
	}

	TimeTextFieldFilter(int length)
	{
		numbersRegex = Pattern.compile("\\d{0," + length + '}');
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

	protected boolean isValid(String content)
	{
		return numbersRegex.matcher(content).matches();
	}
}

final class MinuteSecondFieldFilter extends TimeTextFieldFilter
{
	MinuteSecondFieldFilter()
	{
		super(2); //分鐘和秒最高二位數
	}

	@Override
	protected boolean isValid(String content)
	{
		return super.isValid(content) && (content.length() <= 1 || content.charAt(0) < '6'); //是數字或空 且小於60
	}
}