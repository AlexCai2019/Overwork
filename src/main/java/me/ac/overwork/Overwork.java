package me.ac.overwork;

import me.ac.overwork.backend.BackendCore;
import me.ac.overwork.frontend.MainWindow;

/**
 * 主函數存在的類別，載入一切。
 *
 * @since 2024/09/26
 * @author Alex Cai
 */
public class Overwork
{
	/**
	 * 主函數，先載入前端再載入後端，有任何例外就顯示錯誤視窗。
	 *
	 * @param args 命令列執行.jar時所帶的參數
	 * @since 2024/09/26
	 * @author Alex Cai
	 */
	public static void main(String[] args)
	{
		try
		{
			//先載入後端
			BackendCore.createInstance(); //讓後端核心從檔案中讀取資料
			//後載入前端
			MainWindow.createInstance(); //讓前端可以從後端那裡得到資料
		}
		catch (OverworkException exception) //有例外
		{
			MainWindow.messageBox(exception.getMessage()); //顯示錯誤視窗

			//訊息被關閉後
			System.exit(0); //結束程式
		}
	}
}