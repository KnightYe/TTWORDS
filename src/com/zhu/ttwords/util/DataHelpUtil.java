package com.zhu.ttwords.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.zhu.ttwords.bean.AbstractCommonBean;

public class DataHelpUtil extends DataBaseUtil {

	public static AbstractCommonBean getSingleBean(Class<?> clazz, String sql)
			throws InstantiationException, IllegalAccessException {
		return getSingleBean(clazz, sql, null);
	}

	public static AbstractCommonBean getSingleBean(Class<?> clazz, String sql,
			String[] params) throws InstantiationException,
			IllegalAccessException {
		List<AbstractCommonBean> list = getDataBean(clazz, sql, params);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public static List<AbstractCommonBean> getDataBean(Class<?> clazz,
			String sql) throws InstantiationException, IllegalAccessException {
		return getDataBean(clazz, sql, null);
	}

	public static List<AbstractCommonBean> getDataBean(Class<?> clazz,
			String sql, String[] params) throws InstantiationException,
			IllegalAccessException {
		List<AbstractCommonBean> list = new ArrayList<AbstractCommonBean>();
		Cursor cur = getDatabase().rawQuery(sql, params);
		while (cur.moveToNext()) {
			try {
				list.add(CursorUtil.cursorToBean(cur, clazz));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;

	}

	public static long saveBeanData(String Table, AbstractCommonBean bean) {
		try {
			return getDatabase().insert(Table, "UID",
					CursorUtil.beanToContentValues(bean));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 检查数据库路径,如果有SD卡，缓存存到卡上，如果没有，则存到缓存目录上
	 */
	public static boolean initDatabaseDir(Context context) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			databaseDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"TTWORDS/database");
		} else {
			databaseDir = context.getCacheDir();
		}
		boolean hasDir = databaseDir.exists();
		databaseDir.mkdirs();
		return hasDir;
	}

	/**
	 * 检查数据库文件，如果没有，则从内存中写入
	 */
	public static boolean initDatabaseFile(Context context) {
		dBFile = new File(databaseDir, DATABASE_NAME);
		boolean hasDB = dBFile.exists();
		if (!hasDB) {
			try {
				InputStream localInputStream = context.getAssets().open(
						DATABASE_NAME);
				FileOutputStream localFileOutputStream = new FileOutputStream(
						dBFile);
				byte[] arrayOfByte = new byte[1024];
				while (true) {
					int i = localInputStream.read(arrayOfByte);
					if (i <= 0) {
						localFileOutputStream.flush();
						localFileOutputStream.close();
						localInputStream.close();
						break;
					}
					localFileOutputStream.write(arrayOfByte, 0, i);
				}
			} catch (IOException localIOException) {
				localIOException.printStackTrace();
			}
		}
		return hasDB;
	}

	/**
	 * 检查数据库文件，如果没有，则从内存中写入
	 */
	public static boolean initDatabaseVersion(Context context) {

		int dbVersion = getDatabase().getVersion();
		int appVersion = getVersionCode(context);
		if (dbVersion == 0) {
			getDatabase().setVersion(appVersion);
			dbVersion = appVersion;
		}
		if (dbVersion != appVersion) {
			deleteDB(context);
			initDatabaseFile(context);
		}
		return getDatabase().getVersion() == appVersion;
	}
}
