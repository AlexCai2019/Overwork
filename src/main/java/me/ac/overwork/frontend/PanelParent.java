package me.ac.overwork.frontend;

import me.ac.overwork.backend.BackendCore;
import me.ac.overwork.backend.ColorOperation;
import me.ac.overwork.backend.TimeOperation;

import javax.swing.JPanel;

class PanelParent
{
	protected final JPanel myPanel = new JPanel(null);
	protected final TimeOperation timeOperation; //時間處理類別
	protected final ColorOperation colorOperation;

	PanelParent()
	{
		timeOperation = BackendCore.getInstance().getTimeOperation();
		colorOperation = BackendCore.getInstance().getColorOperation();
	}
}