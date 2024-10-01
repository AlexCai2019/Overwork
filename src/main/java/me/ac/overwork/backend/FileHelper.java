package me.ac.overwork.backend;

import me.ac.overwork.OverworkException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

@SuppressWarnings("UnnecessaryUnicodeEscape")
public class FileHelper
{
	public static final FileHelper instance = new FileHelper();

	private FileHelper() {}

	String readJSONFromFile() throws OverworkException
	{
		try
		{
			return Files.readString(Paths.get(JSONHelper.SAVE_FILE_NAME)); //讀取json
		}
		catch (NoSuchFileException nsfE)
		{
			throw new OverworkException("\u627e\u4e0d\u5230 \"" + JSONHelper.SAVE_FILE_NAME + "\" \u6a94\u6848"); //找不到 "fileName" 檔案
		}
		catch (CharacterCodingException ccE)
		{
			throw new OverworkException("\u6a94\u6848\u7de8\u78bc\u7570\u5e38\uff0c\u8acb\u6aa2\u67e5\u7de8\u78bc"); //檔案編碼異常，請檢查編碼
		}
		catch (IOException exception)
		{
			throw new OverworkException(exception);
		}
	}

	void writeJSONToFile(String value) throws OverworkException
	{
		try (FileWriter writer = new FileWriter(JSONHelper.SAVE_FILE_NAME))
		{
			writer.write(value);
		}
		catch (IOException exception)
		{
			throw new OverworkException(exception);
		}
	}

	void updateTimeFile(String fileName, int[] time)
	{
		try (FileWriter writer = new FileWriter(fileName))
		{
			writer.write(String.format("%05d:%02d:%02d", time[TimeOperation.HOUR], time[TimeOperation.MINUTE], time[TimeOperation.SECOND]));
		}
		catch (IOException exception)
		{
			throw new OverworkException(exception);
		}
	}
}