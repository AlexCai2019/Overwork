package me.ac.overwork.frontend;

import java.awt.*;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class PassTimeWindow extends PopOutWindow
{
	PassTimeWindow()
	{
		super();
		popOutWindow.setTitle("\u5df2\u904e\u6642\u9593"); //已過時間
		popOutWindow.setBounds(MainWindow.WIDTH, HEIGHT, WIDTH, HEIGHT);
		myLabel.setForeground(new Color(colorOperation.passTimeColor));
		myLabel.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, sizeOperation.passTimeSize));
	}

	@Override
	public void updateTimeLabel()
	{
		myLabel.setText(TimePanel.formatTime(timeOperation.getPassTime()));
	}
}