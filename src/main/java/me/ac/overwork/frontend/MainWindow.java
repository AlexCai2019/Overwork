package me.ac.overwork.frontend;

import me.ac.overwork.backend.JSONFileException;
import me.ac.overwork.backend.TimeOperation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class MainWindow
{
	public static final MainWindow instance = new MainWindow();
	public static final String FONT_NAME = "Microsoft JhengHei UI";

	private static final int WIDTH = 480;
	private static final int HEIGHT = 960;

	private final JFrame mainWindow = new JFrame("\u52a0\u73ed\u53f0\u5012\u6578"); //加班台倒數

	private MainWindow()
	{
		try
		{
			TimeOperation.instance.readTimeData(); //讓後端核心從檔案中讀取資料
		}
		catch (JSONFileException exception) //有JSON例外
		{
			JLabel errorMessage = new JLabel(exception.getMessage());
			errorMessage.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
			JOptionPane.showMessageDialog(mainWindow, errorMessage, "\u932f\u8aa4", JOptionPane.ERROR_MESSAGE); //錯誤

			//訊息被關閉後
			System.exit(0);
			return;
		}

		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //按叉叉就結束
		mainWindow.setBounds(0, 0, WIDTH, HEIGHT);
		mainWindow.setResizable(false);

		//時間介面
		JPanel timePanel = new JPanel(new GridLayout(4, 1));
		timePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		timePanel.setBounds(0, 0, WIDTH, HEIGHT / 4);
		timePanel.setBackground(Color.GREEN); //方便OBS去背

		Font timePanelTextFont = new Font(FONT_NAME, Font.PLAIN, 36);
		JLabel remainTimeText = new JLabel("\u5269\u9918\u6642\u9593"); //剩餘時間
		remainTimeText.setFont(timePanelTextFont);
		timePanel.add(remainTimeText);

		mainWindow.add(timePanel);

		//操作介面
		JPanel controlPanel = new JPanel();
		controlPanel.setBounds(0, 0, WIDTH, HEIGHT * 3 / 4);
		mainWindow.add(controlPanel);

	}

	public void showWindow(boolean visible)
	{
		mainWindow.setVisible(visible);
	}
}