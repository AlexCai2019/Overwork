package me.ac.overwork.backend;

import com.google.gson.*;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public class JSONHelper
{
	static final String SAVE_FILE_NAME = "save.json";

	private final Gson gson = new GsonBuilder().setPrettyPrinting().create(); //JSON處理核心
	private final JsonObject save;

	JSONHelper() throws JSONFileException
	{
		try
		{
			save = gson.fromJson(FileHelper.instance.readJSONFromFile(SAVE_FILE_NAME), JsonObject.class); //從檔案中讀取JSON
		}
		catch (JsonSyntaxException e)
		{
			throw new JSONFileException(e);
		}

		if (save == null)
			throw new JSONFileException('"' + SAVE_FILE_NAME + "\" \u5167\u5bb9\u7570\u5e38"); //"save.json" 內容異常
	}

	int[] getTimeArray(String key) throws JSONFileException
	{
		JsonElement element = save.get(key); //尋找key
		if (element == null) //找不到
			throw new JSONFileException("\u5728 " + SAVE_FILE_NAME + " \u5167\u627e\u4e0d\u5230 \"" + key + '"'); //在 save.json 內找不到 "key"
		if (!(element instanceof JsonArray jsonArray) || jsonArray.size() != 3) //不是時 分 秒
			throw new JSONFileException(key + " \u683c\u5f0f\u932f\u8aa4"); //格式錯誤

		int[] timeArray = new int[3];

		try
		{
			timeArray[TimeOperation.HOUR] = jsonArray.get(TimeOperation.HOUR).getAsJsonPrimitive().getAsInt(); //小時
			timeArray[TimeOperation.MINUTE] = jsonArray.get(TimeOperation.MINUTE).getAsJsonPrimitive().getAsInt(); //分鐘
			timeArray[TimeOperation.SECOND] = jsonArray.get(TimeOperation.SECOND).getAsJsonPrimitive().getAsInt(); //秒
		}
		catch (IllegalStateException | NumberFormatException exception) //不是JsonPrimitive 或 不是數字字串
		{
			throw new JSONFileException(key + " \u683c\u5f0f\u932f\u8aa4"); //格式錯誤
		}

		return timeArray;
	}

	void writeJSONToFile() throws JSONFileException
	{
		FileHelper.instance.writeJSONToFile(SAVE_FILE_NAME, gson.toJson(save)); //將物件轉換成JSON字串寫入檔案
	}
}