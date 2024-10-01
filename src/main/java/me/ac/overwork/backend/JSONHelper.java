package me.ac.overwork.backend;

import me.ac.overwork.OverworkException;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public class JSONHelper
{
	static final String SAVE_FILE_NAME = "save.json";

	private final JSONObject save;

	JSONHelper() throws OverworkException
	{
		try
		{
			save = new JSONObject(FileHelper.instance.readJSONFromFile()); //從檔案中讀取JSON
		}
		catch (JSONException e)
		{
			throw new OverworkException(e);
		}
	}

	int[] getTimeArray(String key) throws OverworkException
	{
		Object element = save.opt(key); //尋找key
		if (element == null) //找不到
			throw new OverworkException("\u5728 " + SAVE_FILE_NAME + " \u5167\u627e\u4e0d\u5230 \"" + key + '"'); //在 save.json 內找不到 "key"
		if (!(element instanceof JSONObject timeObject)) //不是JSONObject
			throw new OverworkException(key + " \u4e0d\u662fJSON\u7269\u4ef6"); //不是JSON物件

		int[] timeArray = new int[3];

		try
		{
			timeArray[TimeOperation.HOUR] = timeObject.getInt("hour"); //小時
			timeArray[TimeOperation.MINUTE] = timeObject.getInt("minute"); //分鐘
			timeArray[TimeOperation.SECOND] = timeObject.getInt("second"); //秒
		}
		catch (JSONException exception) //不是JsonPrimitive 或 不是數字字串
		{
			throw new OverworkException(key + " \u683c\u5f0f\u932f\u8aa4"); //格式錯誤
		}

		return timeArray;
	}

	void setTimeArray(String key, int[] values)
	{
		JSONObject timeObject = new JSONObject();
		timeObject.put("hour", values[TimeOperation.HOUR]);
		timeObject.put("minute", values[TimeOperation.MINUTE]);
		timeObject.put("second", values[TimeOperation.SECOND]);
		save.put(key, timeObject); //將物件寫入root json
	}

	void saveJSON() throws OverworkException
	{
		FileHelper.instance.writeJSONToFile(save.toString(4)); //將物件轉換成JSON字串寫入檔案
	}
}