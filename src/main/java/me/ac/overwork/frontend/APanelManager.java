package me.ac.overwork.frontend;

import me.ac.overwork.backend.BackendCore;
import me.ac.overwork.backend.TimeOperation;

import javax.swing.*;

public class APanelManager
{
	protected final JPanel myPanel = new JPanel();
	protected final TimeOperation timeOperation; //時間處理類別

	APanelManager()
	{
		timeOperation = BackendCore.getInstance().getTimeOperation();
	}
}