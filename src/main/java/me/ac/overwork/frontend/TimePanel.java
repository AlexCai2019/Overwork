package me.ac.overwork.frontend;

import me.ac.overwork.backend.TimeOperation;
import me.ac.overwork.frontend.swing_extend.ELabel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class TimePanel extends APanelManager
{
	private final Font numberFont = new Font(MainWindow.FONT_NAME, Font.BOLD, 48); //數字的字型
	final JLabel remainTimeLabel = new ELabel("", SwingConstants.CENTER, numberFont);
	final JLabel passTimeLabel = new ELabel("", SwingConstants.CENTER, numberFont);

	TimePanel()
	{
		myPanel.setBounds(0, 0, MainWindow.WIDTH, MainWindow.HEIGHT * 3 / 10);
		myPanel.setLayout(new GridLayout(4, 1)); //只會放4個物件
		myPanel.setBorder(new EmptyBorder(5, 10, 5, 10)); //為上下左右預留空間
		myPanel.setBackground(Color.GREEN); //方便OBS去背

		//固定文字的字型
		Font fixedTextFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 24);

		JLabel remainTimeText = new ELabel("\u5269\u9918\u6642\u9593", fixedTextFont); //剩餘時間 文字
		myPanel.add(remainTimeText);

		myPanel.add(remainTimeLabel);

		JLabel passTimeText = new ELabel("\u7d93\u904e\u6642\u9593", fixedTextFont); //經過時間 文字
		myPanel.add(passTimeText); //放入panel中

		myPanel.add(passTimeLabel); //放入panel中

		updateTimeLabel(); //更新時間數字
	}

	public void updateTimeLabel()
	{
		remainTimeLabel.setText(formatTime(timeOperation.getRemainTime())); //剩餘時間
		passTimeLabel.setText(formatTime(timeOperation.getPassTime())); //經過時間
	}

	private String formatTime(int[] time)
	{
		return String.format("%05d:%02d:%02d", time[TimeOperation.HOUR], time[TimeOperation.MINUTE], time[TimeOperation.SECOND]);
	}
}