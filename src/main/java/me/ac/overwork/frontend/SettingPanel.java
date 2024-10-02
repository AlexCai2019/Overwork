package me.ac.overwork.frontend;

import me.ac.overwork.frontend.swing_extend.ELabel;
import me.ac.overwork.frontend.swing_extend.ETextField;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class SettingPanel extends PanelParent
{
	private static final int SUB_PANEL_HEIGHT = 20;

	SettingPanel()
	{
		super(); //設定

		myPanel.setLayout(null);

		Font textFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 16);
		JLabel remainColorLabel = new ELabel("\u5269\u9918\u6642\u9593\u984f\u8272", textFont);
		remainColorLabel.setBounds(0, 0, 100, SUB_PANEL_HEIGHT);
		myPanel.add(remainColorLabel); //剩餘時間顏色

		PlainDocument colorDocument = new PlainDocument();
		colorDocument.setDocumentFilter(new ColorFieldFilter()); //輸入中偵測
		JTextField remainColor = new ETextField(colorDocument, 7, "#", textFont, "\u8a2d\u5b9a\u5269\u9918\u6642\u9593\u984f\u8272(16\u9032\u4f4d\u8272\u78bc)"); //設定剩餘時間顏色(16進位色碼)
		remainColor.setBounds(100, 0, MainWindow.WIDTH / 2, SUB_PANEL_HEIGHT);
		myPanel.add(remainColor);
	}
}