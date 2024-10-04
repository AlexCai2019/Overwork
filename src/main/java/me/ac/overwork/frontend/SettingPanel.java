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
	private static final int SUB_PANEL_WIDTH = MainWindow.WIDTH - 40;
	private static final int SUB_PANEL_HEIGHT = 100;
	private static final int BUTTON_HEIGHT = 30;
	private final Font borderFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 12);
	private final Font textFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 18);
	private final Font buttonFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 16);
	private final JTextField[] colorFields = new JTextField[2];
	private final JTextField[] sizeFields = new JTextField[2];
	private final JSlider[] sizeSliders = new JSlider[2];

	private static final int REMAIN_INDEX = 0;
	private static final int PASS_INDEX = 1;

	SettingPanel()
	{
		myPanel.setLayout(null);

		myPanel.add(createTimeOptionPanel("\u5269\u9918", REMAIN_INDEX, colorOperation.remainTimeColor, sizeOperation.remainTimeSize)); //剩餘
		myPanel.add(createTimeOptionPanel("\u7d93\u904e", PASS_INDEX, colorOperation.passTimeColor, sizeOperation.passTimeSize)); //經過

		myPanel.add(createConfirmButton()); //確定按鈕
		myPanel.add(createCancelButton()); //取消按鈕
	}

	private JPanel createTimeOptionPanel(String title, int index, int initialColor, int initialSize)
	{
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;

		JPanel timeOptionsPanel = new JPanel(new GridBagLayout());
		//y是開頭10格 加上panel的高度
		timeOptionsPanel.setBounds(10, 10 * (index + 1) + SUB_PANEL_HEIGHT * index, SUB_PANEL_WIDTH, SUB_PANEL_HEIGHT);
		TitledBorder titledBorder = BorderFactory.createTitledBorder(title + "\u6642\u9593");
		titledBorder.setTitleFont(borderFont);
		timeOptionsPanel.setBorder(titledBorder); //title時間

		constraints.gridx = 0; //第0行
		constraints.gridy = 0; //第0列
		timeOptionsPanel.add(new ELabel("\u984f\u8272 #", textFont), constraints); //顏色 #

		PlainDocument colorDocument = new PlainDocument();
		colorDocument.setDocumentFilter(new ColorFieldFilter()); //輸入中偵測
		//顏色輸入框
		colorFields[index] = new ETextField(colorDocument, 6, String.format("%06X", initialColor),
				textFont, "\u8a2d\u5b9a" + title + "\u6642\u9593\u984f\u8272(16\u9032\u4f4d\u8272\u78bc)"); //設定title時間顏色(16進位色碼)
		constraints.gridx = 1; //第1行
		constraints.gridwidth = 2; //佔2行
		timeOptionsPanel.add(colorFields[index], constraints);

		constraints.gridx = 3; //第3行
		constraints.gridwidth = 1; //佔1行
		JButton chooserButton = new EButton("\u9078\u64c7", buttonFont, "\u9078\u64c7\u984f\u8272"); //選擇 選擇顏色
		JColorChooser.setDefaultLocale(Locale.TRADITIONAL_CHINESE); //語言預設繁體中文 (好像沒效果)
		chooserButton.addActionListener(event ->
		{
			//顏色選擇視窗 不含alpha
			Color newColor = JColorChooser.showDialog(timeOptionsPanel, "\u984f\u8272\u9078\u64c7", new Color(getRGB(colorFields[index].getText())), false); //顏色選擇
			if (newColor != null) //如果按確定
				colorFields[index].setText(String.format("%06X", newColor.getRGB() & 0xFFFFFF)); //將選擇的顏色放入輸入框裡 不含alpha
		});
		timeOptionsPanel.add(chooserButton, constraints);

		constraints.gridx = 0; //第0行
		constraints.gridy = 1; //第1列
		timeOptionsPanel.add(new ELabel("\u5927\u5c0f", textFont), constraints); //大小

		PlainDocument sizeDocument = new PlainDocument();
		sizeDocument.setDocumentFilter(new SizeFieldFilter());
		//字型大小輸入框
		sizeFields[index] = new ETextField(sizeDocument, 2, Integer.toString(initialSize),
				textFont, "\u8a2d\u5b9a" + title + "\u6642\u9593\u5927\u5c0f"); //設定title時間大小
		constraints.gridx = 1; //第1行
		timeOptionsPanel.add(sizeFields[index], constraints);

		constraints.gridx = 2; //第2行
		constraints.gridwidth = 2; //佔2行
		sizeSliders[index] = new JSlider(0, 99);
		sizeSliders[index].setValue(initialSize);
		sizeSliders[index].addChangeListener(event -> sizeFields[index].setText(Integer.toString(sizeSliders[index].getValue())));
		timeOptionsPanel.add(sizeSliders[index], constraints);

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
			mainWindow.passTimeWindow.myLabel.setForeground(passTimeColor); //彈出式視窗

			Font remainTimeFont = new Font(MainWindow.FONT_NAME, Font.BOLD, sizeOperation.remainTimeSize = getSize(sizeFields[REMAIN_INDEX].getText()));
			sizeSliders[REMAIN_INDEX].setValue(sizeOperation.remainTimeSize);
			mainWindow.timePanelManager.remainTimeLabel.setFont(remainTimeFont); //主顯示板
			mainWindow.remainTimeWindow.myLabel.setFont(remainTimeFont); //彈出式視窗

			Font passTimeFont = new Font(MainWindow.FONT_NAME, Font.BOLD, sizeOperation.passTimeSize = getSize(sizeFields[PASS_INDEX].getText()));
			sizeSliders[PASS_INDEX].setValue(sizeOperation.passTimeSize);
			mainWindow.timePanelManager.passTimeLabel.setFont(passTimeFont); //主顯示板
			mainWindow.passTimeWindow.myLabel.setFont(passTimeFont); //彈出式視窗
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
			//恢復輸入框
			colorFields[REMAIN_INDEX].setText(String.format("%06X", colorOperation.remainTimeColor));
			colorFields[PASS_INDEX].setText(String.format("%06X", colorOperation.passTimeColor));

			//恢復輸入框
			sizeFields[REMAIN_INDEX].setText(Integer.toString(sizeOperation.remainTimeSize));
			sizeFields[PASS_INDEX].setText(Integer.toString(sizeOperation.passTimeSize));

			//恢復滑塊
			sizeSliders[REMAIN_INDEX].setValue(sizeOperation.remainTimeSize);
			sizeSliders[PASS_INDEX].setValue(sizeOperation.passTimeSize);
		});

		return cancelButton;
	}

	private int getRGB(String hexString)
	{
		return hexString == null || hexString.isEmpty() ? 0 : Integer.parseInt(hexString, 16); //如果是空字串就變黑色
	}

	private int getSize(String sizeString)
	{
		return sizeString == null || sizeString.isEmpty() ? 0 : Integer.parseInt(sizeString); //如果是空字串就變0
	}

	private static class ColorFieldFilter extends TextFieldFilter
	{
		ColorFieldFilter()
		{
			super("[0-9A-Fa-f]{0,6}"); //0到6個hex數字
		}
	}

	private static class SizeFieldFilter extends TextFieldFilter
	{
		SizeFieldFilter()
		{
			super("\\d{0,2}"); //空字串 或0 ~ 99
		}
	}
}