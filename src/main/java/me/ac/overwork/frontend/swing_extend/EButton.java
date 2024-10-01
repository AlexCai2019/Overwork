package me.ac.overwork.frontend.swing_extend;

import javax.swing.*;
import java.awt.*;

public class EButton extends JButton
{
	public EButton(String text, Font font)
	{
		this(text, font, text);
	}

	public EButton(String text, Font font, String toolTip)
	{
		super(text);
		setFont(font);
		setToolTipText(toolTip);
	}
}