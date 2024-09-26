package me.ac.overwork.frontend;

import me.ac.overwork.backend.BackendCore;
import me.ac.overwork.backend.TimeOperation;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

public class ControlPanel extends APanelManager
{
	private boolean isStarted = false;

	ControlPanel(BackendCore backendCore)
	{
		super(backendCore);

		myPanel.setLayout(new GridLayout(6, 1)); //會放6個物件

		Font buttonFont = new Font(MainWindow.FONT_NAME, Font.PLAIN, 24);

		Panel firstRow = new Panel(new GridLayout(1, 6));
		int[] remainTime = timeOperation.getRemainTime();
		JFormattedTextField hourField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		hourField.setText(Integer.toString(remainTime[TimeOperation.HOUR]));
		firstRow.add(hourField);
		JFormattedTextField minuteField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		minuteField.setText(Integer.toString(remainTime[TimeOperation.MINUTE]));
		firstRow.add(minuteField);
		JFormattedTextField secondField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		secondField.setText(Integer.toString(remainTime[TimeOperation.SECOND]));
		firstRow.add(secondField);

		JButton startButton = new JButton("Start");
		startButton.setFont(buttonFont);
		startButton.addActionListener(event ->
		{
			if (isStarted) //已經開始了
			{
				startButton.setText("Start");
				MainWindow.instance.timePanelManager.pauseTimer();
				isStarted = false; //變成暫停

				//重設輸入框中的數字
				int[] newRemainTime = timeOperation.getRemainTime();
				hourField.setText(Integer.toString(newRemainTime[TimeOperation.HOUR]));
				minuteField.setText(Integer.toString(newRemainTime[TimeOperation.MINUTE]));
				secondField.setText(Integer.toString(newRemainTime[TimeOperation.SECOND]));
			}
			else //已經暫停了
			{
				startButton.setText("Pause");
				MainWindow.instance.timePanelManager.startTimer();
				isStarted = true; //變成開始
			}
		});
		firstRow.add(startButton);

		myPanel.add(firstRow);
	}

	@Override
	void onMainWindowClosing() {}
}