package me.ac.overwork.frontend;

import me.ac.overwork.backend.BackendCore;
import me.ac.overwork.backend.ColorOperation;
import me.ac.overwork.backend.SizeOperation;
import me.ac.overwork.backend.TimeOperation;

import javax.swing.JPanel;

class PanelParent
{
	protected final JPanel myPanel = new JPanel(null);
	protected final TimeOperation timeOperation; //時間處理類別
	protected final ColorOperation colorOperation; //顏色處理類別
	protected final SizeOperation sizeOperation; //字型大小處理類別

	PanelParent()
	{
		timeOperation = BackendCore.getInstance().getTimeOperation();
		colorOperation = BackendCore.getInstance().getColorOperation();
		sizeOperation = BackendCore.getInstance().getSizeOperation();
	}
}