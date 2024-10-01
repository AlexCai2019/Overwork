package me.ac.overwork.frontend;

import me.ac.overwork.backend.BackendCore;
import me.ac.overwork.frontend.swing_extend.ELabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class MainWindow
{
	private static MainWindow instance = null;

	public static void createInstance()
	{
		if (instance == null)
			instance = new MainWindow();
	}

	public static MainWindow getInstance()
	{
		return instance;
	}

	public final TimePanel timePanelManager;
	public final ControlPanel controlPanelManager;
	public final RemainTimeWindow remainTimeWindow;
	public final PassTimeWindow passTimeWindow;

	static final String FONT_NAME = "Microsoft JhengHei UI";
	static final int WIDTH = 360;
	static final int HEIGHT = 580;

	private MainWindow()
	{
		//加班台倒數
		JFrame mainWindow = new JFrame("\u52a0\u73ed\u53f0\u5012\u6578"); //加班台倒數
		URL icon = MainWindow.class.getResource("/clock.png");
		if (icon != null) //有讀到
			mainWindow.setIconImage(new ImageIcon(icon).getImage());
		mainWindow.setLayout(null);
		mainWindow.addWindowListener(new WindowAdapter() //按叉叉就結束
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				BackendCore.getInstance().onApplicationQuit(); //後端關閉
				remainTimeWindow.dispose();
				passTimeWindow.dispose();
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

		//彈出式視窗
		remainTimeWindow = new RemainTimeWindow();
		passTimeWindow = new PassTimeWindow();

		mainWindow.setVisible(true);
	}

	public static void messageBox(String message)
	{
		JOptionPane.showMessageDialog(null, new ELabel(message, new Font(FONT_NAME, Font.PLAIN, 18)), "\u932f\u8aa4", JOptionPane.ERROR_MESSAGE); //錯誤
	}
}