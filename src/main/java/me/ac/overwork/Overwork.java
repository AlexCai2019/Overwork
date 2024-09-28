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
			BackendCore.instance = new BackendCore(); //讓後端核心從檔案中讀取資料
			//後載入前端
			MainWindow.instance = new MainWindow(); //讓前端可以從後端那裡得到資料
		}
		catch (OverworkException exception) //有例外
		{
			MainWindow.messageBox(exception.getMessage()); //顯示錯誤視窗

			//訊息被關閉後
			System.exit(0); //結束程式
		}
	}
}