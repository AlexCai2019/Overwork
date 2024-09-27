package me.ac.overwork.frontend;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import java.util.regex.Pattern;

public sealed class TimeTextFieldFilter extends DocumentFilter permits HourTextFieldFilter, MinuteSecondFieldFilter
{
	private final Pattern numbersRegex;
	private final int[] times; //後端時間陣列 remain或pass
	private final int boundIndex; //對應的陣列索引

	TimeTextFieldFilter(int length, int[] times, int boundIndex)
	{
		numbersRegex = Pattern.compile("\\d{0," + length + '}');
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

		if (isValid(content)) //是數字 通過
		{
			super.insertString(fb, offset, text, attrs);
			update(content);
		}
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException
	{
		Document document = fb.getDocument();
		String content = new StringBuilder(document.getText(0, document.getLength()))
				.replace(offset, offset + length, text)
				.toString();

		if (isValid(content)) //是數字 通過
		{
			super.replace(fb, offset, length, text, attrs);
			update(content);
		}
	}

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException
	{
		Document document = fb.getDocument();
		String content = new StringBuilder(document.getText(0, document.getLength()))
				.delete(offset, offset + length)
				.toString();

		if (isValid(content)) //是數字 通過
		{
			super.remove(fb, offset, length);
			update(content);
		}
	}

	protected boolean isValid(String content)
	{
		return numbersRegex.matcher(content).matches();
	}

	private void update(String content)
	{
		times[boundIndex] = content.isEmpty() ? 0 : Integer.parseInt(content); //更新後端
	}
}

final class HourTextFieldFilter extends TimeTextFieldFilter
{
	HourTextFieldFilter(int[] times, int boundIndex)
	{
		super(5, times, boundIndex); //小時最高到五位數
	}
}

final class MinuteSecondFieldFilter extends TimeTextFieldFilter
{
	MinuteSecondFieldFilter(int[] times, int boundIndex)
	{
		super(2, times, boundIndex); //分鐘和秒最高二位數
	}

	@Override
	protected boolean isValid(String content)
	{
		return super.isValid(content) && (content.length() <= 1 || content.charAt(0) < '6');
	}
}