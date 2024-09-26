package me.ac.overwork.backend;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public class TimeOperation
{
	public static final int HOUR = 0;
	public static final int MINUTE = 1;
	public static final int SECOND = 2;

	private final int[] remainTime; //剩餘時間
	private final int[] passTime; //經過時間

	public TimeOperation() throws JSONFileException
	{
		JSONHelper jsonCore = new JSONHelper();
		remainTime = jsonCore.getTimeArray("remainTime"); //讀取成功
		passTime = jsonCore.getTimeArray("passTime"); //讀取成功
	}

	public void addTime(int value, TimeUnit unit)
	{
		switch (unit)
		{
			case HOURS -> remainTime[HOUR] += value;
			case MINUTES -> remainTime[MINUTE] += value;
			case SECONDS -> remainTime[SECOND] += value;
		}

		if (remainTime[SECOND] >= 60) //加的時間讓秒超過了60
		{
			remainTime[MINUTE] += remainTime[SECOND] / 60; //將超過的秒加到分鐘上
			remainTime[SECOND] %= 60; //餘下的秒
		}

		if (remainTime[MINUTE] >= 60) //加的時間讓分鐘超過了60
		{
			remainTime[HOUR] += remainTime[MINUTE] / 60; //將超過的分鐘加到小時上
			remainTime[MINUTE] %= 60; //餘下的分鐘
		}
	}

	public void subtractTime()
	{
		if (remainTime[HOUR] == 0 && remainTime[MINUTE] == 0 && remainTime[SECOND] == 0) //時間到了
			return;

		remainTime[SECOND]--; //減1秒
		if (remainTime[SECOND] >= 0)
			return;

		//秒變成負的
		remainTime[SECOND] = 59;
		remainTime[MINUTE]--;
		if (remainTime[MINUTE] >= 0)
			return;

		//分鐘變成負的
		remainTime[MINUTE] = 59;
		remainTime[HOUR]--;
	}

	public void subtractTime(int value, TimeUnit unit)
	{
		switch (unit)
		{
			case HOURS -> remainTime[HOUR] -= value;
			case MINUTES -> remainTime[MINUTE] -= value;
			case SECONDS -> remainTime[SECOND] -= value;
		}

		if (remainTime[HOUR] >= 0 && remainTime[MINUTE] >= 0 && remainTime[SECOND] >= 0) //沒有人變成負數
			return;

		//有人變成負數
		int remainSeconds = remainTime[HOUR] * 60 * 60 + remainTime[MINUTE] * 60 + remainTime[SECOND]; //剩下的時間變成秒
		if (remainSeconds <= 0) //時間到了
		{
			Arrays.fill(remainTime, 0);
			return;
		}

		remainTime[SECOND] = remainSeconds % 60;

		int remainMinutes = remainSeconds / 60;
		remainTime[MINUTE] = remainMinutes % 60;

		remainTime[HOUR] = remainMinutes / 60;
	}

	public int[] getRemainTime()
	{
		return remainTime.clone(); //陣列是少數可以正常用clone()的物件
	}

	public int[] getPassTime()
	{
		return passTime.clone();
	}

	public void saveFile()
	{
	}
}