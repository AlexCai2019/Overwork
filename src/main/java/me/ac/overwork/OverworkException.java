package me.ac.overwork;

public class OverworkException extends RuntimeException
{
	public OverworkException(String message)
	{
		super(message);
	}

	public OverworkException(Throwable cause)
	{
		super(cause);
	}
}