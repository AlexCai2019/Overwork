package me.ac.overwork.frontend;

import me.ac.overwork.backend.BackendCore;
import me.ac.overwork.backend.TimeOperation;

import javax.swing.JPanel;

class PanelParent
{
	protected final JPanel myPanel = new JPanel(null);
	protected final TimeOperation timeOperation; //時間處理類別

	PanelParent()
	{
		timeOperation = BackendCore.getInstance().getTimeOperation();
	}
}

class TabParent
{
	protected final JPanel tabPanel = new JPanel(null);

	protected final String tabName;

	TabParent(String tabName)
	{
		this.tabName = tabName;
	}
}