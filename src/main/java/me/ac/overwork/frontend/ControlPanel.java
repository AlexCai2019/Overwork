package me.ac.overwork.frontend;

import me.ac.overwork.backend.TimeOperation;
import me.ac.overwork.frontend.swing_extend.EButton;
import me.ac.overwork.frontend.swing_extend.ELabel;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class ControlPanel extends APanelManager
{
	private static final int MY_PANEL_HEIGHT = MainWindow.HEIGHT * 7 / 10;
	private static final int SUB_PANEL_HEIGHT = MY_PANEL_HEIGHT / 9;

	private boolean isStarted = false; //是否計時中

	private final Font buttonFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 24);
	private final Font textFieldFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 22);
	private final JTextField[] remainTextFields = new JTextField[3]; //剩餘時間輸入框
	private final JTextField[] passTextFields = new JTextField[3]; //經過時間輸入框
	//開始按鈕
	private final JButton startButton = new EButton("\u958b\u59cb", buttonFont, "\u958b\u59cb"); //開始

	ControlPanel()
	{
		myPanel.setBounds(0, MainWindow.HEIGHT * 3 / 10, MainWindow.WIDTH, MY_PANEL_HEIGHT);
		myPanel.setLayout(null); //會放6個物件

		myPanel.add(createFirstRow()); //第二列

		JLabel remainTimeSet = new ELabel("\u5269\u9918\u6642\u9593\u8a2d\u5b9a", textFieldFont); //剩餘時間設定
		remainTimeSet.setBounds(0, SUB_PANEL_HEIGHT, MainWindow.WIDTH, SUB_PANEL_HEIGHT);
		myPanel.add(remainTimeSet);

		myPanel.add(createThirdRow()); //第三列
		myPanel.add(createFourthRow()); //第四列
		myPanel.add(createFifthRow()); //第五列

		JLabel passedTimeSet = new ELabel("\u5df2\u904e\u6642\u9593\u8a2d\u5b9a", textFieldFont); //已過時間設定
		passedTimeSet.setBounds(0, SUB_PANEL_HEIGHT * 5, MainWindow.WIDTH, SUB_PANEL_HEIGHT);
		myPanel.add(passedTimeSet);

		myPanel.add(createSeventhRow()); //第七列
	}

	private JButton createFirstRow()
	{
		startButton.setBounds(MainWindow.WIDTH / 3, 10, MainWindow.WIDTH / 3, SUB_PANEL_HEIGHT);
		startButton.setHorizontalAlignment(SwingConstants.CENTER);
		startButton.addActionListener(event ->
		{
			if (isStarted) //已經開始了
			{
				updateRemainFields(timeOperation.getRemainTime()); //根據後端資料 更新輸入框數字
				updatePassFields(timeOperation.getPassTime()); //根據後端資料 更新輸入框數字
				startButton.setText("\u958b\u59cb"); //開始
				startButton.setToolTipText("\u958b\u59cb"); //開始
				timeOperation.pauseTimer();
				isStarted = false;
			}
			else //已經暫停了
			{
				startButton.setText("\u66ab\u505c"); //暫停
				startButton.setToolTipText("\u66ab\u505c"); //暫停
				timeOperation.startTimer();
				isStarted = true; //變成開始
			}
		});

		return startButton;
	}

	private JPanel createThirdRow()
	{
		JPanel thirdRow = new JPanel(new FlowLayout(FlowLayout.LEFT)); //第三列
		thirdRow.setBounds(0, SUB_PANEL_HEIGHT * 2, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		int[] remainTime = timeOperation.getRemainTime();

		PlainDocument hourDocument = new PlainDocument();
		hourDocument.setDocumentFilter(new TimeTextFieldFilter()); //輸入中偵測
		thirdRow.add(remainTextFields[TimeOperation.HOUR] = createTimeTextField(hourDocument, 5, "\u5c0f\u6642")); //小時

		thirdRow.add(new ELabel("\u6642", textFieldFont)); //時

		PlainDocument minuteDocument = new PlainDocument();
		minuteDocument.setDocumentFilter(new MinuteSecondFieldFilter()); //輸入中偵測
		thirdRow.add(remainTextFields[TimeOperation.MINUTE] = createTimeTextField(minuteDocument, 2, "\u5206\u9418")); //分鐘

		thirdRow.add(new ELabel("\uu5206", textFieldFont)); //分

		PlainDocument secondDocument = new PlainDocument();
		secondDocument.setDocumentFilter(new MinuteSecondFieldFilter()); //輸入中偵測
		thirdRow.add(remainTextFields[TimeOperation.SECOND] = createTimeTextField(secondDocument, 2, "\u79d2")); //秒

		thirdRow.add(new ELabel("\u79d2", textFieldFont)); //秒

		updateRemainFields(remainTime); //根據後端資料 初始化輸入框數字

		//設定按鈕
		JButton setButton = new EButton("\u8a2d\u5b9a", buttonFont, "\u8a2d\u5b9a"); //設定
		setButton.addActionListener(event ->
		{
			timeOperation.setRemainTime(Integer.parseInt(remainTextFields[TimeOperation.HOUR].getText()), TimeUnit.HOURS);
			timeOperation.setRemainTime(Integer.parseInt(remainTextFields[TimeOperation.MINUTE].getText()), TimeUnit.MINUTES);
			timeOperation.setRemainTime(Integer.parseInt(remainTextFields[TimeOperation.SECOND].getText()), TimeUnit.SECONDS);

			MainWindow.getInstance().timePanelManager.updateTimeLabel(); //更新新時間
		});
		thirdRow.add(setButton);

		return thirdRow;
	}

	private JTextField createTimeTextField(Document document, int columns, String toolTip)
	{
		JTextField remainTextField = new JTextField(document, null, columns);
		remainTextField.setFont(textFieldFont);
		remainTextField.setToolTipText(toolTip);
		return remainTextField;
	}

	private JPanel createFourthRow()
	{
		JPanel thirdRow = new JPanel(new FlowLayout(FlowLayout.LEFT)); //第三列
		thirdRow.setBounds(0, SUB_PANEL_HEIGHT * 3, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		JButton add1Hour = new EButton("+60\u5206\u9418", buttonFont, "+60\u5206\u9418"); //+60分鐘
		add1Hour.addActionListener(event ->
		{
			timeOperation.addRemainTime(1, TimeUnit.HOURS);
			MainWindow.getInstance().timePanelManager.updateTimeLabel();
		});
		thirdRow.add(add1Hour);

		JButton add10Minutes = new EButton("+10\u5206\u9418", buttonFont, "+10\u5206\u9418"); //+10分鐘
		add10Minutes.addActionListener(event ->
		{
			timeOperation.addRemainTime(10, TimeUnit.MINUTES);
			MainWindow.getInstance().timePanelManager.updateTimeLabel();
		});
		thirdRow.add(add10Minutes);

		JButton add1Minute = new EButton("+1\u5206\u9418", buttonFont, "+1\u5206\u9418"); //+1分鐘
		add1Minute.addActionListener(event ->
		{
			timeOperation.addRemainTime(1, TimeUnit.MINUTES);
			MainWindow.getInstance().timePanelManager.updateTimeLabel();
		});
		thirdRow.add(add1Minute);

		return thirdRow;
	}

	private JPanel createFifthRow()
	{
		JPanel fourthRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fourthRow.setBounds(0, SUB_PANEL_HEIGHT * 4, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

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
		JButton addButton = new EButton("\u589e\u52a0", buttonFont, "\u589e\u52a0"); //增加
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
			MainWindow.getInstance().timePanelManager.updateTimeLabel(); //更新時間顯示
		});
		fourthRow.add(addButton);

		//減少按鈕
		JButton subtractButton = new EButton("\u6e1b\u5c11", buttonFont, "\u6e1b\u5c11"); //減少
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
			MainWindow.getInstance().timePanelManager.updateTimeLabel(); //更新時間顯示
		});
		fourthRow.add(subtractButton);

		return fourthRow;
	}

	private JPanel createSeventhRow()
	{
		JPanel seventhRow = new JPanel(new FlowLayout(FlowLayout.LEFT)); //第七列
		seventhRow.setBounds(0, SUB_PANEL_HEIGHT * 6, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		int[] passTime = timeOperation.getPassTime();

		PlainDocument hourDocument = new PlainDocument();
		hourDocument.setDocumentFilter(new TimeTextFieldFilter()); //輸入中偵測
		seventhRow.add(passTextFields[TimeOperation.HOUR] = createTimeTextField(hourDocument, 5, "\u5c0f\u6642")); //小時

		seventhRow.add(new ELabel("\u6642", textFieldFont)); //時

		PlainDocument minuteDocument = new PlainDocument();
		minuteDocument.setDocumentFilter(new MinuteSecondFieldFilter()); //輸入中偵測
		seventhRow.add(passTextFields[TimeOperation.MINUTE] = createTimeTextField(minuteDocument, 2, "\u5206\u9418")); //分鐘

		seventhRow.add(new ELabel("\uu5206", textFieldFont)); //分

		PlainDocument secondDocument = new PlainDocument();
		secondDocument.setDocumentFilter(new MinuteSecondFieldFilter()); //輸入中偵測
		seventhRow.add(passTextFields[TimeOperation.SECOND] = createTimeTextField(secondDocument, 2, "\u79d2")); //秒

		seventhRow.add(new ELabel("\u79d2", textFieldFont)); //秒

		updatePassFields(passTime); //根據後端資料 初始化輸入框數字

		//設定按鈕
		JButton setButton = new EButton("\u8a2d\u5b9a", buttonFont, "\u8a2d\u5b9a"); //設定
		setButton.addActionListener(event ->
		{
			timeOperation.setPassTime(Integer.parseInt(passTextFields[TimeOperation.HOUR].getText()), TimeUnit.HOURS);
			timeOperation.setPassTime(Integer.parseInt(passTextFields[TimeOperation.MINUTE].getText()), TimeUnit.MINUTES);
			timeOperation.setPassTime(Integer.parseInt(passTextFields[TimeOperation.SECOND].getText()), TimeUnit.SECONDS);

			MainWindow.getInstance().timePanelManager.updateTimeLabel(); //更新新時間
		});
		seventhRow.add(setButton);

		return seventhRow;
	}

	public void updateRemainFields(int[] newTime)
	{
		//從後端獲得更新
		remainTextFields[TimeOperation.HOUR].setText(Integer.toString(newTime[TimeOperation.HOUR]));
		remainTextFields[TimeOperation.MINUTE].setText(Integer.toString(newTime[TimeOperation.MINUTE]));
		remainTextFields[TimeOperation.SECOND].setText(Integer.toString(newTime[TimeOperation.SECOND]));
	}

	public void updatePassFields(int[] newTime)
	{
		//從後端獲得更新
		passTextFields[TimeOperation.HOUR].setText(Integer.toString(newTime[TimeOperation.HOUR]));
		passTextFields[TimeOperation.MINUTE].setText(Integer.toString(newTime[TimeOperation.MINUTE]));
		passTextFields[TimeOperation.SECOND].setText(Integer.toString(newTime[TimeOperation.SECOND]));
	}
}