package com.zhu.ttwords.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	public static String getCurrentDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss",
				Locale.getDefault());// �������ڸ�ʽ
		return df.format(new Date());
	}
}
