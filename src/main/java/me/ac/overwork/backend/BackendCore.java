package me.ac.overwork.backend;

import me.ac.overwork.OverworkException;

public class BackendCore
{
	private final TimeOperation timeOperation; //讓後端核心從檔案中讀取資料

	public BackendCore() throws OverworkException
	{
		JSONHelper jsonCore = new JSONHelper();
		timeOperation = new TimeOperation(jsonCore);
	}

	public TimeOperation getTimeOperation()
	{
		return timeOperation;
	}

	public void onApplicationQuit()
	{
		timeOperation.saveFile();
	}
}