package me.ac.overwork.backend;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SettingOperation implements IHasDestructor
{
	private final Map<TimeType, int[]> data = new EnumMap<>(TimeType.class);

	public static final int COLOR_INDEX = 0;
	public static final int SIZE_INDEX = 1;

	SettingOperation()
	{
		int[] remainArray = new int[2];
		int[] passArray = new int[2];
		data.put(TimeType.remainTime, remainArray);
		data.put(TimeType.passTime, passArray);

		JSONHelper jsonCore = JSONHelper.getInstance(); //JSON處理核心

		//讀取失敗會預設為0
		String remainColorString = jsonCore.get(TimeType.remainTime, JSONHelper.COLOR, "0", String.class); //剩餘時間
		String passColorString = jsonCore.get(TimeType.passTime, JSONHelper.COLOR, "0", String.class); //經過時間
		//可轉換就轉換 不能轉換就變黑色
		Pattern hexadecimal = Pattern.compile("[0-9A-Fa-f]{1,6}");
		remainArray[COLOR_INDEX] = hexadecimal.matcher(remainColorString).matches() ? Integer.parseInt(remainColorString, 16) : 0;
		passArray[COLOR_INDEX] = hexadecimal.matcher(passColorString).matches() ? Integer.parseInt(passColorString, 16) : 0;

		//讀取失敗會預設為24
		//範圍是0 ~ 99
		remainArray[SIZE_INDEX] = Math.max(Math.min(99, jsonCore.get(TimeType.remainTime, JSONHelper.SIZE, 24, Integer.class)), 0); //剩餘時間
		passArray[SIZE_INDEX] = Math.max(Math.min(99, jsonCore.get(TimeType.passTime, JSONHelper.SIZE, 24, Integer.class)), 0); //經過時間
	}

	public void setColor(TimeType type, int color)
	{
		data.get(type)[COLOR_INDEX] = color;
	}

	public int getColor(TimeType type)
	{
		return data.get(type)[COLOR_INDEX];
	}

	public void setSize(TimeType type, int size)
	{
		data.get(type)[SIZE_INDEX] = size;
	}

	public int getSize(TimeType type)
	{
		return data.get(type)[SIZE_INDEX];
	}

	@Override
	public void onApplicationQuit()
	{
		int[] remain = data.get(TimeType.remainTime);
		int[] pass = data.get(TimeType.passTime);
		JSONHelper jsonCore = JSONHelper.getInstance(); //JSON處理核心
		jsonCore.put(TimeType.remainTime, JSONHelper.COLOR, remain[COLOR_INDEX]);
		jsonCore.put(TimeType.passTime, JSONHelper.COLOR, pass[COLOR_INDEX]);
		jsonCore.put(TimeType.remainTime, JSONHelper.SIZE, remain[SIZE_INDEX]);
		jsonCore.put(TimeType.passTime, JSONHelper.SIZE, pass[SIZE_INDEX]);
	}
}