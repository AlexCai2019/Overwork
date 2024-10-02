package me.ac.overwork.frontend;

@SuppressWarnings("UnnecessaryUnicodeEscape") //為了避免亂碼
public class MainTab extends TabParent
{
	private static final MainTab instance = new MainTab();

	public static MainTab getInstance()
	{
		return instance;
	}

	public final TimePanel timePanelManager;
	public final ControlPanel controlPanelManager;

	private MainTab()
	{
		super("\u8a08\u6642\u5668"); //計時器

		//時間介面
		timePanelManager = new TimePanel(); //現在才創 為了給載入JSON留時間
		tabPanel.add(timePanelManager.myPanel);

		//操作介面
		controlPanelManager = new ControlPanel();
		tabPanel.add(controlPanelManager.myPanel);
	}
}