package me.ac.overwork.frontend;

import me.ac.overwork.backend.TimeOperation;

import javax.swing.*;

public abstract class APanelManager
{
	protected final JPanel myPanel = new JPanel();
	protected final TimeOperation backendCore;

	APanelManager(TimeOperation backendCore)
	{
		this.backendCore = backendCore;
	}

	JPanel getPanel()
	{
		return myPanel;
	}

	abstract void onMainWindowClosing();
}