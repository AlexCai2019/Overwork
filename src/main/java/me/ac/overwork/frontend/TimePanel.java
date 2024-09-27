package me.ac.overwork.frontend;

import me.ac.overwork.backend.TimeOperation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class TimePanel extends APanelManager
{
	final JLabel remainTimeLabel = new JLabel("", SwingConstants.CENTER);
	final JLabel passTimeLabel = new JLabel("", SwingConstants.CENTER);

	TimePanel()
	{
		myPanel.setBounds(0, 0, MainWindow.WIDTH, MainWindow.HEIGHT * 3 / 10);
		myPanel.setLayout(new GridLayout(4, 1)); //只會放4個物件
		myPanel.setBorder(new EmptyBorder(5, 10, 5, 10)); //為上下左右預留空間
		myPanel.setBackground(Color.GREEN); //方便OBS去背

		Font fixedTextFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 24); //固定文字的字型
		Font numberFont = new Font(MainWindow.FONT_NAME, Font.BOLD, 48); //數字的字型

		JLabel remainTimeText = new JLabel("\u5269\u9918\u6642\u9593"); //剩餘時間 文字
		remainTimeText.setFont(fixedTextFont); //是固定文字
		myPanel.add(remainTimeText);

		remainTimeLabel.setFont(numberFont); //剩餘時間 數字
		myPanel.add(remainTimeLabel);

		JLabel passTimeText = new JLabel("\u7d93\u904e\u6642\u9593"); //經過時間 文字
		passTimeText.setFont(fixedTextFont);
		myPanel.add(passTimeText); //放入panel中

		passTimeLabel.setFont(numberFont); //經過時間 數字
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