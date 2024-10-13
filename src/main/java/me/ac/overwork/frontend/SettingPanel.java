package me.ac.overwork.frontend;

import me.ac.overwork.backend.TimeType;
import me.ac.overwork.frontend.swing_extend.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class SettingPanel extends PanelParent
{
	private static final int SUB_PANEL_WIDTH = MainWindow.WIDTH - 40;
	private static final int SUB_PANEL_HEIGHT = 100;
	private static final int BUTTON_HEIGHT = 30;

	private final Font borderFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 12);
	private final Font textFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 18);
	private final Font buttonFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 16);
	private final Map<TimeType, JTextField> colorFields = new EnumMap<>(TimeType.class);
	private final Map<TimeType, JTextField> sizeFields = new EnumMap<>(TimeType.class);
	private final Map<TimeType, JSlider> sizeSliders = new EnumMap<>(TimeType.class); //滑塊
	private final JButton confirmButton = new EButton("\u78ba\u5b9a", buttonFont); //確定
	private final JButton cancelButton = new EButton("\u53d6\u6d88", buttonFont); //取消

	private final Map<TimeType, Integer> tempColor = new EnumMap<>(TimeType.class);
	private final Map<TimeType, Integer> tempSize = new EnumMap<>(TimeType.class);

	SettingPanel(MainWindow mainWindow)
	{
		super(mainWindow);

		//現在就創好避免FieldFilter找不到
		sizeSliders.put(TimeType.remainTime, new JSlider(0, 99));
		sizeSliders.put(TimeType.passTime, new JSlider(0, 99));

		myPanel.setLayout(null);

		myPanel.add(createTimeOptionPanel(TimeType.remainTime, "\u5269\u9918")); //剩餘
		myPanel.add(createTimeOptionPanel(TimeType.passTime, "\u7d93\u904e")); //經過

		myPanel.add(createConfirmButton()); //確定按鈕
		myPanel.add(createCancelButton()); //取消按鈕
	}

	private JPanel createTimeOptionPanel(TimeType type, String title)
	{
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.anchor = GridBagConstraints.CENTER;

		JPanel timeOptionsPanel = new JPanel(new GridBagLayout());
		//y是開頭10格 加上panel的高度
		timeOptionsPanel.setBounds(10, 10 * (type.ordinal() + 1) + SUB_PANEL_HEIGHT * type.ordinal(), SUB_PANEL_WIDTH, SUB_PANEL_HEIGHT);
		TitledBorder titledBorder = BorderFactory.createTitledBorder(title + "\u6642\u9593"); //title時間
		titledBorder.setTitleFont(borderFont);
		timeOptionsPanel.setBorder(titledBorder);

		constraints.gridx = 0; //第0行
		constraints.gridy = 0; //第0列
		timeOptionsPanel.add(new ELabel("\u984f\u8272 #", textFont), constraints); //顏色 #

		PlainDocument colorDocument = new PlainDocument();
		colorDocument.setDocumentFilter(new ColorFieldFilter(type)); //輸入中偵測
		//顏色輸入框
		JTextField colorField = new ETextField(colorDocument, 6, String.format("%06X", settingOperation.getColor(type)), //從後端獲得顏色
				textFont, "\u8a2d\u5b9a" + title + "\u6642\u9593\u984f\u8272(16\u9032\u4f4d\u8272\u78bc)"); //設定title時間顏色(16進位色碼)
		colorFields.put(type, colorField);
		constraints.gridx = 1; //第1行
		constraints.gridwidth = 2; //佔2行
		timeOptionsPanel.add(colorField, constraints);

		constraints.gridx = 3; //第3行
		constraints.gridwidth = 1; //佔1行
		JButton chooserButton = new EButton("\u9078\u64c7", buttonFont, "\u9078\u64c7\u984f\u8272"); //選擇 選擇顏色
		chooserButton.addActionListener(event ->
		{
			JTextField field = colorFields.get(type);
			//顏色選擇視窗 不含alpha
			Color newColor = JColorChooser.showDialog(timeOptionsPanel, "\u984f\u8272\u9078\u64c7", new Color(getRGB(field.getText())), false); //顏色選擇
			if (newColor != null) //如果按確定
				field.setText(String.format("%06X", newColor.getRGB() & 0xFFFFFF)); //將選擇的顏色放入輸入框裡 不含alpha
		});
		timeOptionsPanel.add(chooserButton, constraints);

		constraints.gridx = 0; //第0行
		constraints.gridy = 1; //第1列
		timeOptionsPanel.add(new ELabel("\u5927\u5c0f", textFont), constraints); //大小

		PlainDocument sizeDocument = new PlainDocument();
		sizeDocument.setDocumentFilter(new SizeFieldFilter(type));
		JTextField sizeField = new ETextField(sizeDocument, 2, Integer.toString(settingOperation.getSize(type)), //從後端獲得字型大小
				textFont, "\u8a2d\u5b9a" + title + "\u6642\u9593\u5927\u5c0f"); //設定title時間大小
		//字型大小輸入框
		sizeFields.put(type, sizeField);
		constraints.gridx = 1; //第1行
		timeOptionsPanel.add(sizeField, constraints);

		constraints.gridx = 2; //第2行
		constraints.gridwidth = 2; //佔2行
		//滑塊不用設定數值 FieldFilter會設定
		JSlider slider = sizeSliders.get(type);
		slider.addChangeListener(event -> sizeFields.get(type).setText(Integer.toString(slider.getValue()))); //變更滑塊的時候同步更改輸入框
		timeOptionsPanel.add(slider, constraints);

		return timeOptionsPanel;
	}

	private JButton createConfirmButton()
	{
		confirmButton.setForeground(EColor.MINECRAFT_DARK_GREEN);
		confirmButton.setBounds(10, 300, 70, BUTTON_HEIGHT);
		confirmButton.setEnabled(false); //預設不啟用

		confirmButton.addActionListener(event ->
		{
			//暫時的變成永久的
			settingOperation.setColor(TimeType.remainTime, tempColor.get(TimeType.remainTime));
			settingOperation.setColor(TimeType.passTime, tempColor.get(TimeType.passTime));
			settingOperation.setSize(TimeType.remainTime, tempSize.get(TimeType.remainTime));
			settingOperation.setSize(TimeType.passTime, tempSize.get(TimeType.passTime));

			//恢復成不啟用狀態
			confirmButton.setEnabled(false);
			cancelButton.setEnabled(false);
		});

		return confirmButton;
	}

	private JButton createCancelButton()
	{
		cancelButton.setForeground(EColor.MINECRAFT_RED);
		cancelButton.setBounds(100, 300, 70, BUTTON_HEIGHT);
		cancelButton.setEnabled(false); //預設不啟用

		cancelButton.addActionListener(event ->
		{
			//恢復輸入框
			colorFields.get(TimeType.remainTime).setText(String.format("%06X", settingOperation.getColor(TimeType.remainTime)));
			colorFields.get(TimeType.passTime).setText(String.format("%06X", settingOperation.getColor(TimeType.passTime)));

			int remainSize = settingOperation.getSize(TimeType.remainTime);
			int passSize = settingOperation.getSize(TimeType.passTime);

			//恢復輸入框
			sizeFields.get(TimeType.remainTime).setText(Integer.toString(remainSize));
			sizeFields.get(TimeType.passTime).setText(Integer.toString(passSize));

			//恢復滑塊
			sizeSliders.get(TimeType.remainTime).setValue(remainSize);
			sizeSliders.get(TimeType.passTime).setValue(passSize);

			//恢復成不啟用狀態
			confirmButton.setEnabled(false);
			cancelButton.setEnabled(false);
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

	private class FieldFilter extends TextFieldFilter
	{
		protected final TimeType type; //0或1
		protected JLabel timeLabel; //主顯示板
		protected PopOutWindow popOutWindow; //彈出式視窗

		protected FieldFilter(TimeType type, String regex)
		{
			super(regex);
			this.type = type;

			if (type == TimeType.remainTime) //如果是remain
			{
				timeLabel = mainWindow.timePanelManager.remainTimeLabel;
				popOutWindow = mainWindow.remainTimeWindow;
			}
			else //如果是pass
			{
				timeLabel = mainWindow.timePanelManager.passTimeLabel;
				popOutWindow = mainWindow.passTimeWindow;
			}
		}

		@Override
		protected void onValid(String content)
		{
			//有修改就啟用
			confirmButton.setEnabled(true);
			cancelButton.setEnabled(true);
		}
	}

	private class ColorFieldFilter extends FieldFilter
	{
		private ColorFieldFilter(TimeType type)
		{
			super(type, "[0-9A-Fa-f]{0,6}"); //0到6個hex數字
		}

		@Override
		protected void onValid(String content)
		{
			super.onValid(content);

			int newRGB = getRGB(content);
			tempColor.put(type, newRGB);
			Color newColor = new Color(newRGB); //設定暫時的顏色

			timeLabel.setForeground(newColor);
			popOutWindow.myLabel.setForeground(newColor);
		}
	}

	private class SizeFieldFilter extends FieldFilter
	{
		private SizeFieldFilter(TimeType type)
		{
			super(type, "\\d{0,2}"); //空字串 或0 ~ 99
		}

		@Override
		protected void onValid(String content)
		{
			super.onValid(content);
			int newSize = getSize(content);
			tempSize.put(type, newSize);
			Font newFont = new Font(MainWindow.FONT_NAME, Font.BOLD, newSize); //設定暫時的字型大小

			sizeSliders.get(type).setValue(newSize); //同步到滑塊上
			timeLabel.setFont(newFont);
			popOutWindow.myLabel.setFont(newFont);
		}
	}
}