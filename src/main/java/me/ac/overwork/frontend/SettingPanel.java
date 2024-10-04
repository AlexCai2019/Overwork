package me.ac.overwork.frontend;

import me.ac.overwork.frontend.swing_extend.EButton;
import me.ac.overwork.frontend.swing_extend.EColor;
import me.ac.overwork.frontend.swing_extend.ELabel;
import me.ac.overwork.frontend.swing_extend.ETextField;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.Locale;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class SettingPanel extends PanelParent
{
	private static final int SUB_PANEL_HEIGHT = 100;
	private static final int BUTTON_HEIGHT = 30;
	private final Font borderFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 12);
	private final Font textFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 18);
	private final Font buttonFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 16);
	private final JTextField[] colorFields = new JTextField[2];

	private static final int REMAIN_INDEX = 0;
	private static final int PASS_INDEX = 1;

	SettingPanel()
	{
		super(); //設定

		myPanel.setLayout(null);

		JPanel remainPanel = createTimeOptionPanel("\u5269\u9918", REMAIN_INDEX, colorOperation.remainTimeColor); //剩餘
		myPanel.add(remainPanel);

		JPanel passPanel = createTimeOptionPanel("\u7d93\u904e", PASS_INDEX, colorOperation.passTimeColor); //經過
		myPanel.add(passPanel);

		myPanel.add(createConfirmButton());
		myPanel.add(createCancelButton());
	}

	private JPanel createTimeOptionPanel(String title, int index, int initialValue)
	{
		JPanel timeOptionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		timeOptionsPanel.setBounds(10, 10 + SUB_PANEL_HEIGHT * index, MainWindow.WIDTH - 40, SUB_PANEL_HEIGHT);
		TitledBorder titledBorder = BorderFactory.createTitledBorder(title + "\u6642\u9593");
		titledBorder.setTitleFont(borderFont);
		timeOptionsPanel.setBorder(titledBorder); //title時間

		JLabel colorLabel = new ELabel("\u984f\u8272   #", textFont); //顏色   #
		timeOptionsPanel.add(colorLabel);

		PlainDocument colorDocument = new PlainDocument();
		colorDocument.setDocumentFilter(new ColorFieldFilter()); //輸入中偵測
		colorFields[index] = new ETextField(colorDocument, 6, String.format("%06X", initialValue),
				textFont, "\u8a2d\u5b9a" + title + "\u6642\u9593\u984f\u8272(16\u9032\u4f4d\u8272\u78bc)"); //設定title時間顏色(16進位色碼)
		timeOptionsPanel.add(colorFields[index]);

		JButton chooserButton = new EButton("\u9078\u64c7", buttonFont, "\u9078\u64c7\u984f\u8272"); //選擇 選擇顏色
		JColorChooser.setDefaultLocale(Locale.TRADITIONAL_CHINESE); //語言預設繁體中文 (好像沒效果)
		chooserButton.addActionListener(event ->
		{
			Color newColor = JColorChooser.showDialog(timeOptionsPanel, "\u984f\u8272\u9078\u64c7", new Color(getRGB(colorFields[index].getText())), false); //顏色選擇
			if (newColor != null) //如果按確定
				colorFields[index].setText(String.format("%06X", newColor.getRGB() & 0xFFFFFF)); //將選擇的顏色放入輸入框裡 不含alpha
		});
		timeOptionsPanel.add(chooserButton);

		return timeOptionsPanel;
	}

	private JButton createConfirmButton()
	{
		JButton confirmButton = new EButton("\u78ba\u5b9a", buttonFont); //確定
		confirmButton.setForeground(EColor.MINECRAFT_DARK_GREEN);
		confirmButton.setBounds(10, 300, 70, BUTTON_HEIGHT);

		confirmButton.addActionListener(event ->
		{
			MainWindow mainWindow = MainWindow.getInstance();

			Color remainTimeColor = new Color(colorOperation.remainTimeColor = getRGB(colorFields[REMAIN_INDEX].getText()));
			mainWindow.timePanelManager.remainTimeLabel.setForeground(remainTimeColor); //主顯示板
			mainWindow.remainTimeWindow.myLabel.setForeground(remainTimeColor); //彈出式視窗

			Color passTimeColor = new Color(colorOperation.passTimeColor = getRGB(colorFields[PASS_INDEX].getText()));
			mainWindow.timePanelManager.passTimeLabel.setForeground(passTimeColor); //主顯示板
			mainWindow.remainTimeWindow.myLabel.setForeground(passTimeColor); //彈出式視窗
		});

		return confirmButton;
	}

	private JButton createCancelButton()
	{
		JButton cancelButton = new EButton("\u53d6\u6d88", buttonFont); //取消
		cancelButton.setForeground(EColor.MINECRAFT_RED);
		cancelButton.setBounds(100, 300, 70, BUTTON_HEIGHT);

		cancelButton.addActionListener(event ->
		{
			colorFields[REMAIN_INDEX].setText(String.format("%06X", colorOperation.remainTimeColor)); //恢復輸入框
			colorFields[PASS_INDEX].setText(String.format("%06X", colorOperation.passTimeColor)); //恢復輸入框
		});

		return cancelButton;
	}

	private int getRGB(String hexString)
	{
		return hexString == null || hexString.isEmpty() ? 0 : Integer.parseInt(hexString, 16); //如果是空字串就變黑色
	}
}