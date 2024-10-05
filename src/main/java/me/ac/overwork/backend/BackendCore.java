package me.ac.overwork.backend;

import me.ac.overwork.OverworkException;

import java.util.Locale;

/**
 * 後端的核心，掌控著所有的後端類別。
 *
 * @since 2024/09/27
 * @author Alex Cai
 */
public class BackendCore implements IHasDestructor
{
	/**
	 * 後端核心的singleton。
	 *
	 * @since 2024/09/30
	 */
	private static BackendCore instance = null;

	/**
	 * 產生唯一的後端核心。
	 *
	 * @throws OverworkException 建立JSON核心或時間處理核心時的例外
	 * @since 2024/09/30
	 * @author Alex Cai
	 */
	public static void createInstance() throws OverworkException
	{
		if (instance == null)
			instance = new BackendCore();
	}

	/**
	 * 獲得後端核心。
	 *
	 * @return 後端核心
	 * @since 2024/09/30
	 * @author Alex Cai
	 */
	public static BackendCore getInstance()
	{
		return instance;
	}

	/**
	 * 時間處理核心。
	 *
	 * @since 2024/09/27
	 */
	private final TimeOperation timeOperation;
	/**
	 * 顏色處理核心。
	 *
	 * @since 2024/10/03
	 */
	private final ColorOperation colorOperation;
	/**
	 * 字型大小處理核心。
	 *
	 * @since 2024/10/04
	 */
	private final SizeOperation sizeOperation;

	/**
	 * 後端核心建構子，產生其他的核心。
	 *
	 * @throws OverworkException 建立JSON核心或時間處理核心時的例外
	 * @since 2024/09/27
	 * @author Alex Cai
	 */
	private BackendCore() throws OverworkException
	{
		Locale.setDefault(Locale.TRADITIONAL_CHINESE); //語言預設繁體中文
		JSONHelper.createInstance(); //讓後端核心從檔案中讀取資料 有任何例外就丟給main
		timeOperation = new TimeOperation(); //建立時間處理核心 有任何例外就丟給main
		colorOperation = new ColorOperation(); //建立顏色處理核心
		sizeOperation = new SizeOperation(); //建立字型大小處理核心
	}

	/**
	 * 獲得時間處理核心。
	 *
	 * @return 時間處理核心
	 * @since 2024/09/27
	 * @author Alex Cai
	 */
	public TimeOperation getTimeOperation()
	{
		return timeOperation;
	}

	/**
	 * 獲得顏色處理核心。
	 *
	 * @return 顏色處理核心
	 * @since 2024/10/03
	 * @author Alex Cai
	 */
	public ColorOperation getColorOperation()
	{
		return colorOperation;
	}

	/**
	 * 獲得字型大小處理核心。
	 *
	 * @return 字型大小處理核心
	 * @since 2024/10/04
	 * @author Alex Cai
	 */
	public SizeOperation getSizeOperation()
	{
		return sizeOperation;
	}

	/**
	 * 當應用程式關閉時，令所有的核心準備進入關閉狀態。
	 *
	 * @throws OverworkException 寫入檔案時發生例外
	 * @since 2024/09/27
	 * @author Alex Cai
	 */
	@Override
	public void onApplicationQuit() throws OverworkException
	{
		timeOperation.onApplicationQuit();
		colorOperation.onApplicationQuit();
		sizeOperation.onApplicationQuit();
		JSONHelper.getInstance().saveJSON(); //上面三個存好JSON後 將JSON寫進檔案
	}
}