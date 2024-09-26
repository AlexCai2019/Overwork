package me.ac.overwork.backend;

public class JSONFileException extends RuntimeException
{
	//json出現的例外
	public JSONFileException(String message)
	{
		super(message);
	}

	public JSONFileException(Throwable cause)
	{
		super(cause);
	}
}