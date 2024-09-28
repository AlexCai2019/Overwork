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

	private boolean isStarted = false; //是否計時中

	private final Font buttonFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 24);
	private final Font textFieldFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 22);
	private final JTextField[] remainTextFields = new JTextField[3]; //剩餘時間輸入框
	//開始按鈕
	private final JButton startButton = new JButton(); //開始

	ControlPanel()
	{
		myPanel.setBounds(0, MainWindow.HEIGHT * 3 / 10, MainWindow.WIDTH, MY_PANEL_HEIGHT);
		myPanel.setLayout(null); //會放6個物件

		myPanel.add(createFirstRow()); //第一列
		myPanel.add(createSecondRow()); //第二列
		myPanel.add(createThirdRow()); //第三列
		myPanel.add(createFourthRow()); //第四列
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
		JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.LEFT)); //第一列
		firstRow.setBounds(0, 0, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		resetToStopState();
		startButton.setFont(buttonFont);
		startButton.addActionListener(event ->
		{
			if (isStarted) //已經開始了
			{
				resetToStopState();
				updateRemainFields(timeOperation.getRemainTime()); //根據後端資料 更新輸入框數字
				return;
			}

			//已經暫停了
			startButton.setText("\u66ab\u505c"); //暫停
			startButton.setToolTipText("\u66ab\u505c"); //暫停
			timeOperation.startTimer();
			isStarted = true; //變成開始
		});
		firstRow.add(startButton);

		//歸零按鈕
		JButton zeroButton = new JButton("\u6b78\u96f6"); //歸零
		zeroButton.setFont(buttonFont);
		zeroButton.setToolTipText("\u6b78\u96f6"); //歸零
		zeroButton.addActionListener(event->
		{
			resetToStopState(); //狀態設為未開始 並暫停計時器

			//輸入框歸零
			int[] zeroRemainTime = {0, 0, 0};
			updateRemainFields(zeroRemainTime);

			//後端更新數字
			timeOperation.setRemainTime(zeroRemainTime);

			//前端時間面板顯示數字歸零
			MainWindow.instance.timePanelManager.updateTimeLabel();
		});
		firstRow.add(zeroButton);

		return firstRow;
	}

	private void resetToStopState()
	{
		isStarted = false;
		startButton.setText("\u958b\u59cb"); //"開始"
		startButton.setToolTipText("\u958b\u59cb"); //開始
		timeOperation.pauseTimer();
	}

	private JPanel createSecondRow()
	{
		JPanel secondRow = new JPanel(new FlowLayout(FlowLayout.LEFT)); //第二列
		secondRow.setBounds(0, SUB_PANEL_HEIGHT, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		int[] remainTime = timeOperation.getRemainTime();

		PlainDocument hourDocument = new PlainDocument();
		hourDocument.setDocumentFilter(new TimeTextFieldFilter()); //輸入中偵測
		remainTextFields[TimeOperation.HOUR] = new JTextField(hourDocument, null, 5); //小時輸入框
		remainTextFields[TimeOperation.HOUR].setFont(textFieldFont);
		remainTextFields[TimeOperation.HOUR].setToolTipText("\u5c0f\u6642"); //小時
		secondRow.add(remainTextFields[TimeOperation.HOUR]);

		JLabel hourLabel = new JLabel("\u6642"); //時
		hourLabel.setFont(textFieldFont);
		secondRow.add(hourLabel);

		PlainDocument minuteDocument = new PlainDocument();
		minuteDocument.setDocumentFilter(new MinuteSecondFieldFilter()); //輸入中偵測
		remainTextFields[TimeOperation.MINUTE] = new JTextField(minuteDocument, null, 2); //分鐘輸入框
		remainTextFields[TimeOperation.MINUTE].setFont(textFieldFont);
		remainTextFields[TimeOperation.MINUTE].setToolTipText("\u5206\u9418"); //分鐘
		secondRow.add(remainTextFields[TimeOperation.MINUTE]);

		JLabel minuteLabel = new JLabel("\uu5206"); //分
		minuteLabel.setFont(textFieldFont);
		secondRow.add(minuteLabel);

		PlainDocument secondDocument = new PlainDocument();
		secondDocument.setDocumentFilter(new MinuteSecondFieldFilter()); //輸入中偵測
		remainTextFields[TimeOperation.SECOND] = new JTextField(secondDocument, null, 2); //秒輸入框
		remainTextFields[TimeOperation.SECOND].setFont(textFieldFont);
		remainTextFields[TimeOperation.SECOND].setToolTipText("\u79d2"); //秒
		secondRow.add(remainTextFields[TimeOperation.SECOND]);

		JLabel secondLabel = new JLabel("\u79d2"); //秒
		secondLabel.setFont(textFieldFont);
		secondRow.add(secondLabel);

		updateRemainFields(remainTime); //根據後端資料 更新輸入框數字

		//設定按鈕
		JButton setButton = new JButton("\u8a2d\u5b9a"); //設定
		setButton.setFont(buttonFont);
		setButton.setToolTipText("\u8a2d\u5b9a"); //設定
		setButton.addActionListener(event ->
		{
			timeOperation.setRemainTime(Integer.parseInt(remainTextFields[TimeOperation.HOUR].getText()), TimeUnit.HOURS);
			timeOperation.setRemainTime(Integer.parseInt(remainTextFields[TimeOperation.MINUTE].getText()), TimeUnit.MINUTES);
			timeOperation.setRemainTime(Integer.parseInt(remainTextFields[TimeOperation.SECOND].getText()), TimeUnit.SECONDS);

			MainWindow.instance.timePanelManager.updateTimeLabel(); //更新新時間
		});
		secondRow.add(setButton);

		return secondRow;
	}

	private JPanel createThirdRow()
	{
		JPanel thirdRow = new JPanel(new FlowLayout(FlowLayout.LEFT)); //第三列
		thirdRow.setBounds(0, SUB_PANEL_HEIGHT * 2, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		JButton add1Hour = new JButton("+60\u5206\u9418"); //+60分鐘
		add1Hour.setFont(buttonFont);
		add1Hour.setToolTipText("+60\u5206\u9418");
		add1Hour.addActionListener(event ->
		{
			timeOperation.addRemainTime(1, TimeUnit.HOURS);
			MainWindow.instance.timePanelManager.updateTimeLabel();
		});
		thirdRow.add(add1Hour);

		JButton add10Minutes = new JButton("+10\u5206\u9418"); //+10分鐘
		add10Minutes.setFont(buttonFont);
		add10Minutes.setToolTipText("+10\u5206\u9418"); //+10分鐘
		add10Minutes.addActionListener(event ->
		{
			timeOperation.addRemainTime(10, TimeUnit.MINUTES);
			MainWindow.instance.timePanelManager.updateTimeLabel();
		});
		thirdRow.add(add10Minutes);

		JButton add1Minute = new JButton("+1\u5206\u9418"); //+1分鐘
		add1Minute.setFont(buttonFont);
		add1Minute.setToolTipText("+1\u5206\u9418"); //+1分鐘
		add1Minute.addActionListener(event ->
		{
			timeOperation.addRemainTime(1, TimeUnit.MINUTES);
			MainWindow.instance.timePanelManager.updateTimeLabel();
		});
		thirdRow.add(add1Minute);

		return thirdRow;
	}

	private JPanel createFourthRow()
	{
		JPanel fourthRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fourthRow.setBounds(0, SUB_PANEL_HEIGHT * 3, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		PlainDocument timeDocument = new PlainDocument();
		timeDocument.setDocumentFilter(new TimeTextFieldFilter()); //輸入中偵測

		JTextField timeField = new JTextField(timeDocument, "0", 5); //時間輸入框
		timeField.setFont(textFieldFont);
		timeField.setToolTipText("\u6642\u9593"); //時間
		fourthRow.add(timeField);

		//選單
		String[] timeSelect = new String[3];
		timeSelect[TimeOperation.HOUR] = "\u5c0f\u6642"; //小時
		timeSelect[TimeOperation.MINUTE] = "\u5206\u9418"; //分鐘
		timeSelect[TimeOperation.SECOND] = "\u79d2"; //秒
		JComboBox<String> unitDropDown = new JComboBox<>(timeSelect);
		unitDropDown.setFont(textFieldFont);
		unitDropDown.setToolTipText("\u55ae\u4f4d"); //單位
		unitDropDown.setSelectedIndex(TimeOperation.MINUTE);
		fourthRow.add(unitDropDown);

		//增加按鈕
		JButton addButton = new JButton("\u589e\u52a0"); //增加
		addButton.setFont(buttonFont);
		addButton.setToolTipText("\u589e\u52a0"); //增加
		addButton.addActionListener(event ->
		{
			String addString = timeField.getText();
			if (addString == null || addString.isEmpty())
				return;
			timeOperation.addRemainTime(Integer.parseInt(addString), switch (unitDropDown.getSelectedIndex())
			{
				case TimeOperation.HOUR -> TimeUnit.HOURS;
				case TimeOperation.MINUTE -> TimeUnit.MINUTES;
				default -> TimeUnit.SECONDS;
			});
		});
		fourthRow.add(addButton);

		//減少按鈕
		JButton subtractButton = new JButton("\u6e1b\u5c11"); //減少
		subtractButton.setFont(buttonFont);
		subtractButton.setToolTipText("\u6e1b\u5c11"); //減少
		subtractButton.addActionListener(event ->
		{
			String subtractString = timeField.getText();
			if (subtractString == null || subtractString.isEmpty())
				return;
			timeOperation.subtractRemainTime(Integer.parseInt(subtractString), switch (unitDropDown.getSelectedIndex())
			{
				case TimeOperation.HOUR -> TimeUnit.HOURS;
				case TimeOperation.MINUTE -> TimeUnit.MINUTES;
				default -> TimeUnit.SECONDS;
			});
		});
		fourthRow.add(subtractButton);

		return fourthRow;
	}

	public void updateRemainFields(int[] newTime)
	{
		//從後端獲得更新
		remainTextFields[TimeOperation.HOUR].setText(Integer.toString(newTime[TimeOperation.HOUR]));
		remainTextFields[TimeOperation.MINUTE].setText(Integer.toString(newTime[TimeOperation.MINUTE]));
		remainTextFields[TimeOperation.SECOND].setText(Integer.toString(newTime[TimeOperation.SECOND]));
	}
}