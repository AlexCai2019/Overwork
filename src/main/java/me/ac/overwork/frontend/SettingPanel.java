package me.ac.overwork.frontend;

import me.ac.overwork.frontend.swing_extend.EButton;
import me.ac.overwork.frontend.swing_extend.EColor;
import me.ac.overwork.frontend.swing_extend.ELabel;
import me.ac.overwork.frontend.swing_extend.ETextField;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class SettingPanel extends PanelParent
{
	private static final int SUB_PANEL_HEIGHT = 30;
	private final Font buttonFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 16);
	private final JTextField remainColor;
	private final JTextField passColor;

	SettingPanel()
	{
		super(); //設定

		myPanel.setLayout(null);

		Font textFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 18);
		JLabel remainColorLabel = new ELabel("\u5269\u9918\u6642\u9593\u984f\u8272   #", textFont); //剩餘時間顏色   #
		remainColorLabel.setBounds(10, 10, 140, SUB_PANEL_HEIGHT);
		myPanel.add(remainColorLabel); //剩餘時間顏色

		PlainDocument remainColorDocument = new PlainDocument();
		remainColorDocument.setDocumentFilter(new ColorFieldFilter()); //輸入中偵測
		remainColor = new ETextField(remainColorDocument, 6, String.format("%06X", colorOperation.remainTime),
				textFont, "\u8a2d\u5b9a\u5269\u9918\u6642\u9593\u984f\u8272(16\u9032\u4f4d\u8272\u78bc)"); //設定剩餘時間顏色(16進位色碼)
		remainColor.setBounds(150, 10, 140, SUB_PANEL_HEIGHT);
		myPanel.add(remainColor);

		JLabel passColorLabel = new ELabel("\u7d93\u904e\u6642\u9593\u984f\u8272   #", textFont); //經過時間顏色   #
		passColorLabel.setBounds(10, 10 + SUB_PANEL_HEIGHT, 140, SUB_PANEL_HEIGHT);
		myPanel.add(passColorLabel);

		PlainDocument passColorDocument = new PlainDocument();
		passColorDocument.setDocumentFilter(new ColorFieldFilter()); //輸入中偵測
		passColor = new ETextField(passColorDocument, 6, String.format("%06X", colorOperation.passTime),
				textFont, "\u8a2d\u5b9a\u7d93\u904e\u6642\u9593\u984f\u8272(16\u9032\u4f4d\u8272\u78bc)"); //設定經過時間顏色(16進位色碼)
		passColor.setBounds(150, 10 + SUB_PANEL_HEIGHT, 140, SUB_PANEL_HEIGHT);
		myPanel.add(passColor);

		myPanel.add(createConfirmButton());
		myPanel.add(createCancelButton());
	}

	private JButton createConfirmButton()
	{
		JButton confirmButton = new EButton("\u78ba\u5b9a", buttonFont); //確定
		confirmButton.setForeground(EColor.MINECRAFT_DARK_GREEN);
		confirmButton.setBounds(10, 100, 70, SUB_PANEL_HEIGHT);

		confirmButton.addActionListener(event ->
		{
			String remainColorString = remainColor.getText(); //如果是空字串就變黑色
			colorOperation.remainTime = remainColorString.isEmpty() ? 0 : Integer.parseInt(remainColorString, 16);
			MainWindow.getInstance().timePanelManager.remainTimeLabel.setForeground(new Color(colorOperation.remainTime));

			String passColorString = passColor.getText(); //如果是空字串就變黑色
			colorOperation.passTime = passColorString.isEmpty() ? 0 : Integer.parseInt(passColorString, 16);
			MainWindow.getInstance().timePanelManager.passTimeLabel.setForeground(new Color(colorOperation.passTime));
		});

		return confirmButton;
	}

	private JButton createCancelButton()
	{
		JButton cancelButton = new EButton("\u53d6\u6d88", buttonFont); //取消
		cancelButton.setForeground(EColor.MINECRAFT_RED);
		cancelButton.setBounds(100, 100, 70, SUB_PANEL_HEIGHT);

		cancelButton.addActionListener(event ->
		{
			remainColor.setText(String.format("%06X", colorOperation.remainTime));
			passColor.setText(String.format("%06X", colorOperation.passTime));
		});

		return cancelButton;
	}
}