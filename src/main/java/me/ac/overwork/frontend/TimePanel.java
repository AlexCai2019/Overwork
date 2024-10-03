package me.ac.overwork.frontend;

import me.ac.overwork.backend.TimeOperation;
import me.ac.overwork.frontend.swing_extend.ELabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class TimePanel extends PanelParent
{
	static final int MY_PANEL_HEIGHT = MainWindow.HEIGHT * 2 / 10;

	private final Font numberFont = new Font(MainWindow.FONT_NAME, Font.BOLD, 24); //數字的字型
	final JLabel remainTimeLabel = new ELabel(formatTime(timeOperation.getRemainTime()), SwingConstants.CENTER, numberFont);
	final JLabel passTimeLabel = new ELabel(formatTime(timeOperation.getPassTime()), SwingConstants.CENTER, numberFont);

	TimePanel()
	{
		myPanel.setBounds(0, 0, MainWindow.WIDTH, MY_PANEL_HEIGHT);
		myPanel.setLayout(new GridLayout(4, 1)); //只會放4個物件
		myPanel.setBorder(new EmptyBorder(5, 10, 5, 10)); //為上下左右預留空間

		//固定文字的字型
		Font fixedTextFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 18);

		JLabel remainTimeText = new ELabel("\u5269\u9918\u6642\u9593", fixedTextFont); //剩餘時間 文字
		myPanel.add(remainTimeText);

		myPanel.add(remainTimeLabel);

		JLabel passTimeText = new ELabel("\u7d93\u904e\u6642\u9593", fixedTextFont); //經過時間 文字
		myPanel.add(passTimeText); //放入panel中

		myPanel.add(passTimeLabel); //放入panel中
	}

	public void updateTimeLabel()
	{
		remainTimeLabel.setText(formatTime(timeOperation.getRemainTime())); //剩餘時間
		passTimeLabel.setText(formatTime(timeOperation.getPassTime())); //經過時間
	}

	static String formatTime(int[] time)
	{
		return String.format("%d:%02d:%02d", time[TimeOperation.HOUR], time[TimeOperation.MINUTE], time[TimeOperation.SECOND]);
	}
}