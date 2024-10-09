package me.ac.overwork.backend;

import java.util.regex.Pattern;

public class ColorOperation implements IHasDestructor
{
	public int remainTimeColor;
	public int passTimeColor;

	ColorOperation()
	{
		//讀取失敗會預設為0
		JSONHelper jsonCore = JSONHelper.getInstance(); //JSON處理核心
		String remainTimeString = jsonCore.get(TimeOperation.TimeType.remainTime, JSONHelper.COLOR, "0", String.class); //剩餘時間
		String passTimeString = jsonCore.get(TimeOperation.TimeType.passTime, JSONHelper.COLOR, "0", String.class); //經過時間

		Pattern hexadecimal = Pattern.compile("[0-9A-Fa-f]{1,6}");
		//可轉換就轉換 不能轉換就變黑色
		remainTimeColor = hexadecimal.matcher(remainTimeString).matches() ? Integer.parseInt(remainTimeString, 16) : 0;
		passTimeColor = hexadecimal.matcher(passTimeString).matches() ? Integer.parseInt(passTimeString, 16) : 0;
	}

	@Override
	public void onApplicationQuit()
	{
		JSONHelper jsonCore = JSONHelper.getInstance(); //JSON處理核心
		jsonCore.put(TimeOperation.TimeType.remainTime, JSONHelper.COLOR, String.format("%06X", remainTimeColor));
		jsonCore.put(TimeOperation.TimeType.passTime, JSONHelper.COLOR, String.format("%06X", passTimeColor));
	}
}