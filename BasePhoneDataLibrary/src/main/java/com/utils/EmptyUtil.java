package com.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 判断数据结构是否为空
 *
 * @author bobby
 * @date 2018/6/20
 */


public class EmptyUtil {
	/**
	 * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
	 */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}

	public static String isEmptyString(String str){
		return isEmpty(str) ? " " : str;
	}

	public static boolean isEmpty(List<?> list) {
		if (list == null || list.size() == 0) {
			return true;
		}

		return false;
	}

	public static boolean isEmpty(Set<?> list) {
		if (list == null || list.size() == 0) {
			return true;
		}

		return false;
	}

	public static boolean isEmpty(Map<?, ?> map) {
		if (map == null || map.size() == 0) {
			return true;
		}

		return false;
	}

	public static boolean isEmpty(Object[] array) {
		if (array == null || array.length == 0) {
			return true;
		}

		return false;
	}

	public static <T> T checkNotNull(T object, String message){
		if(object == null){
			throw  new NullPointerException(message);
		}
		return object;
	}
}
