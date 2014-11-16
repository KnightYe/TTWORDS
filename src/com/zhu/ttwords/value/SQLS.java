package com.zhu.ttwords.value;

public class SQLS {
	/**
	 * Login
	 */
	public static final String Loading_UserInfo = "SELECT* FROM TT_USER "
			+ "WHERE USERNAME = ? AND PASSWORD = ?;";
	/**
	 * Main
	 */
	// ѡ����Ϥ�ĵ�����
	public static final String Main_All_Count = "SELECT count(*) AS 'COUNT' "
			+ "FROM TT_REPERTORY_JP WHERE UID = ?;";
	// ѡ������������
	public static final String Main_Mission_Count = "SELECT count(*) AS 'COUNT' "
			+ "FROM TT_REPERTORY_JP WHERE UID = ? AND UPDATE_DATE < ?;";
	/**
	 * Study
	 */
	// ѡ��ѧϰ�Ͳ��Ե�����
	public static final String Study_StudyWords = "select * from TT_RESOURCE_JP "
			+ " WHERE WID NOT IN (SELECT WID FROM TT_REPERTORY_JP WHERE UID= ?) LIMIT ?";
	// ѡ����ϰ�Ͳ��Եĵ���
	public static final String Study_ReviewWords = "SELECT "
			+ "A.WID, A.COUNT, A.CONTENT, A. EXPLAIN, A.POS, A.PRONOUNCE, A.SOUND, A.BOOK, A.CHAPTER,A.CLASS, KIND "
			+ "FROM	TT_RESOURCE_JP A  LEFT JOIN TT_REPERTORY_JP B  ON A.WID = B.WID "
			+ "WHERE	 B.UID = ?  AND UPDATE_DATE < ?;";

	public static final String Study_getResource = "SELECT* FROM TT_REPERTORY_JP WHERE WID= ?;";

	public static final String Study_updateWordInfo = "TNAME = ? AND WID = ?";
	/**
	 * Setting
	 */
	public static final String Setting_SelectUserBean = "SELECT * FROM TT_USER WHERE USERNAME = ?;";
}
