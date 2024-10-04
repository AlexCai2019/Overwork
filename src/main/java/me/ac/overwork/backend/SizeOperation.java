package me.ac.overwork.backend;

public class SizeOperation implements IHasDestructor
{
	public int remainTimeSize;
	public int passTimeSize;

	SizeOperation()
	{

	}

	@Override
	public void onApplicationQuit()
	{
		JSONHelper jsonCore = JSONHelper.getInstance(); //JSON處理核心
		jsonCore.put(JSONHelper.TimeType.REMAIN_TIME, JSONHelper.SIZE, remainTimeSize);
		jsonCore.put(JSONHelper.TimeType.PASS_TIME, JSONHelper.SIZE, passTimeSize);
	}
}