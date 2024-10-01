package me.ac.overwork.backend;

import me.ac.overwork.OverworkException;
import me.ac.overwork.frontend.MainWindow;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public class TimeOperation
{
	public static final int HOUR = 0;
	public static final int MINUTE = 1;
	public static final int SECOND = 2;

	private final int[] remainTime; //剩餘時間
	private final int[] passTime; //經過時間
	private final JSONHelper jsonCore;

	private ScheduledExecutorService executorService = null;
	private ScheduledFuture<?> everySecond;

	TimeOperation(JSONHelper jsonCore) throws OverworkException
	{
		this.jsonCore = jsonCore; //JSON處理核心
		//讀取失敗就會throw
		remainTime = jsonCore.getTimeArray("remainTime"); //剩餘時間
		passTime = jsonCore.getTimeArray("passTime"); //經過時間
	}

	public void addRemainTime(int value, TimeUnit unit)
	{
		addTime(remainTime, value, unit);
	}

	public void addPassTime()
	{
		addTime(passTime, 1, TimeUnit.SECONDS); //增加經過時間一秒
	}

	private void addTime(int[] time, int value, TimeUnit unit)
	{
		switch (unit)
		{
			case HOURS -> time[HOUR] += value;
			case MINUTES -> time[MINUTE] += value;
			case SECONDS -> time[SECOND] += value;
		}

		if (time[SECOND] >= 60) //加的時間讓秒超過了60
		{
			time[MINUTE] += time[SECOND] / 60; //將超過的秒加到分鐘上
			time[SECOND] %= 60; //餘下的秒
		}

		if (time[MINUTE] >= 60) //加的時間讓分鐘超過了60
		{
			time[HOUR] += time[MINUTE] / 60; //將超過的分鐘加到小時上
			time[MINUTE] %= 60; //餘下的分鐘
		}
	}

	public void subtractRemainTime()
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

	public void subtractRemainTime(int value, TimeUnit unit)
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

	public void setRemainTime(int value, TimeUnit unit)
	{
		switch (unit)
		{
			case HOURS -> remainTime[HOUR] = value;
			case MINUTES -> remainTime[MINUTE] = value;
			case SECONDS -> remainTime[SECOND] = value;
		}
	}

	public void setPassTime(int value, TimeUnit unit)
	{
		switch (unit)
		{
			case HOURS -> passTime[HOUR] = value;
			case MINUTES -> passTime[MINUTE] = value;
			case SECONDS -> passTime[SECOND] = value;
		}
	}

	public int[] getRemainTime()
	{
		return remainTime.clone(); //陣列是少數可以正常用clone()的物件
	}

	public int[] getPassTime()
	{
		return passTime.clone();
	}

	//不用後端計時，修改比較方便
	public void startTimer()
	{
		if (executorService != null) //已經開始了
			return;
		executorService = Executors.newSingleThreadScheduledExecutor(); //處理中控
		everySecond = executorService.scheduleAtFixedRate(() -> //每秒執行
		{
			subtractRemainTime(); //減少剩餘時間1秒
			addPassTime(); //增加經過時間1秒

			MainWindow mainWindow = MainWindow.getInstance();
			mainWindow.timePanelManager.updateTimeLabel(); //根據資料更新顯示數字
			if (mainWindow.remainTimeWindow.isVisible())
				mainWindow.remainTimeWindow.updateTimeLabel(); //更新彈出式視窗剩餘時間
			if (mainWindow.passTimeWindow.isVisible())
				mainWindow.passTimeWindow.updateTimeLabel(); //更新彈出式視窗已過時間

			if (mainWindow.controlPanelManager.shouldWriteFile()) //該寫檔
			{
				FileHelper.instance.updateTimeFile("remainTime.txt", remainTime);
				FileHelper.instance.updateTimeFile("passTime.txt", passTime);
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	public void pauseTimer()
	{
		if (executorService == null) //已經結束了
			return;
		everySecond.cancel(true);
		executorService.shutdown();
		executorService = null;
	}

	public void onApplicationQuit()
	{
		jsonCore.setTimeArray("remainTime", remainTime); //儲存到JSON
		jsonCore.setTimeArray("passTime", passTime); //儲存到JSON
		jsonCore.saveJSON(); //寫檔
		pauseTimer();
	}
}