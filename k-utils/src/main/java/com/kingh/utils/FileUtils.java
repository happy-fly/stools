package com.kingh.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

	public static String getFileContent(InputStream ins) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
			String line = null;
			StringBuffer sbuff = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				sbuff.append(line);
			}
			return sbuff.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Map<Integer, String> getFileContentWithLineNumber(InputStream ins) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
			String line = null;
			Map<Integer, String> content = new HashMap<>();
			int lineCount = 0;
			while ((line = reader.readLine()) != null) {
				content.put(lineCount ++, line);
			}
			return content;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
