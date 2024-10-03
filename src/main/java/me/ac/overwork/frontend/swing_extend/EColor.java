package me.ac.overwork.frontend.swing_extend;

import java.awt.Color;

public class EColor extends Color
{
	public EColor(int r, int g, int b)
	{
		super(r, g, b);
	}

	public static final EColor MINECRAFT_DARK_GREEN = new EColor(0, 170, 0);
	public static final EColor MINECRAFT_RED = new EColor(255, 85, 85);
}