package me.ac.overwork;

/**
 * 任何在後端建立時期發生的例外。被捕捉到的例外也會被轉化為這個例外。
 *
 * @since 2024/09/26
 * @author Alex Cai
 */
public class OverworkException extends RuntimeException
{
	/**
	 * 建構子，設定訊息。
	 *
	 * @param message 例外訊息
	 * @since 2024/09/26
	 * @author Alex Cai
	 */
	public OverworkException(String message)
	{
		super(message);
	}

	/**
	 * 建構子，從另一個例外設定訊息
	 *
	 * @param cause 另一個例外。
	 * @since 2024/09/26
	 * @author Alex Cai
	 */
	public OverworkException(Throwable cause)
	{
		super(cause);
	}
}