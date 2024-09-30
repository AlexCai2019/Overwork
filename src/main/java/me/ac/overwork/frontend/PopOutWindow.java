package me.ac.overwork.frontend;

import me.ac.overwork.backend.BackendCore;
import me.ac.overwork.backend.TimeOperation;
import me.ac.overwork.frontend.swing_extend.ELabel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class PopOutWindow
{
	private final JFrame popOutWindow = new JFrame("\u52a0\u73ed\u53f0\u6642\u9593"); //加班台時間
	private final Font font = new Font(MainWindow.FONT_NAME, Font.BOLD, 48);
	private final JLabel remainTimeLabel = new ELabel("", SwingConstants.CENTER, font);
	private final JLabel passTimeLabel = new ELabel("", SwingConstants.CENTER, font);

	private final TimeOperation timeOperation = BackendCore.getInstance().getTimeOperation();

	private static final int WIDTH = MainWindow.WIDTH * 4 / 5;
	private static final int HEIGHT = MainWindow.HEIGHT / 3;

	PopOutWindow()
	{
		popOutWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		URL icon = PopOutWindow.class.getResource("/clock.png");
		if (icon != null) //有讀到
			popOutWindow.setIconImage(new ImageIcon(icon).getImage());
		popOutWindow.setLayout(null);
		popOutWindow.getContentPane().setBackground(Color.GREEN);
		popOutWindow.setBounds(MainWindow.WIDTH, 0, WIDTH, HEIGHT);
		popOutWindow.setResizable(false);

		remainTimeLabel.setBounds(0, 0, WIDTH, HEIGHT / 3);
		remainTimeLabel.setVerticalAlignment(SwingConstants.CENTER);
		popOutWindow.add(remainTimeLabel);

		passTimeLabel.setBounds(0, HEIGHT / 3, WIDTH, HEIGHT / 3);
		passTimeLabel.setVerticalAlignment(SwingConstants.CENTER);
		popOutWindow.add(passTimeLabel);

		updateTimeLabel(); //初始化數字
	}

	void setVisible()
	{
		popOutWindow.setVisible(true);
	}

	void dispose()
	{
		popOutWindow.dispose(); //關閉彈出式視窗
	}

	public void updateTimeLabel()
	{
		remainTimeLabel.setText(TimePanel.formatTime(timeOperation.getRemainTime()));
		passTimeLabel.setText(TimePanel.formatTime(timeOperation.getPassTime()));
	}
}