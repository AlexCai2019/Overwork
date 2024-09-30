package me.ac.overwork.frontend.swing_extend;

import javax.swing.*;
import java.awt.*;

public class ELabel extends JLabel
{
	public ELabel(String text, Font font)
	{
		super(text);
		setFont(font);
	}

	public ELabel(String text, int horizontalAlignment, Font font)
	{
		super(text, null, horizontalAlignment);
		setFont(font);
	}
}