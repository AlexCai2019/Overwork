package me.ac.overwork.frontend;

import me.ac.overwork.backend.BackendCore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class MainWindow
{
	public static MainWindow instance;

	final TimePanel timePanelManager;
	final ControlPanel controlPanelManager;

	static final String FONT_NAME = "Microsoft JhengHei UI";
	private static final int WIDTH = 480;
	private static final int HEIGHT = 720;

	public MainWindow()
	{
		//加班台倒數
		JFrame mainWindow = new JFrame("\u52a0\u73ed\u53f0\u5012\u6578");
		URL icon = MainWindow.class.getResource("/clock.png");
		if (icon != null)
			mainWindow.setIconImage(new ImageIcon(icon).getImage());
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //按叉叉就結束
		mainWindow.setLayout(new GridLayout(4, 1));
		mainWindow.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				BackendCore.instance.onApplicationQuit(); //後端關閉
				timePanelManager.onMainWindowClosing(); //計時關閉
				controlPanelManager.onMainWindowClosing(); //操作關閉
				System.exit(0); //結束
			}
		});
		mainWindow.setBounds(0, 0, WIDTH, HEIGHT);
		mainWindow.setResizable(false);

		//時間介面
		timePanelManager = new TimePanel(); //現在才創 為了給載入JSON留時間
		mainWindow.add(timePanelManager.myPanel);

		//操作介面
		controlPanelManager = new ControlPanel();
		mainWindow.add(controlPanelManager.myPanel);

		mainWindow.setVisible(true);
	}

	public static void messageBox(String message)
	{
		JLabel errorMessage = new JLabel(message);
		errorMessage.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
		JOptionPane.showMessageDialog(null, errorMessage, "\u932f\u8aa4", JOptionPane.ERROR_MESSAGE); //錯誤
	}
}