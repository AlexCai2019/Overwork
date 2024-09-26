package me.ac.overwork.backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONHelper
{
	public static final JSONHelper instance = new JSONHelper();

	private final Gson gson = new GsonBuilder().setPrettyPrinting().create(); //JSON處理核心

	private JSONHelper() {}

	<T> T readJSONFromFile(String fileName, Class<T> clazz) throws JSONFileException
	{
		return gson.fromJson(FileHelper.instance.readJSONFromFile(fileName), clazz); //從檔案中讀取JSON
	}

	void writeJSONToFile(String fileName, Object object) throws JSONFileException
	{
		FileHelper.instance.writeJSONToFile(fileName, gson.toJson(object)); //將物件轉換成JSON字串寫入檔案
	}
}