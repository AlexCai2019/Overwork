package me.ac.overwork.frontend;

import me.ac.overwork.backend.TimeOperation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class MainWindow
{
	private final TimeOperation backendCore;
	private final TimePanel timePanelManager;

	static final String FONT_NAME = "Microsoft JhengHei UI";
	static final int WIDTH = 480;
	static final int HEIGHT = 960;

	private final JFrame mainWindow = new JFrame("\u52a0\u73ed\u53f0\u5012\u6578"); //加班台倒數

	public MainWindow(TimeOperation backendCore)
	{
		this.backendCore = backendCore; //讓後端核心從檔案中讀取資料

		URL icon = MainWindow.class.getResource("/clock.png");
		if (icon != null)
			mainWindow.setIconImage(new ImageIcon(icon).getImage());
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //按叉叉就結束
		mainWindow.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				timePanelManager.onMainWindowClosing();

				mainWindow.dispose();
				System.exit(0);
			}
		});
		mainWindow.setBounds(0, 0, WIDTH, HEIGHT);
		mainWindow.setResizable(false);

		//時間介面
		timePanelManager = new TimePanel(backendCore); //現在才創 為了給載入JSON留時間
		mainWindow.add(timePanelManager.getPanel());

		//操作介面
		mainWindow.add(createControlPanel());

		mainWindow.setVisible(true);
	}

	private JPanel createControlPanel()
	{
		JPanel controlPanel = new JPanel();
		controlPanel.setBounds(0, 0, WIDTH, HEIGHT * 4 / 5);

		return controlPanel;
	}

	public static void messageBox(String message)
	{
		JLabel errorMessage = new JLabel(message);
		errorMessage.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
		JOptionPane.showMessageDialog(null, errorMessage, "\u932f\u8aa4", JOptionPane.ERROR_MESSAGE); //錯誤
	}
}