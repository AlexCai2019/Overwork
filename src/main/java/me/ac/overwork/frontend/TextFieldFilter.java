package me.ac.overwork.frontend;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import java.util.regex.Pattern;

class TextFieldFilter extends DocumentFilter
{
	protected final Pattern regex; //要檢查的正規表示式

	protected TextFieldFilter(String regexString)
	{
		regex = Pattern.compile(regexString); //編譯正規表示式
	}

	protected boolean isValid(String content)
	{
		return regex.matcher(content).matches(); //回傳正規表示式檢查是否通過
	}

	@Override
	public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException
	{
		Document document = fb.getDocument();
		String content = new StringBuilder(document.getText(0, document.getLength()))
				.insert(offset, text)
				.toString();

		if (isValid(content)) //通過
			super.insertString(fb, offset, text, attrs);
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
	{
		Document document = fb.getDocument();
		String content = new StringBuilder(document.getText(0, document.getLength()))
				.replace(offset, offset + length, text)
				.toString();

		if (isValid(content)) //通過
			super.replace(fb, offset, length, text, attrs);
	}

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException
	{
		Document document = fb.getDocument();
		String content = new StringBuilder(document.getText(0, document.getLength()))
				.delete(offset, offset + length)
				.toString();

		if (isValid(content)) //通過
			super.remove(fb, offset, length);
	}
}