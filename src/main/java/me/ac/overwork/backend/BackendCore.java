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

	private final TimeOperation timeOperation; //讓後端核心從檔案中讀取資料
	private final ColorOperation colorOperation;
	private final SizeOperation sizeOperation;

	private BackendCore() throws OverworkException
	{
		Locale.setDefault(Locale.TRADITIONAL_CHINESE); //語言預設繁體中文
		JSONHelper.createInstance(); //有任何例外就丟給main
		timeOperation = new TimeOperation(); //建立時間處理核心 有任何例外就丟給main
		colorOperation = new ColorOperation();
		sizeOperation = new SizeOperation();
	}

	public TimeOperation getTimeOperation()
	{
		return timeOperation;
	}

	public ColorOperation getColorOperation()
	{
		return colorOperation;
	}

	@Override
	public void onApplicationQuit() throws OverworkException
	{
		timeOperation.onApplicationQuit();
		colorOperation.onApplicationQuit();
		JSONHelper.getInstance().saveJSON(); //寫檔
	}
}