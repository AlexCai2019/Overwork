package me.ac.overwork.frontend;

import me.ac.overwork.backend.BackendCore;
import me.ac.overwork.backend.TimeOperation;

import javax.swing.*;

public abstract class APanelManager
{
	protected final JPanel myPanel = new JPanel();
	protected final BackendCore backendCore;
	protected final TimeOperation timeOperation ; //時間處理類別

	APanelManager(BackendCore backendCore)
	{
		this.backendCore = backendCore;
		timeOperation = backendCore.getTimeOperation();
	}

	JPanel getPanel()
	{
		return myPanel;
	}

	abstract void onMainWindowClosing();
}