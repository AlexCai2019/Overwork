package me.ac.overwork.frontend.swing_extend;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;

public class ETextField extends JTextField
{
	public ETextField(Document document, int columns, String text, Font font, String toolTip)
	{
		super(document, text, columns);
		setFont(font);
		setToolTipText(toolTip);
	}
}