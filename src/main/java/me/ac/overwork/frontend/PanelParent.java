package me.ac.overwork.frontend;

import me.ac.overwork.backend.BackendCore;
import me.ac.overwork.backend.SettingOperation;
import me.ac.overwork.backend.TimeOperation;

import javax.swing.JPanel;

class PanelParent
{
	protected final MainWindow mainWindow;
	protected final JPanel myPanel = new JPanel(null);
	protected final TimeOperation timeOperation = BackendCore.getInstance().getTimeOperation(); //時間處理類別
	protected final SettingOperation settingOperation = BackendCore.getInstance().getSettingOperation();

	PanelParent(MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
	}
}