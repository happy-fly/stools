package com.kingh.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class POIUtils {

	/**
	 * 解析文件，构建workbook
	 * 
	 * @param path
	 * @return
	 */
	public static Workbook parse(String path) {
		if (StringUtils.isBlank(path)) {
			throw new NullPointerException("path is null");
		}

		try {
			InputStream inp = new FileInputStream(path);
			Workbook wb = WorkbookFactory.create(inp);
			return wb;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 创建一个空的wb
	 * 
	 * @return
	 */
	public static Workbook create() {
		Workbook wb = new HSSFWorkbook();
		return wb;
	}

	public static String write(Workbook wb) {
		OutputStream out = null;
		try {
			String fid = UUID.randomUUID().toString();
			out = new FileOutputStream("/data/download/" + fid);
			wb.write(out);
			return fid;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String write(Workbook wb, String path) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(path);
			wb.write(out);
			return path;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
