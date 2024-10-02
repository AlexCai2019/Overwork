package me.ac.overwork.frontend;

import me.ac.overwork.frontend.swing_extend.ELabel;
import me.ac.overwork.frontend.swing_extend.ETextField;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.*;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class SettingTab extends TabParent
{
	private static final SettingTab instance = new SettingTab();

	public static SettingTab getInstance()
	{
		return instance;
	}

	SettingTab()
	{
		super("\u8a2d\u5b9a"); //設定

		tabPanel.setLayout(new GridLayout(10, 2));

		Font textFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 16);

		tabPanel.add(new ELabel("\u5269\u9918\u6642\u9593\u984f\u8272", textFont)); //剩餘時間顏色

		PlainDocument colorDocument = new PlainDocument();
		colorDocument.setDocumentFilter(new ColorFieldFilter()); //輸入中偵測
		JTextField remainColor = new ETextField(colorDocument, 7, "#", textFont, "\u8a2d\u5b9a\u5269\u9918\u6642\u9593\u984f\u8272(16\u9032\u4f4d\u8272\u78bc)"); //設定剩餘時間顏色(16進位色碼)
		tabPanel.add(remainColor);
	}
}