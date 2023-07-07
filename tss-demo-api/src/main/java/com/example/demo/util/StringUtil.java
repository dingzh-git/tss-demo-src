package com.example.demo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtil {

	public static String formatExcelDateTime(Date src) {
		if (src == null) {
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String formattedDate = formatter.format(src);
		
		return formattedDate;
	}
}
