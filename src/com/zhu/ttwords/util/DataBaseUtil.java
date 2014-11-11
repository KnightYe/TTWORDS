package com.zhu.ttwords.util;

import java.io.File;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseUtil extends DatabaseUtils {

	static final String DATABASE_NAME = "TTWORDS.DB";
	static File databaseDir;
	static SQLiteDatabase db;
	static File dBFile;

	static SQLiteDatabase getDatabase() {
		if (db == null) {
			db = SQLiteDatabase.openOrCreateDatabase(dBFile, null);
		}
		return db;
	}

	static void deleteDB(Context context) {
		dBFile = new File(databaseDir, DATABASE_NAME);
		if (dBFile.exists()) {
			dBFile.delete();
		}
	}

	static int getVersionCode(Context context) {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
		PackageInfo packInfo;
		int versionCode = 0;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			versionCode = packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
}
