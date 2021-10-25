package com.utils;

import android.util.Log;


/**
 * Created by bobby
 */
public class LogUtils {
	/**
	 * tag
	 */
	public final static String tag = "out";

	/**
	 * 是否需要开启Log(needLog：DEBUG模式开启， 非DEBUG模式关闭)
	 */
//	private static boolean needLog = BuildConfig.DEBUG;
	private static boolean needLog = true;


	static {
//		needLog = BuildConfig.DEBUG;
	}

	public static void i(String content) {
		if (needLog) {
			try {
				Log.i(tag, getLogInfo() + content);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}

	public static double i(String tag, String content) {
		if (needLog) {
			try {
				Log.i(tag, getLogInfo() + content);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
		return 0;
	}

	public static void d(String content) {
		if (needLog) {
			try {
				Log.d(tag, getLogInfo() + content);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}

	public static void d(String tag, String content) {
		if (needLog) {
			try {
				Log.d(tag, getLogInfo() + content);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}

	public static void e(String content) {
		if (needLog) {
			try {
				Log.e(tag, getLogInfo() + content);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}

	public static void e(String tag, String content) {
		if (needLog) {
			try {
				Log.e(tag, getLogInfo() + content);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}

	public static void v(String content) {
		if (needLog) {
			try {
				Log.v(tag, getLogInfo() + content);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}

	public static void v(String tag, String content) {
		if (needLog) {
			try {
				Log.v(tag, getLogInfo() + content);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}

	public static void w(String content) {
		if (needLog) {
			try {
				Log.w(tag, getLogInfo() + content);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}

	public static void w(String tag, String content) {
		if (needLog) {
			try {
				Log.w(tag, getLogInfo() + content);
			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
	}

	private static String getLogInfo() {
		return getClassName() + "(" + getLineNumber() + ")" + "$" + getMethodName() + ": ";
	}

	/**
	 * 获取Log所在的类名 （getStackTrace的索引根据调用的顺序来决定，可通过打印Log栈来获取）
	 *
	 * @return
	 */
	private static String getClassName() {
		try {
			String classPath = Thread.currentThread().getStackTrace()[5].getClassName();
			return classPath.substring(classPath.lastIndexOf(".") + 1);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return null;
	}

	/**
	 * 获取Log所在的方法名
	 *
	 * @return
	 */
	private static String getMethodName() {
		try {
			return Thread.currentThread().getStackTrace()[5].getMethodName();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return null;
	}

	/**
	 * 获取Log所在的行
	 *
	 * @return
	 */
	private static int getLineNumber() {
		try {
			return Thread.currentThread().getStackTrace()[5].getLineNumber();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		return 0;
	}
}
