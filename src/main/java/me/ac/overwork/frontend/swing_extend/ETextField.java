package me.ac.overwork.frontend.swing_extend;

import javax.swing.JTextField;
import javax.swing.text.Document;
import java.awt.Font;

public class ETextField extends JTextField
{
	public ETextField(Document document, int columns, String text, Font font, String toolTip)
	{
		super(document, text, columns);
		setFont(font);
		setToolTipText(toolTip);
	}
}