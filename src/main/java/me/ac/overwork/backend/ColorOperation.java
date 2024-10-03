package me.ac.overwork.backend;

import java.util.regex.Pattern;

public class ColorOperation implements IHasDestructor
{
	public int remainTime;
	public int passTime;

	private static final String REMAIN_TIME = "remainTimeColor";
	private static final String PASS_TIME = "passTimeColor";

	ColorOperation()
	{
		//讀取失敗會預設為0
		JSONHelper jsonCore = JSONHelper.getInstance(); //JSON處理核心
		String remainTimeString = jsonCore.get(REMAIN_TIME, "0", String.class); //剩餘時間
		String passTimeString = jsonCore.get(PASS_TIME, "0", String.class); //經過時間

		Pattern hexadecimal = Pattern.compile("[0-9A-Fa-f]{1,6}");
		//可轉換就轉換 不能轉換就變黑色
		remainTime = hexadecimal.matcher(remainTimeString).matches() ? Integer.parseInt(remainTimeString, 16) : 0;
		passTime = hexadecimal.matcher(passTimeString).matches() ? Integer.parseInt(passTimeString, 16) : 0;
	}

	@Override
	public void onApplicationQuit()
	{
		JSONHelper jsonCore = JSONHelper.getInstance(); //JSON處理核心
		jsonCore.put(REMAIN_TIME, String.format("%06X", remainTime));
		jsonCore.put(PASS_TIME, String.format("%06X", passTime));
	}
}