package me.ac.overwork.frontend;

import me.ac.overwork.backend.TimeType;

import java.awt.*;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class PassTimeWindow extends PopOutWindow
{
	PassTimeWindow()
	{
		popOutWindow.setTitle("\u5df2\u904e\u6642\u9593"); //已過時間
		popOutWindow.setBounds(MainWindow.WIDTH, settingOperation.getSize(TimeType.remainTime) * 6, settingOperation.getSize(TimeType.passTime) * 8, settingOperation.getSize(TimeType.passTime) * 6); //y座標是remain視窗的高度
		myLabel.setForeground(new Color(settingOperation.getColor(TimeType.passTime)));
		myLabel.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, settingOperation.getSize(TimeType.passTime)));
	}

	@Override
	public void updateTimeLabel()
	{
		myLabel.setText(TimePanel.formatTime(timeOperation.getPassTime()));
	}
}