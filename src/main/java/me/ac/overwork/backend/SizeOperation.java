package me.ac.overwork.backend;

public class SizeOperation implements IHasDestructor
{
	public int remainTimeSize;
	public int passTimeSize;

	SizeOperation()
	{
		JSONHelper jsonCore = JSONHelper.getInstance(); //JSON處理核心
		//讀取失敗會預設為24
		//範圍是0 ~ 99
		remainTimeSize = Math.max(Math.min(99, jsonCore.get(TimeOperation.TimeType.remainTime, JSONHelper.SIZE, 24, Integer.class)), 0); //剩餘時間
		passTimeSize = Math.max(Math.min(99, jsonCore.get(TimeOperation.TimeType.passTime, JSONHelper.SIZE, 24, Integer.class)), 0); //經過時間
	}

	@Override
	public void onApplicationQuit()
	{
		JSONHelper jsonCore = JSONHelper.getInstance(); //JSON處理核心
		jsonCore.put(TimeOperation.TimeType.remainTime, JSONHelper.SIZE, remainTimeSize);
		jsonCore.put(TimeOperation.TimeType.passTime, JSONHelper.SIZE, passTimeSize);
	}
}