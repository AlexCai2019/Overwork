package me.ac.overwork.backend;

public class SizeOperation implements IHasDestructor
{
	public int remainTimeSize;
	public int passTimeSize;

	SizeOperation()
	{
		JSONHelper jsonCore = JSONHelper.getInstance(); //JSON處理核心
		//讀取失敗會預設為24
		remainTimeSize = jsonCore.get(JSONHelper.TimeType.REMAIN_TIME, JSONHelper.SIZE, 24, Integer.class); //剩餘時間
		passTimeSize = jsonCore.get(JSONHelper.TimeType.PASS_TIME, JSONHelper.SIZE, 24, Integer.class); //經過時間
	}

	@Override
	public void onApplicationQuit()
	{
		JSONHelper jsonCore = JSONHelper.getInstance(); //JSON處理核心
		jsonCore.put(JSONHelper.TimeType.REMAIN_TIME, JSONHelper.SIZE, remainTimeSize);
		jsonCore.put(JSONHelper.TimeType.PASS_TIME, JSONHelper.SIZE, passTimeSize);
	}
}