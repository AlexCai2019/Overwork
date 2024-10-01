package me.ac.overwork.frontend;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class RemainTimeWindow extends PopOutWindow
{
	RemainTimeWindow()
	{
		super();
		popOutWindow.setTitle("\u5269\u9918\u6642\u9593"); //剩餘時間
		popOutWindow.setBounds(MainWindow.WIDTH, 0, WIDTH, HEIGHT);
	}

	@Override
	public void updateTimeLabel()
	{
		myLabel.setText(TimePanel.formatTime(timeOperation.getRemainTime()));
	}
}