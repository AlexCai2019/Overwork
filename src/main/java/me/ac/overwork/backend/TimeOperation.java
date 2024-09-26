package me.ac.overwork.backend;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class TimeOperation
{
	public static final TimeOperation instance = new TimeOperation();

	private static final String SAVE_FILE_NAME = "save.json";

	private TimeData timeData;

	private TimeOperation() {}

	public void readTimeData() throws JSONFileException
	{
		timeData = JSONHelper.instance.readJSONFromFile(SAVE_FILE_NAME, TimeData.class);
	}

	public void addTime(int value, TimeUnit unit)
	{
		final int[] time = timeData.remainTime; //指標複製 讓程式碼短一點

		switch (unit)
		{
			case HOURS -> time[TimeData.HOUR] += value;
			case MINUTES -> time[TimeData.MINUTE] += value;
			case SECONDS -> time[TimeData.SECOND] += value;
		}

		if (time[TimeData.SECOND] >= 60) //加的時間讓秒超過了60
		{
			time[TimeData.MINUTE] += time[TimeData.SECOND] / 60; //將超過的秒加到分鐘上
			time[TimeData.SECOND] %= 60; //餘下的秒
		}

		if (time[TimeData.MINUTE] >= 60) //加的時間讓分鐘超過了60
		{
			time[TimeData.HOUR] += time[TimeData.MINUTE] / 60; //將超過的分鐘加到小時上
			time[TimeData.MINUTE] %= 60; //餘下的分鐘
		}
	}

	public void subtractTime()
	{
		final int[] time = timeData.remainTime;
		if (time[TimeData.HOUR] == 0 && time[TimeData.MINUTE] == 0 && time[TimeData.SECOND] == 0) //時間到了
			return;

		time[TimeData.SECOND]--; //減1秒
		if (time[TimeData.SECOND] >= 0)
			return;

		//秒變成負的
		time[TimeData.SECOND] = 59;
		time[TimeData.MINUTE]--;
		if (time[TimeData.MINUTE] >= 0)
			return;

		//分鐘變成負的
		time[TimeData.MINUTE] = 59;
		time[TimeData.HOUR]--;
	}

	public void subtractTime(int value, TimeUnit unit)
	{
		final int[] time = timeData.remainTime; //指標複製 讓程式碼短一點

		switch (unit)
		{
			case HOURS -> time[TimeData.HOUR] -= value;
			case MINUTES -> time[TimeData.MINUTE] -= value;
			case SECONDS -> time[TimeData.SECOND] -= value;
		}

		if (time[TimeData.HOUR] > 0 && time[TimeData.MINUTE] > 0 && time[TimeData.SECOND] > 0) //沒有人變成負數
			return;

		//有人變成負數
		int remainSeconds = time[TimeData.HOUR] * 60 * 60 + time[TimeData.MINUTE] * 60 + time[TimeData.SECOND]; //剩下的時間變成秒
		if (remainSeconds <= 0) //時間到了
		{
			Arrays.fill(time, 0);
			return;
		}

		time[TimeData.SECOND] = remainSeconds % 60;

		int remainMinutes = remainSeconds / 60;
		time[TimeData.MINUTE] = remainMinutes % 60;

		time[TimeData.HOUR] = remainMinutes / 60;

	}

	public int[] getRemainTime()
	{
		return timeData.remainTime.clone(); //陣列是少數可以正常用clone()的物件
	}

	public void save()
	{
		//存檔
		JSONHelper.instance.writeJSONToFile(SAVE_FILE_NAME, timeData);
	}

	private static class TimeData
	{
		static final int HOUR = 0;
		static final int MINUTE = 1;
		static final int SECOND = 2;

		//剩下的時間
		int[] remainTime;
	}
}