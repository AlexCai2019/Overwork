package me.ac.overwork.backend;

import com.google.gson.*;
import me.ac.overwork.OverworkException;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public class JSONHelper
{
	static final String SAVE_FILE_NAME = "save.json";
	static final String CONFIG_FILE_NAME = "config.json";

	private final Gson gson = new GsonBuilder().setPrettyPrinting().create(); //JSON處理核心
	private final JsonObject save;
	private final JsonObject config;

	JSONHelper() throws OverworkException
	{
		try
		{
			save = gson.fromJson(FileHelper.instance.readJSONFromFile(SAVE_FILE_NAME), JsonObject.class); //從檔案中讀取JSON
			config = gson.fromJson(FileHelper.instance.readJSONFromFile(CONFIG_FILE_NAME), JsonObject.class); //從檔案中讀取JSON
		}
		catch (JsonSyntaxException e)
		{
			throw new OverworkException(e);
		}

		if (save == null)
			throw new OverworkException('"' + SAVE_FILE_NAME + "\" \u5167\u5bb9\u7570\u5e38"); //"save.json" 內容異常

		if (config == null)
			throw new OverworkException('"' + CONFIG_FILE_NAME + "\" \u5167\u5bb9\u7570\u5e38"); //"config.json" 內容異常
	}

	int[] getTimeArray(String key) throws OverworkException
	{
		JsonElement element = save.get(key); //尋找key
		if (element == null) //找不到
			throw new OverworkException("\u5728 " + SAVE_FILE_NAME + " \u5167\u627e\u4e0d\u5230 \"" + key + '"'); //在 save.json 內找不到 "key"

		JsonObject timeObject; //時間物件
		try
		{
			timeObject = element.getAsJsonObject();
		}
		catch (IllegalStateException exception) //不是JsonObject
		{
			throw new OverworkException(key + " \u4e0d\u662fJSON\u7269\u4ef6"); //不是JSON物件
		}

		int[] timeArray = new int[3];

		try
		{
			timeArray[TimeOperation.HOUR] = timeObject.get("hour").getAsJsonPrimitive().getAsInt(); //小時
			timeArray[TimeOperation.MINUTE] = timeObject.get("minute").getAsJsonPrimitive().getAsInt(); //分鐘
			timeArray[TimeOperation.SECOND] = timeObject.get("second").getAsJsonPrimitive().getAsInt(); //秒
		}
		catch (IllegalStateException | NumberFormatException exception) //不是JsonPrimitive 或 不是數字字串
		{
			throw new OverworkException(key + " \u683c\u5f0f\u932f\u8aa4"); //格式錯誤
		}

		return timeArray;
	}

	void setTimeArray(String key, int[] values)
	{
		JsonObject timeObject = new JsonObject();
		timeObject.addProperty("hour", values[TimeOperation.HOUR]);
		timeObject.addProperty("minute", values[TimeOperation.MINUTE]);
		timeObject.addProperty("second", values[TimeOperation.SECOND]);
		save.add(key, timeObject); //將物件寫入root json
	}

	void saveJSON() throws OverworkException
	{
		FileHelper.instance.writeJSONToFile(SAVE_FILE_NAME, gson.toJson(save)); //將物件轉換成JSON字串寫入檔案
	}
}