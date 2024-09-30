package me.ac.overwork.backend;

import me.ac.overwork.OverworkException;

public class BackendCore
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

	private BackendCore() throws OverworkException
	{
		timeOperation = new TimeOperation(new JSONHelper()); //建立時間處理核心 有任何例外就丟給main
	}

	public TimeOperation getTimeOperation()
	{
		return timeOperation;
	}

	public void onApplicationQuit()
	{
		timeOperation.onApplicationQuit();
	}
}