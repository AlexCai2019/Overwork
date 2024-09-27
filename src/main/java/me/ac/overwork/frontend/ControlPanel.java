package me.ac.overwork.frontend;

import me.ac.overwork.backend.TimeOperation;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class ControlPanel extends APanelManager
{
	private static final int MY_PANEL_HEIGHT = MainWindow.HEIGHT * 7 / 10;
	private static final int SUB_PANEL_HEIGHT = MY_PANEL_HEIGHT / 7;

	private boolean isStarted = false;

	private final Font buttonFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 24);
	private final Font textFieldFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 22);
	private final JTextField[] timeFields = new JTextField[3];

	ControlPanel()
	{
		myPanel.setBounds(0, MainWindow.HEIGHT * 3 / 10, MainWindow.WIDTH, MY_PANEL_HEIGHT);
		myPanel.setLayout(null); //會放6個物件

		myPanel.add(createFirstRow()); //第一列
		myPanel.add(createSecondRow()); //第二列
		myPanel.add(createThirdRow()); //第三列
		JPanel p4 = new JPanel();
		p4.add(new JLabel("4"));
		p4.setBounds(0, SUB_PANEL_HEIGHT * 3, MainWindow.WIDTH, SUB_PANEL_HEIGHT);
		myPanel.add(p4);
		JPanel p5 = new JPanel();
		p5.add(new JLabel("5"));
		p5.setBounds(0, SUB_PANEL_HEIGHT * 4, MainWindow.WIDTH, SUB_PANEL_HEIGHT);
		myPanel.add(p5);
		JPanel p6 = new JPanel();
		p6.add(new JLabel("6"));
		p6.setBounds(0, SUB_PANEL_HEIGHT * 5, MainWindow.WIDTH, SUB_PANEL_HEIGHT);
		myPanel.add(p6);
	}

	private JPanel createFirstRow()
	{
		//Panel firstRow = new Panel(new GridLayout(1, 2)); //第一列
		JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.LEFT)); //第一列
		firstRow.setBounds(0, 0, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		//開始按鈕
		JButton startButton = new JButton("\u958b\u59cb"); //開始
		startButton.setFont(buttonFont);
		startButton.addActionListener(event ->
		{
			if (isStarted) //已經開始了
			{
				startButton.setText("\u958b\u59cb"); //開始
				timeOperation.pauseTimer(); //暫停計時器
				isStarted = false; //變成暫停
			}
			else //已經暫停了
			{
				//後端更新
				timeOperation.setRemainTime(Integer.parseInt(timeFields[TimeOperation.HOUR].getText()), TimeUnit.HOURS);
				timeOperation.setRemainTime(Integer.parseInt(timeFields[TimeOperation.MINUTE].getText()), TimeUnit.MINUTES);
				timeOperation.setRemainTime(Integer.parseInt(timeFields[TimeOperation.SECOND].getText()), TimeUnit.SECONDS);

				startButton.setText("\u66ab\u505c"); //暫停
				timeOperation.startTimer();
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
			updateRemainFields(new int[] {0, 0, 0});
			//後端更新並暫停計時器
			timeOperation.setRemainTime(0, TimeUnit.HOURS);
			timeOperation.setRemainTime(0, TimeUnit.MINUTES);
			timeOperation.setRemainTime(0, TimeUnit.SECONDS);
			timeOperation.pauseTimer();
			//前端顯示數字歸零
			MainWindow.instance.timePanelManager.updateTimeLabel();
		});
		firstRow.add(zeroButton);

		return firstRow;
	}

	private JPanel createSecondRow()
	{
		//Panel secondRow = new Panel(new GridLayout(1, 6)); //第二列
		JPanel secondRow = new JPanel(new FlowLayout(FlowLayout.LEFT)); //第二列
		secondRow.setBounds(0, SUB_PANEL_HEIGHT, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		int[] remainTime = timeOperation.getRemainTime();

		PlainDocument hourDocument = new PlainDocument();
		hourDocument.setDocumentFilter(new HourTextFieldFilter(remainTime, TimeOperation.HOUR)); //輸入中偵測
		timeFields[TimeOperation.HOUR] = new JTextField(hourDocument, null, 5); //小時輸入框
		timeFields[TimeOperation.HOUR].setFont(textFieldFont);
		secondRow.add(timeFields[TimeOperation.HOUR]);

		JLabel hourLabel = new JLabel("\u6642"); //時
		hourLabel.setFont(textFieldFont);
		secondRow.add(hourLabel);

		PlainDocument minuteDocument = new PlainDocument();
		minuteDocument.setDocumentFilter(new MinuteSecondFieldFilter(remainTime, TimeOperation.MINUTE)); //輸入中偵測
		timeFields[TimeOperation.MINUTE] = new JTextField(minuteDocument, null, 2); //分鐘輸入框
		timeFields[TimeOperation.MINUTE].setFont(textFieldFont);
		secondRow.add(timeFields[TimeOperation.MINUTE]);

		JLabel minuteLabel = new JLabel("\uu5206"); //分
		minuteLabel.setFont(textFieldFont);
		secondRow.add(minuteLabel);

		PlainDocument secondDocument = new PlainDocument();
		secondDocument.setDocumentFilter(new MinuteSecondFieldFilter(remainTime, TimeOperation.SECOND)); //輸入中偵測
		timeFields[TimeOperation.SECOND] = new JTextField(secondDocument, null, 2); //秒輸入框
		timeFields[TimeOperation.SECOND].setFont(textFieldFont);
		secondRow.add(timeFields[TimeOperation.SECOND]);

		JLabel secondLabel = new JLabel("\u79d2"); //秒
		secondLabel.setFont(textFieldFont);
		secondRow.add(secondLabel);

		updateRemainFields(remainTime);

		//設定按鈕
		JButton setButton = new JButton("\u8a2d\u5b9a"); //設定
		setButton.setFont(buttonFont);
		setButton.addActionListener(event ->
		{
			timeOperation.setRemainTime(Integer.parseInt(timeFields[TimeOperation.HOUR].getText()), TimeUnit.HOURS);
			timeOperation.setRemainTime(Integer.parseInt(timeFields[TimeOperation.MINUTE].getText()), TimeUnit.MINUTES);
			timeOperation.setRemainTime(Integer.parseInt(timeFields[TimeOperation.SECOND].getText()), TimeUnit.SECONDS);

			MainWindow.instance.timePanelManager.updateTimeLabel(); //更新新時間
		});
		secondRow.add(setButton);

		return secondRow;
	}

	private JPanel createThirdRow()
	{
		JPanel thirdRow = new JPanel(new FlowLayout(FlowLayout.LEFT)); //第三列
		thirdRow.setBounds(0, SUB_PANEL_HEIGHT * 2, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		JButton add1Hour = new JButton("+60\u5206\u9418"); //+1小時
		add1Hour.setFont(buttonFont);
		add1Hour.addActionListener(event ->
		{
			timeOperation.addRemainTime(1, TimeUnit.HOURS);
			MainWindow.instance.timePanelManager.updateTimeLabel();
		});
		thirdRow.add(add1Hour);

		JButton add10Minutes = new JButton("+10\u5206\u9418"); //+10分鐘
		add10Minutes.setFont(buttonFont);
		add10Minutes.addActionListener(event ->
		{
			timeOperation.addRemainTime(10, TimeUnit.MINUTES);
			MainWindow.instance.timePanelManager.updateTimeLabel();
		});
		thirdRow.add(add10Minutes);

		JButton add1Minute = new JButton("+1\u5206\u9418"); //+1分鐘
		add1Minute.setFont(buttonFont);
		add1Minute.addActionListener(event ->
		{
			timeOperation.addRemainTime(1, TimeUnit.MINUTES);
			MainWindow.instance.timePanelManager.updateTimeLabel();
		});
		thirdRow.add(add1Minute);

		return thirdRow;
	}

	public void updateRemainFields(int[] newTime)
	{
		//從後端獲得更新
		timeFields[TimeOperation.HOUR].setText(Integer.toString(newTime[TimeOperation.HOUR]));
		timeFields[TimeOperation.MINUTE].setText(Integer.toString(newTime[TimeOperation.MINUTE]));
		timeFields[TimeOperation.SECOND].setText(Integer.toString(newTime[TimeOperation.SECOND]));
	}
}