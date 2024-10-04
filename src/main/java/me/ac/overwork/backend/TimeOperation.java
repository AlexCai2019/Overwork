package me.ac.overwork.backend;

import me.ac.overwork.OverworkException;
import me.ac.overwork.frontend.MainWindow;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public class TimeOperation implements IHasDestructor
{
	public static final int HOUR = 0;
	public static final int MINUTE = 1;
	public static final int SECOND = 2;

	private final int[] remainTime; //剩餘時間
	private final int[] passTime; //經過時間

	private ScheduledExecutorService executorService = null;
	private ScheduledFuture<?> everySecond;

	TimeOperation() throws OverworkException
	{
		//讀取失敗就會throw
		JSONHelper jsonCore = JSONHelper.getInstance(); //JSON處理核心
		remainTime = jsonCore.getTimeArray(JSONHelper.TimeType.remainTime); //剩餘時間
		passTime = jsonCore.getTimeArray(JSONHelper.TimeType.passTime); //經過時間
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
		int totalInSeconds = remainTime[HOUR] * 60 * 60 + remainTime[MINUTE] * 60 + remainTime[SECOND]; //將剩下的時間變成以秒為單位
		if (totalInSeconds <= 0) //時間到了
		{
			Arrays.fill(remainTime, 0);
			return;
		}

		remainTime[SECOND] = totalInSeconds % 60; //以秒為單位下 除以60的餘數是真正的秒 例如64秒等於1分4秒 64 % 60 = 4

		int totalInMinutes = totalInSeconds / 60; //以秒為單位下 除以60會得到以分為單位 其中秒被無條件捨去了 例如60秒 / 60 = 1分
		remainTime[MINUTE] = totalInMinutes % 60; //以分為單位下 除以60的餘數是真正的分 例如72分等於1時12分 72 % 60 = 12

		remainTime[HOUR] = totalInMinutes / 60; //以分為單位下 除以60會得到以時為單位 其中分和秒被無條件捨去了 例如120分 / 60 = 2時
	}

	public void setRemainTime(int value, TimeUnit unit)
	{
		switch (unit) //直接設定時間 caller應該做數值檢測
		{
			case HOURS -> remainTime[HOUR] = value;
			case MINUTES -> remainTime[MINUTE] = value;
			case SECONDS -> remainTime[SECOND] = value;
		}
	}

	public void setPassTime(int value, TimeUnit unit)
	{
		switch (unit) //直接設定時間 caller應該做數值檢測
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
		return passTime.clone(); //陣列是少數可以正常用clone()的物件
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

			MainWindow.getInstance().timePanelManager.updateTimeLabel(); //根據資料更新顯示數字

			MainWindow mainWindow = MainWindow.getInstance();
			if (mainWindow.remainTimeWindow.isVisible()) //如果彈出式視窗有顯示
				mainWindow.remainTimeWindow.updateTimeLabel(); //更新彈出式視窗剩餘時間
			if (mainWindow.passTimeWindow.isVisible()) //如果彈出式視窗有顯示
				mainWindow.passTimeWindow.updateTimeLabel(); //更新彈出式視窗已過時間
		}, 0, 1, TimeUnit.SECONDS);
	}

	public void pauseTimer()
	{
		if (executorService == null) //已經結束了
			return;
		//結束計時
		everySecond.cancel(true);
		executorService.shutdown();
		executorService = null;
	}

	@Override
	public void onApplicationQuit() throws OverworkException
	{
		JSONHelper jsonCore = JSONHelper.getInstance();
		jsonCore.setTimeArray(JSONHelper.TimeType.remainTime, remainTime); //儲存到JSON
		jsonCore.setTimeArray(JSONHelper.TimeType.passTime, passTime); //儲存到JSON
		pauseTimer(); //結束計時
	}
}