package me.ac.overwork.backend;

public class JSONFileException extends RuntimeException
{
	//json出現的異常
	public JSONFileException(String message)
	{
		super(message);
	}

	public JSONFileException(Throwable cause)
	{
		super(cause);
	}
}