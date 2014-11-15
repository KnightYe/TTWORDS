package com.zhu.ttwords.util;

public class SuperMeMoUtil {

	public static int getDay(float ef, int n) {
		if (n == 1) {
			return 1;
		} else if (n == 2) {
			return 6;
		} else {
			return (int) Math.ceil(getDay(ef, n-1)*ef);
		}
	}

	public static float getEM(float ef, int level) {
		float EF = ef + (0.1f - (5f - level) * (0.08f + (5f - level) * 0.02f));
		if (EF <= 1.3f) {
			EF = 1.3f;
		} else if (EF >= 2.5f) {
			EF = 2.5f;
		}
		return EF;
	}
}
