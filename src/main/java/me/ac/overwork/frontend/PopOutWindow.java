package me.ac.overwork.frontend;

import me.ac.overwork.backend.BackendCore;
import me.ac.overwork.backend.ColorOperation;
import me.ac.overwork.backend.TimeOperation;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

abstract class PopOutWindow
{
	protected final JFrame popOutWindow = new JFrame();
	protected final JLabel myLabel = new JLabel("", SwingConstants.CENTER); //顯示的文字

	protected final TimeOperation timeOperation = BackendCore.getInstance().getTimeOperation();
	protected final ColorOperation colorOperation = BackendCore.getInstance().getColorOperation();

	protected static final int WIDTH = MainWindow.WIDTH / 2;
	protected static final int HEIGHT = MainWindow.HEIGHT / 4;

	PopOutWindow()
	{
		popOutWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		URL icon = PopOutWindow.class.getResource("/clock.png");
		if (icon != null) //有讀到
			popOutWindow.setIconImage(new ImageIcon(icon).getImage());
		popOutWindow.setLayout(new BorderLayout());
		popOutWindow.getContentPane().setBackground(Color.GREEN); //綠色背景 方便OBS

		//myLabel.setBounds(0, HEIGHT / 3, WIDTH, HEIGHT / 3);
		myLabel.setVerticalAlignment(SwingConstants.CENTER);
		popOutWindow.add(myLabel, BorderLayout.CENTER);

		updateTimeLabel(); //初始化數字
	}

	public boolean isVisible()
	{
		return popOutWindow.isVisible();
	}

	protected void setVisible()
	{
		popOutWindow.setVisible(true);
	}

	protected void dispose()
	{
		popOutWindow.dispose(); //關閉彈出式視窗
	}

	public abstract void updateTimeLabel();
}