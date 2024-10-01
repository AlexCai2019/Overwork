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
	private static final int TEXT_HEIGHT = MY_PANEL_HEIGHT / 10;
	private static final int SUB_PANEL_HEIGHT = MY_PANEL_HEIGHT / 9;
	private static final int LEFT_PADDING = 10;

	private boolean isStarted = false; //是否計時中

	private final Font textFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 26);
	private final Font buttonFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 24);
	private final Font textFieldFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 22);
	private final JTextField[] remainTextFields = new JTextField[3]; //剩餘時間輸入框
	private final JTextField[] passTextFields = new JTextField[3]; //經過時間輸入框
	//開始按鈕
	private final JButton startButton = new EButton("\u958b\u59cb", buttonFont, "\u958b\u59cb\u8a08\u6642"); //開始 開始計時

	ControlPanel()
	{
		myPanel.setBounds(0, MainWindow.HEIGHT * 3 / 10, MainWindow.WIDTH, MY_PANEL_HEIGHT);
		myPanel.setLayout(null);

		myPanel.add(createFirstRow()); //第二列

		JLabel remainTimeSet = new ELabel("\u5269\u9918\u6642\u9593\u8a2d\u5b9a", textFont); //剩餘時間設定
		remainTimeSet.setBounds(LEFT_PADDING, SUB_PANEL_HEIGHT, MainWindow.WIDTH, TEXT_HEIGHT);
		myPanel.add(remainTimeSet);

		myPanel.add(createThirdRow()); //第三列
		myPanel.add(createFourthRow()); //第四列
		myPanel.add(createFifthRow()); //第五列

		JLabel passedTimeSet = new ELabel("\u5df2\u904e\u6642\u9593\u8a2d\u5b9a", textFont); //已過時間設定
		passedTimeSet.setBounds(LEFT_PADDING, TEXT_HEIGHT + SUB_PANEL_HEIGHT * 4, MainWindow.WIDTH, TEXT_HEIGHT);
		myPanel.add(passedTimeSet);

		myPanel.add(createSeventhRow()); //第七列
		myPanel.add(createEighthPanel()); //第八列
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
				startButton.setToolTipText("\u958b\u59cb\u8a08\u6642"); //開始計時
				timeOperation.pauseTimer();
				isStarted = false;
			}
			else //已經暫停了
			{
				startButton.setText("\u66ab\u505c"); //暫停
				startButton.setToolTipText("\u66ab\u505c\u8a08\u6642"); //暫停計時
				timeOperation.startTimer();
				isStarted = true; //變成開始
			}
		});

		return startButton;
	}

	private JPanel createThirdRow()
	{
		JPanel thirdRow = new JPanel(new FlowLayout(FlowLayout.LEFT)); //第三列
		thirdRow.setBounds(LEFT_PADDING, TEXT_HEIGHT + SUB_PANEL_HEIGHT, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		int[] remainTime = timeOperation.getRemainTime();

		PlainDocument hourDocument = new PlainDocument();
		hourDocument.setDocumentFilter(new TimeTextFieldFilter()); //輸入中偵測
		thirdRow.add(remainTextFields[TimeOperation.HOUR] = createTimeTextField(hourDocument, 5, "\u5c0f\u6642")); //小時

		thirdRow.add(new ELabel("\u6642", textFont)); //時

		PlainDocument minuteDocument = new PlainDocument();
		minuteDocument.setDocumentFilter(new MinuteSecondFieldFilter()); //輸入中偵測
		thirdRow.add(remainTextFields[TimeOperation.MINUTE] = createTimeTextField(minuteDocument, 2, "\u5206\u9418")); //分鐘

		thirdRow.add(new ELabel("\uu5206", textFont)); //分

		PlainDocument secondDocument = new PlainDocument();
		secondDocument.setDocumentFilter(new MinuteSecondFieldFilter()); //輸入中偵測
		thirdRow.add(remainTextFields[TimeOperation.SECOND] = createTimeTextField(secondDocument, 2, "\u79d2")); //秒

		thirdRow.add(new ELabel("\u79d2", textFont)); //秒

		updateRemainFields(remainTime); //根據後端資料 初始化輸入框數字

		//設定按鈕
		JButton setButton = new EButton("\u8a2d\u5b9a", buttonFont, "\u8a2d\u5b9a\u5269\u9918\u6642\u9593"); //設定 設定剩餘時間
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
		JPanel fourthRow = new JPanel(new FlowLayout(FlowLayout.LEFT)); //第三列
		fourthRow.setBounds(LEFT_PADDING, TEXT_HEIGHT + SUB_PANEL_HEIGHT * 2, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		JButton add1Hour = new EButton("+60\u5206\u9418", buttonFont); //+60分鐘
		add1Hour.addActionListener(event ->
		{
			timeOperation.addRemainTime(1, TimeUnit.HOURS);
			MainWindow.getInstance().timePanelManager.updateTimeLabel();
		});
		fourthRow.add(add1Hour);

		JButton add10Minutes = new EButton("+10\u5206\u9418", buttonFont); //+10分鐘
		add10Minutes.addActionListener(event ->
		{
			timeOperation.addRemainTime(10, TimeUnit.MINUTES);
			MainWindow.getInstance().timePanelManager.updateTimeLabel();
		});
		fourthRow.add(add10Minutes);

		JButton add1Minute = new EButton("+1\u5206\u9418", buttonFont); //+1分鐘
		add1Minute.addActionListener(event ->
		{
			timeOperation.addRemainTime(1, TimeUnit.MINUTES);
			MainWindow.getInstance().timePanelManager.updateTimeLabel();
		});
		fourthRow.add(add1Minute);

		return fourthRow;
	}

	private JPanel createFifthRow()
	{
		JPanel fifthRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		fifthRow.setBounds(LEFT_PADDING, TEXT_HEIGHT + SUB_PANEL_HEIGHT * 3, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		PlainDocument timeDocument = new PlainDocument();
		timeDocument.setDocumentFilter(new TimeTextFieldFilter()); //輸入中偵測

		//時間輸入框
		JTextField timeField = createTimeTextField(timeDocument, 5, "\u6642\u9593"); //時間
		timeField.setText("0");
		fifthRow.add(timeField);

		//選單
		String[] timeSelect = new String[3];
		timeSelect[TimeOperation.HOUR] = "\u5c0f\u6642"; //小時
		timeSelect[TimeOperation.MINUTE] = "\u5206\u9418"; //分鐘
		timeSelect[TimeOperation.SECOND] = "\u79d2"; //秒
		JComboBox<String> unitDropDown = new JComboBox<>(timeSelect); //下拉式選單
		unitDropDown.setFont(textFieldFont);
		unitDropDown.setToolTipText("\u55ae\u4f4d"); //單位
		unitDropDown.setSelectedIndex(TimeOperation.MINUTE); //選擇分鐘
		fifthRow.add(unitDropDown);

		//增加按鈕
		JButton addButton = new EButton("\u589e\u52a0", buttonFont, "\u589e\u52a0"); //增加
		addButton.addActionListener(event ->
		{
			String addString = timeField.getText(); //從時間輸入框獲得
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
		fifthRow.add(addButton);

		//減少按鈕
		JButton subtractButton = new EButton("\u6e1b\u5c11", buttonFont, "\u6e1b\u5c11"); //減少
		subtractButton.addActionListener(event ->
		{
			String subtractString = timeField.getText(); //從時間輸入框獲得
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
		fifthRow.add(subtractButton);

		return fifthRow;
	}

	private JPanel createSeventhRow()
	{
		JPanel seventhRow = new JPanel(new FlowLayout(FlowLayout.LEFT)); //第七列
		seventhRow.setBounds(LEFT_PADDING, TEXT_HEIGHT * 2 + SUB_PANEL_HEIGHT * 4, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		int[] passTime = timeOperation.getPassTime();

		PlainDocument hourDocument = new PlainDocument();
		hourDocument.setDocumentFilter(new TimeTextFieldFilter()); //輸入中偵測
		seventhRow.add(passTextFields[TimeOperation.HOUR] = createTimeTextField(hourDocument, 5, "\u5c0f\u6642")); //小時

		seventhRow.add(new ELabel("\u6642", textFont)); //時

		PlainDocument minuteDocument = new PlainDocument();
		minuteDocument.setDocumentFilter(new MinuteSecondFieldFilter()); //輸入中偵測
		seventhRow.add(passTextFields[TimeOperation.MINUTE] = createTimeTextField(minuteDocument, 2, "\u5206\u9418")); //分鐘

		seventhRow.add(new ELabel("\uu5206", textFont)); //分

		PlainDocument secondDocument = new PlainDocument();
		secondDocument.setDocumentFilter(new MinuteSecondFieldFilter()); //輸入中偵測
		seventhRow.add(passTextFields[TimeOperation.SECOND] = createTimeTextField(secondDocument, 2, "\u79d2")); //秒

		seventhRow.add(new ELabel("\u79d2", textFont)); //秒

		updatePassFields(passTime); //根據後端資料 初始化輸入框數字

		//設定按鈕
		JButton setButton = new EButton("\u8a2d\u5b9a", buttonFont, "\u8a2d\u5b9a\u5df2\u904e\u6642\u9593"); //設定 設定已過時間
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

	private JPanel createEighthPanel()
	{
		JPanel eighthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		eighthPanel.setBounds(LEFT_PADDING, TEXT_HEIGHT * 2 + SUB_PANEL_HEIGHT * 5, MainWindow.WIDTH, SUB_PANEL_HEIGHT);

		JButton popOutRemainButton = new EButton("\u5269\u9918\u6642\u9593\u8996\u7a97", buttonFont, "\u5f48\u51fa\u5269\u9918\u6642\u9593\u8996\u7a97"); //剩餘時間視窗 彈出剩餘時間視窗
		popOutRemainButton.addActionListener(event -> MainWindow.getInstance().remainTimeWindow.setVisible());
		eighthPanel.add(popOutRemainButton);

		JButton popOutPassButton = new EButton("\u5df2\u904e\u6642\u9593\u8996\u7a97", buttonFont, "\u5f48\u51fa\u5df2\u904e\u6642\u9593\u8996\u7a97"); //已過時間視窗 彈出已過時間視窗
		popOutPassButton.addActionListener(event -> MainWindow.getInstance().passTimeWindow.setVisible());
		eighthPanel.add(popOutPassButton);

		return eighthPanel;
	}

	private void updateRemainFields(int[] newTime)
	{
		//從後端獲得更新
		remainTextFields[TimeOperation.HOUR].setText(Integer.toString(newTime[TimeOperation.HOUR]));
		remainTextFields[TimeOperation.MINUTE].setText(Integer.toString(newTime[TimeOperation.MINUTE]));
		remainTextFields[TimeOperation.SECOND].setText(Integer.toString(newTime[TimeOperation.SECOND]));
	}

	private void updatePassFields(int[] newTime)
	{
		//從後端獲得更新
		passTextFields[TimeOperation.HOUR].setText(Integer.toString(newTime[TimeOperation.HOUR]));
		passTextFields[TimeOperation.MINUTE].setText(Integer.toString(newTime[TimeOperation.MINUTE]));
		passTextFields[TimeOperation.SECOND].setText(Integer.toString(newTime[TimeOperation.SECOND]));
	}
}