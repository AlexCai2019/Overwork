package me.ac.overwork.frontend;

import me.ac.overwork.backend.TimeType;

import java.awt.*;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class RemainTimeWindow extends PopOutWindow
{
	RemainTimeWindow()
	{
		popOutWindow.setTitle("\u5269\u9918\u6642\u9593"); //剩餘時間
		popOutWindow.setBounds(MainWindow.WIDTH, 0, settingOperation.getSize(TimeType.remainTime) * 8, settingOperation.getSize(TimeType.remainTime) * 6);
		myLabel.setForeground(new Color(settingOperation.getColor(TimeType.remainTime)));
		myLabel.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, settingOperation.getSize(TimeType.remainTime)));
	}

	@Override
	public void updateTimeLabel()
	{
		myLabel.setText(TimePanel.formatTime(timeOperation.getRemainTime()));
	}
}