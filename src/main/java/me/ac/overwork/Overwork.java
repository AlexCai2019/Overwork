package me.ac.overwork;

import me.ac.overwork.backend.BackendCore;
import me.ac.overwork.frontend.MainWindow;

public class Overwork
{
	public static void main(String[] args)
	{
		try
		{
			//先載入後端
			BackendCore backendCore = new BackendCore(); //讓後端核心從檔案中讀取資料
			MainWindow.instance = new MainWindow(backendCore);
		}
		catch (OverworkException exception) //有JSON例外
		{
			MainWindow.messageBox(exception.getMessage());

			//訊息被關閉後
			System.exit(0);
		}
	}
}