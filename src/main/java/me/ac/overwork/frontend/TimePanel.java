package me.ac.overwork.frontend;

import me.ac.overwork.backend.TimeOperation;
import me.ac.overwork.frontend.swing_extend.ELabel;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class TimePanel extends PanelParent
{
	static final int MY_PANEL_HEIGHT = MainWindow.HEIGHT * 2 / 10;

	final JLabel remainTimeLabel = new JLabel(formatTime(timeOperation.getRemainTime()), SwingConstants.CENTER);
	final JLabel passTimeLabel = new JLabel(formatTime(timeOperation.getPassTime()), SwingConstants.CENTER);

	TimePanel()
	{
		myPanel.setBounds(0, 0, MainWindow.WIDTH, MY_PANEL_HEIGHT);
		myPanel.setBackground(Color.GREEN); //綠色背景方便OBS擷取

		//固定文字的字型
		Font fixedTextFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 18);

		JLabel remainTimeText = new ELabel("\u5269\u9918\u6642\u9593", fixedTextFont); //剩餘時間
		remainTimeText.setBounds(10, 0, 18 * 4, MY_PANEL_HEIGHT / 2);
		myPanel.add(remainTimeText);

		remainTimeLabel.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, sizeOperation.remainTimeSize));
		remainTimeLabel.setBounds(18 * 4, 0, MainWindow.WIDTH - 18 * 4, MY_PANEL_HEIGHT / 2);
		myPanel.add(remainTimeLabel); //放入panel中

		JLabel passTimeText = new ELabel("\u7d93\u904e\u6642\u9593", fixedTextFont); //經過時間
		passTimeText.setBounds(10, MY_PANEL_HEIGHT / 2, 18 * 4, MY_PANEL_HEIGHT / 2);
		myPanel.add(passTimeText);

		passTimeLabel.setFont(new Font(MainWindow.FONT_NAME, Font.BOLD, sizeOperation.passTimeSize));
		passTimeLabel.setBounds(18 * 4, MY_PANEL_HEIGHT / 2, MainWindow.WIDTH - 18 * 4, MY_PANEL_HEIGHT / 2);
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