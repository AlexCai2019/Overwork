package me.ac.overwork.backend;

import me.ac.overwork.OverworkException;

import java.util.Locale;

public class BackendCore implements IHasDestructor
{
	private static BackendCore instance = null;

	public static void createInstance() throws OverworkException
	{
		if (instance == null)
			instance = new BackendCore();
	}

	public static BackendCore getInstance()
	{
		return instance;
	}

	private final TimeOperation timeOperation;
	private final ColorOperation colorOperation;
	private final SizeOperation sizeOperation;

	private BackendCore() throws OverworkException
	{
		Locale.setDefault(Locale.TRADITIONAL_CHINESE); //語言預設繁體中文
		JSONHelper.createInstance(); //讓後端核心從檔案中讀取資料 有任何例外就丟給main
		timeOperation = new TimeOperation(); //建立時間處理核心 有任何例外就丟給main
		colorOperation = new ColorOperation(); //建立顏色處理核心
		sizeOperation = new SizeOperation(); //建立字型大小處理核心
	}

	public TimeOperation getTimeOperation()
	{
		return timeOperation;
	}

	public ColorOperation getColorOperation()
	{
		return colorOperation;
	}

	public SizeOperation getSizeOperation()
	{
		return sizeOperation;
	}

	@Override
	public void onApplicationQuit() throws OverworkException
	{
		timeOperation.onApplicationQuit();
		colorOperation.onApplicationQuit();
		sizeOperation.onApplicationQuit();
		JSONHelper.getInstance().saveJSON(); //上面三個存好JSON後 將JSON寫進檔案
	}
}