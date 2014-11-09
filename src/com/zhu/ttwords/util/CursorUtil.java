package com.zhu.ttwords.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;

import com.zhu.ttwords.bean.AbstractCommonBean;

public class CursorUtil {
	public static AbstractCommonBean cursorToBena(Cursor cur, Class<?> clazz)
			throws InstantiationException, IllegalAccessException,
			NoSuchMethodException, IllegalArgumentException,
			InvocationTargetException {
		Field[] fields = clazz.getDeclaredFields();
		AbstractCommonBean bean = (AbstractCommonBean) clazz.newInstance();
		for (Field field : fields) {
			String name = field.getName();
			name = name.substring(0, 1).toUpperCase(Locale.getDefault())
					+ name.substring(1);
			String type = field.getGenericType().toString();
			if (type.equals("class java.lang.Integer")) {
				Method m = clazz.getMethod("set" + name,Integer.class);
				int param = cur.getInt(cur.getColumnIndex(name
						.toUpperCase(Locale.getDefault())));
				m.invoke(bean, param);
			} else if (type.equals("class java.lang.String")) {
				Method m = clazz.getMethod("set" + name,String.class);
				String param = cur.getString(cur.getColumnIndex(name
						.toUpperCase(Locale.getDefault())));
				m.invoke(bean, param);
			}
		}
		return bean;
	}

	public static ContentValues beanToContentValues(AbstractCommonBean bean) {

		return null;
	}
}
