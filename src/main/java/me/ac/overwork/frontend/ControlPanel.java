package me.ac.overwork.frontend;

import me.ac.overwork.backend.TimeOperation;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class ControlPanel extends APanelManager
{
	private boolean isStarted = false;

	private final Font buttonFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 24);
	private final JFormattedTextField[] timeFields = new JFormattedTextField[3];

	ControlPanel()
	{
		myPanel.setLayout(new GridLayout(6, 1)); //會放6個物件

		myPanel.add(createFirstRow()); //第一列
		myPanel.add(createSecondRow()); //第二列
	}

	private Panel createFirstRow()
	{
		NumberFormatter hourFormatter = new NumberFormatter(NumberFormat.getIntegerInstance()); //小時的格式
		NumberFormatter minuteSecondFormatter = new NumberFormatter(NumberFormat.getIntegerInstance()); //分鐘和秒的格式
		hourFormatter.setMinimum(0);
		minuteSecondFormatter.setMinimum(0);
		hourFormatter.setMaximum(99999);
		minuteSecondFormatter.setMaximum(59);

		Panel firstRow = new Panel(new GridLayout(1, 6)); //第一列

		timeFields[TimeOperation.HOUR] = new JFormattedTextField(hourFormatter); //小時輸入框
		firstRow.add(timeFields[TimeOperation.HOUR]);
		timeFields[TimeOperation.MINUTE] = new JFormattedTextField(minuteSecondFormatter); //分鐘輸入框
		firstRow.add(timeFields[TimeOperation.MINUTE]);
		timeFields[TimeOperation.SECOND] = new JFormattedTextField(minuteSecondFormatter); //秒輸入框
		firstRow.add(timeFields[TimeOperation.SECOND]);
		updateTimeFields(timeOperation.getRemainTime());

		firstRowButton(firstRow); //第一列的按鈕

		return firstRow;
	}

	private void firstRowButton(Panel firstRow)
	{
		//開始按鈕
		JButton startButton = new JButton("\u958b\u59cb"); //開始
		startButton.setFont(buttonFont);
		startButton.addActionListener(event ->
		{
			if (isStarted) //已經開始了
			{
				startButton.setText("\u958b\u59cb"); //開始
				MainWindow.instance.timePanelManager.pauseTimer();
				isStarted = false; //變成暫停

				//重新讀取後端儲存的時間 放進輸入框中的數字
				updateTimeFields(timeOperation.getRemainTime());
			}
			else //已經暫停了
			{
				//後端更新
				timeOperation.setRemainTime((Integer) timeFields[TimeOperation.HOUR].getValue(), TimeUnit.HOURS);
				timeOperation.setRemainTime((Integer) timeFields[TimeOperation.MINUTE].getValue(), TimeUnit.MINUTES);
				timeOperation.setRemainTime((Integer) timeFields[TimeOperation.SECOND].getValue(), TimeUnit.SECONDS);

				startButton.setText("\u66ab\u505c"); //暫停
				MainWindow.instance.timePanelManager.startTimer();
				isStarted = true; //變成開始
			}
		});
		firstRow.add(startButton);

		//歸零按鈕
		JButton zeroButton = new JButton("\u6b78\u96f6"); //歸零
		zeroButton.setFont(buttonFont);
		zeroButton.addActionListener(event->
		{
			isStarted = false;
			startButton.setText("\u958b\u59cb"); //按鈕文字設為"開始"
			//輸入框歸零
			updateTimeFields(new int[] {0, 0, 0});
			//後端更新
			timeOperation.setRemainTime(0, TimeUnit.HOURS);
			timeOperation.setRemainTime(0, TimeUnit.MINUTES);
			timeOperation.setRemainTime(0, TimeUnit.SECONDS);
			//前端顯示數字歸零
			MainWindow.instance.timePanelManager.pauseTimer();
			MainWindow.instance.timePanelManager.updateTimeLabel();
		});
		firstRow.add(zeroButton);
	}

	void updateTimeFields(int[] newTime)
	{
		//更新輸入框內的數字
		timeFields[TimeOperation.HOUR].setText(Integer.toString(newTime[TimeOperation.HOUR]));
		timeFields[TimeOperation.HOUR].setValue(newTime[TimeOperation.HOUR]);
		timeFields[TimeOperation.MINUTE].setText(Integer.toString(newTime[TimeOperation.MINUTE]));
		timeFields[TimeOperation.MINUTE].setValue(newTime[TimeOperation.MINUTE]);
		timeFields[TimeOperation.SECOND].setText(Integer.toString(newTime[TimeOperation.SECOND]));
		timeFields[TimeOperation.SECOND].setValue(newTime[TimeOperation.SECOND]);
	}

	private Panel createSecondRow()
	{
		Panel secondRow = new Panel(new GridLayout(1, 4)); //第二列

		JButton add1Hour = new JButton("+ 60"); //+1小時
		add1Hour.setFont(buttonFont);
		add1Hour.addActionListener(event -> timeOperation.addRemainTime(1, TimeUnit.HOURS));
		secondRow.add(add1Hour);

		return secondRow;
	}

	@Override
	void onMainWindowClosing() {}
}