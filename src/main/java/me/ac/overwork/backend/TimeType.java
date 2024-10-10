package me.ac.overwork.backend;

public enum TimeType //remainTime或passTime, 取決於要獲得已過還是剩餘
{
	//千萬不可以改這兩個變數的名字 它們和json key有關
	//enum的toString是變數的名字
	remainTime, //toString會是remainTime
	passTime //toString會是passTime
}