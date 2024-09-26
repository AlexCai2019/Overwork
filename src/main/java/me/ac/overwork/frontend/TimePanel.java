package me.ac.overwork.frontend;

import me.ac.overwork.backend.BackendCore;
import me.ac.overwork.backend.TimeOperation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class TimePanel extends APanelManager
{
	final JLabel remainTimeLabel = new JLabel("", SwingConstants.CENTER);
	final JLabel passTimeLabel = new JLabel("", SwingConstants.CENTER);

	private ScheduledExecutorService executorService = null;
	private ScheduledFuture<?> everySecond;

	TimePanel(BackendCore backendCore)
	{
		super(backendCore);

		myPanel.setLayout(new GridLayout(4, 1)); //只會放4個物件
		myPanel.setBorder(new EmptyBorder(5, 10, 5, 10)); //為上下左右預留空間
		myPanel.setBackground(Color.GREEN); //方便OBS去背

		Font fixedTextFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 24); //固定文字的字型
		Font numberFont = new Font(MainWindow.FONT_NAME, Font.BOLD, 48); //數字的字型

		JLabel remainTimeText = new JLabel("\u5269\u9918\u6642\u9593"); //剩餘時間 文字
		remainTimeText.setFont(fixedTextFont); //是固定文字
		myPanel.add(remainTimeText);

		TimeOperation timeOperation = backendCore.getTimeOperation();

		remainTimeLabel.setFont(numberFont); //剩餘時間 數字
		remainTimeLabel.setText(formatTime(timeOperation.getRemainTime()));
		myPanel.add(remainTimeLabel);

		JLabel passTimeText = new JLabel("\u7d93\u904e\u6642\u9593"); //經過時間 文字
		passTimeText.setFont(fixedTextFont);
		myPanel.add(passTimeText); //放入panel中

		passTimeLabel.setFont(numberFont); //經過時間 數字
		passTimeLabel.setText(formatTime(timeOperation.getPassTime()));
		myPanel.add(passTimeLabel); //放入panel中
	}

	//不用後端計時，修改比較方便
	void startTimer()
	{
		if (executorService != null) //已經開始了
			return;
		executorService = Executors.newSingleThreadScheduledExecutor(); //處理中控
		everySecond = executorService.scheduleAtFixedRate(() -> //每秒執行
		{
			timeOperation.subtractRemainTime(); //減少剩餘時間1秒
			timeOperation.addPassTime(); //增加經過時間1秒

			remainTimeLabel.setText(formatTime(timeOperation.getRemainTime())); //設定文字
			passTimeLabel.setText(formatTime(timeOperation.getPassTime())); //設定文字
		}, 0, 1, TimeUnit.SECONDS);
	}

	void pauseTimer()
	{
		if (executorService == null) //已經結束了
			return;
		everySecond.cancel(true);
		executorService.shutdown();
		executorService = null;
	}

	private String formatTime(int[] time)
	{
		return String.format("%05d:%02d:%02d", time[TimeOperation.HOUR], time[TimeOperation.MINUTE], time[TimeOperation.SECOND]);
	}

	@Override
	void onMainWindowClosing()
	{
		pauseTimer();
	}
}