package me.ac.overwork.frontend;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import java.util.regex.Pattern;

public sealed class TimeTextFieldFilter extends DocumentFilter permits HourTextFieldFilter
{
	private final Pattern numbersRegex;
	private final JTextField[] timePanelFields;
	private final int[] times;
	private final int boundIndex;

	TimeTextFieldFilter(int length, JTextField[] timePanelFields, int[] times, int boundIndex)
	{
		numbersRegex = Pattern.compile("\\d{0," + length + '}');
		this.timePanelFields = timePanelFields;
		this.times = times;
		this.boundIndex = boundIndex;
	}

	@Override
	public void insertString(FilterBypass fb, int offset, String text, AttributeSet attrs) throws BadLocationException
	{
		Document document = fb.getDocument();
		String content = new StringBuilder(document.getText(0, document.getLength()))
				.insert(offset, text)
				.toString();

		if (numbersRegex.matcher(content).matches()) //是數字 通過
			super.insertString(fb, offset, text, attrs);
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
	{
		Document document = fb.getDocument();
		String content = new StringBuilder(document.getText(0, document.getLength()))
				.replace(offset, offset + length, text)
				.toString();

		if (numbersRegex.matcher(content).matches()) //是數字 通過
			super.replace(fb, offset, length, text, attrs);
	}

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException
	{
		Document document = fb.getDocument();
		String content = new StringBuilder(document.getText(0, document.getLength()))
				.delete(offset, offset + length)
				.toString();

		if (numbersRegex.matcher(content).matches()) //是數字 通過
		{
			super.remove(fb, offset, length);
			update(content);
		}
	}

	private void update(String content)
	{
		String newTime = content.isEmpty() ? "0" : content;
		times[boundIndex] = Integer.parseInt(newTime); //更新後端
		timePanelFields[boundIndex].setText(newTime); //更新時間面板
	}
}

final class HourTextFieldFilter extends TimeTextFieldFilter
{
	HourTextFieldFilter(JTextField[] timePanelFields, int[] times, int boundIndex)
	{
		super(5, timePanelFields, times, boundIndex); //小時最高到五位數
	}
}