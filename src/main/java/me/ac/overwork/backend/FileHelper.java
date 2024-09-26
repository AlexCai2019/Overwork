package me.ac.overwork.backend;

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

	String readJSONFromFile(String fileName)
	{
		try
		{
			return Files.readString(Paths.get(fileName)); //讀取json
		}
		catch (NoSuchFileException nsfE)
		{
			throw new JSONFileException("\u627e\u4e0d\u5230 \"" + fileName + "\" \u6a94\u6848"); //找不到 "fileName" 檔案
		}
		catch (CharacterCodingException ccE)
		{
			throw new JSONFileException("\u6a94\u6848\u7de8\u78bc\u7570\u5e38\uff0c\u8acb\u6aa2\u67e5\u7de8\u78bc"); //檔案編碼異常，請檢查編碼
		}
		catch (IOException exception)
		{
			throw new JSONFileException(exception);
		}
	}

	void writeJSONToFile(String fileName, String value)
	{
		try (FileWriter writer = new FileWriter(fileName))
		{
			writer.write(value);
		}
		catch (IOException exception)
		{
			throw new JSONFileException(exception);
		}
	}
}