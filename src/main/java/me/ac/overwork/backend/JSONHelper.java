package me.ac.overwork.backend;

import me.ac.overwork.OverworkException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumMap;
import java.util.Map;

@SuppressWarnings("UnnecessaryUnicodeEscape")
class JSONHelper
{
	private static JSONHelper instance;

	static void createInstance() throws OverworkException
	{
		if (instance == null)
			instance = new JSONHelper();
	}

	static JSONHelper getInstance()
	{
		return instance;
	}

	static final String SAVE_FILE_NAME = "save.json";
	static final String HOUR = "hour";
	static final String MINUTE = "minute";
	static final String SECOND = "second";
	static final String COLOR = "color";
	static final String SIZE = "size";

	private final JSONObject save;
	private final Map<TimeType, JSONObject> saveMap = new EnumMap<>(TimeType.class);

	private JSONHelper() throws OverworkException
	{
		try
		{
			save = new JSONObject(FileHelper.instance.readJSONFromFile()); //從檔案中讀取JSON
		}
		catch (JSONException e)
		{
			throw new OverworkException(e);
		}

		initialSaveMap(TimeType.remainTime); //初始化remainTime
		initialSaveMap(TimeType.passTime); //初始化passTime
	}

	private void initialSaveMap(TimeType timeKey) throws OverworkException
	{
		Object timeObject = save.opt(timeKey.toString()); //從save裡面找remainTime或passTime
		if (timeObject instanceof JSONObject time) //找到了 而且是正確的(json object)
			saveMap.put(timeKey, time); //放入map 然後這程式就結束了
		else if (timeObject == null) //找不到timeKey
			throw new OverworkException("\u5728 " + SAVE_FILE_NAME + " \u5167\u627e\u4e0d\u5230 \"" + timeKey + '"'); //在 save.json 內找不到 "timeKey"
		else //有找到timeKey 但格式錯誤
			throw new OverworkException(SAVE_FILE_NAME + " \u7684 \"" + timeKey + "\" \u683c\u5f0f\u932f\u8aa4"); //save.json 的 "timeKey" 格式錯誤
	}

	int[] getTimeArray(TimeType timeType) throws OverworkException
	{
		JSONObject timeObject = saveMap.get(timeType); //尋找timeKey

		int[] timeArray = new int[3];

		try
		{
			timeArray[TimeOperation.HOUR] = timeObject.getInt(HOUR); //小時
			timeArray[TimeOperation.MINUTE] = timeObject.getInt(MINUTE); //分鐘
			timeArray[TimeOperation.SECOND] = timeObject.getInt(SECOND); //秒
		}
		catch (JSONException exception) //不是int 或 不是數字字串
		{
			throw new OverworkException(timeType + " \u683c\u5f0f\u932f\u8aa4"); //格式錯誤
		}

		return timeArray;
	}

	void setTimeArray(TimeType timeType, int[] values) throws OverworkException
	{
		JSONObject timeObject = saveMap.get(timeType); //不可能為null 如果真是null 一定是initialSaveMap時沒有正確擲出例外
		timeObject.put(HOUR, values[TimeOperation.HOUR]);
		timeObject.put(MINUTE, values[TimeOperation.MINUTE]);
		timeObject.put(SECOND, values[TimeOperation.SECOND]);
	}

	void saveJSON() throws OverworkException
	{
		FileHelper.instance.writeJSONToFile(save.toString(4)); //將物件轉換成JSON字串寫入檔案
	}

	@SuppressWarnings("unchecked")
	<T> T get(TimeType timeType, String key, T defaultValue, Class<T> type)
	{
		JSONObject timeObject = saveMap.get(timeType); //remainTime或passTime
		if (timeObject == null) //找不到
			return defaultValue; //回傳預設值

		Object obj = timeObject.opt(key); //color或size
		return type.isInstance(obj) ? (T) obj : defaultValue;
	}

	void put(TimeType timeType, String key, Object value)
	{
		JSONObject timeObject = saveMap.get(timeType); //remainTime或passTime
		if (timeObject != null)
			timeObject.put(key, value);
	}
}