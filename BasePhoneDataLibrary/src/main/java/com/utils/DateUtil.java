package com.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.icu.text.DateTimePatternGenerator.DAY;

/**
 * 时间转换工具类
 */

public class DateUtil {

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sf = null;
	private static SimpleDateFormat sf2 = null;
	private static Calendar c = Calendar.getInstance();



	/**
	 * 日期格式字符串转换成时间戳
	 * @param format 如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static long date2TimeStamp(String date_str,String format){
		if(TextUtils.isEmpty(format)){
			format = "yyyy-MM-dd HH:mm:ss";
		}
		try {
			@SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
			long a = sdf.parse(date_str).getTime();
//			long a = sdf.parse(date_str).getTime()/1000;
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	//日期格式字符串转换成时间戳
	public static long date3TimeStamp(String date_str){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long a = sdf.parse(date_str).getTime();
//			long a = sdf.parse(date_str).getTime()/1000;
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}


	//日期格式字符串转换成时间戳
	public static long date4TimeStamp(String date_str){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			long a = sdf.parse(date_str).getTime();
//			long a = sdf.parse(date_str).getTime()/1000;
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	//日期格式字符串转换成时间戳(23点59分59秒字符串)
	public static long date5TimeStamp(String date_str){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
			long a = sdf.parse(date_str).getTime();
//			long a = sdf.parse(date_str).getTime()/1000;
			return a;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/*
	 *计算time2减去time1的差值
	 * */
	public static String getDistanceTime(long time1, long time2) {
		long day = 0;
		long diff = 0;
		//LogUtil.i("out","起始时间:"+time1+"____结束时间:"+time2);
		if (time1 < time2) {
			diff = time2 - time1;
		}
//		else {
//			diff = time1 - time2;
//		}
		//LogUtil.i("out","相减之后:"+diff);
		day = (diff * 1000) / (24 * 60 * 60 * 1000);
		//LogUtil.i("out","时间:"+day);
		if (day != 0)
			return day + "";
		return "";
	}


	/**
	 * 将字符串数据转化为毫秒数
	 */
	public static long StrToTimeInMillis(String dateTime) {
		//String dateTime="20121025112950";
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("时间转化后的毫秒数为：" + c.getTimeInMillis());
		return c.getTimeInMillis();
	}

	/**
	 * 将毫秒数转化为时间
	 */
	public static String TimeInMillisToDate(String sstime) {
		//String sstime="1339033320000";
		Date date = new Date(sstime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("毫秒数转化后的时间为：" + sdf.format(date));
		return sdf.format(date);
	}


	/*将年月日字符串转换为时间*/
	public static String StringChangTime(Date date) {
		sf = new SimpleDateFormat("yyyy年MM月dd日");
		return sf.format(date);
	}

	/*将时间转换为年月日字符串*/
	public static Date TimeStringChang(String str) {
		sf = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = null;
		try {
			date = sf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}


	/**
	 * 计算两个日期之间相差的天数
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int daysBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}


	//Unicode转UTF-8的转化
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len; ) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException(
										"Malformed   \\uxxxx   encoding.");
						}

					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	/**
	 * type=1顺序，非1倒序
	 *
	 * @Name
	 * @Return_type List<Date>
	 * @Todo 排序
	 */
	public static List<Date> SortDate(List<Date> dates, int type) {
		Collections.sort(dates, new DateComparator(type));
		return dates;
	}

	static class DateComparator implements Comparator<Date> {
		private int type;

		public DateComparator(int type) {
			// TODO Auto-generated constructor stub
			this.type = type;
		}

		public int compare(Date obj1, Date obj2) {

			Date begin = obj1;
			Date end = obj2;
			boolean b = false;
			b = begin.after(end);
			if (type != 1) {
				b = !b;
			}
			if (b) {
				return 1;

			} else {
				return -1;

			}
		}
	}

	private static String getWeek(long lcc_time) {
		// 再转换为时间

		Date date = new Date(lcc_time * 1000L);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// int hour=c.get(Calendar.DAY_OF_WEEK);
		// hour中存的就是周几了，其范围 1~7
		// 1=周日 7=周六，其他类推
		return new SimpleDateFormat("EEEE").format(c.getTime());
	}

	public static String getWeekStr(long sdate) {
		String str = "";
		str = getWeek(sdate);
		if ("1".equals(str)) {
			str = "周日";
		} else if ("2".equals(str)) {
			str = "周一";
		} else if ("3".equals(str)) {
			str = "周二";
		} else if ("4".equals(str)) {
			str = "周三";
		} else if ("5".equals(str)) {
			str = "周四";
		} else if ("6".equals(str)) {
			str = "周五";
		} else if ("7".equals(str)) {
			str = "周六";
		}
		str = str.replaceAll("星期", "周");
		return str;
	}


	//获取当天(0点0分0秒)时间戳
	public static long getToday0(){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
				0, 0, 0);
		Date beginOfDate = calendar1.getTime();

		return beginOfDate.getTime();
	}

	//获取当天(23点59分59秒)时间戳
	public static long getToday23(){
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
				23, 59, 59);
		Date beginOfDate = calendar1.getTime();

		return beginOfDate.getTime();
	}


	/**
	 * 获取当前时间的前一天时间
	 * @param cl
	 * @return
	 */
	public static Calendar getBeforeDay(Calendar cl){
		//使用roll方法进行向前回滚
		//cl.roll(Calendar.DATE, -1);
		//使用set方法直接进行设置
		int day = cl.get(Calendar.DATE);
		cl.set(Calendar.DATE, day-1);
		return cl;
	}
	//获取当前时间的前一天时间 23点59分59秒时间
	public static Calendar getBeforeDay23(Calendar cl){
		//使用roll方法进行向前回滚
		//cl.roll(Calendar.DATE, -1);
		//使用set方法直接进行设置
		int day = cl.get(Calendar.DATE);
		cl.set(Calendar.DATE, day-1);
		cl.set(cl.get(Calendar.YEAR), cl.get(Calendar.MONTH), cl.get(Calendar.DAY_OF_MONTH),
				23, 59, 59);
		return cl;
	}

	//过去六天不包含当天
	public static Date pastSixDays() {
		c.setTime(new Date());
		c.add(Calendar.DATE, -6);
		Date d = c.getTime();
		String day = format.format(d);
		LogUtils.e("过去六天：" + day);
		return d;
	}

	//过去七天
	public static Date pastSevenDays() {
		c.setTime(new Date());
		c.add(Calendar.DATE, -7);
		Date d = c.getTime();
		String day = format.format(d);
		//LogUtil.i("过去七天：" + day);
		return d;
	}

	//在当前的日期之上，加N天
	public static String pastTimeAddDay(String currentTime,int day){
		sf = new SimpleDateFormat("yyyy-MM-dd");
		LogUtils.e("添加时间前：" + currentTime);
		Date date;
		if(!TextUtils.isEmpty(currentTime)){
			try {
				date = sf.parse(currentTime);
				date.setDate(date.getDate() + day);
				currentTime = sf.format(date);
			}catch (Exception e){
				e.printStackTrace();
			}

		}
		LogUtils.e("添加时间后：" + currentTime);
		return currentTime;
	}

	/*将时间类型转换为字符串不要分秒*/
//	public static String StrChangTime(Date date) {
//		sf = new SimpleDateFormat("yyyy-MM-dd");
//		return sf.format(date);
//	}


	//过去七天(0点0分0秒)时间
	public static Date pastSevenDays0() {
		c.setTime(new Date());
		c.add(Calendar.DATE, -7);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
				0, 0, 0);
		Date d = c.getTime();
		String day = format.format(d);
		//LogUtil.i("过去七天：" + day);
		return d;
	}

	//过去一月
	public static Date pastMonth() {
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		Date m = c.getTime();
		String mon = format.format(m);
		//LogUtil.i("过去一个月：" + mon);
		return m;
	}

	//过去一月-1天
	public static Date pastMonth1() {
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		Date m = c.getTime();
		m.setDate(m.getDate() - 1);
		String mon = format.format(m);
		//LogUtil.i("过去一个月：" + mon);
		return m;
	}

	//指定时间过去一月
	public static Date customPastMonth(Date date) {
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		Date m = c.getTime();
		String mon = format.format(m);
		//LogUtil.i("过去一个月：" + mon);
		return m;
	}

	//指定时间过去一月-1天
	public static Date customPastMonth1(Date date) {
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		Date m = c.getTime();
		m.setDate(m.getDate() - 1);
		String mon = format.format(m);
		//LogUtil.i("过去一个月：" + mon);
		return m;
	}

	//过去三个月
	public static Date pastThreeMonth() {
		c.setTime(new Date());
		c.add(Calendar.MONTH, -3);
		Date m3 = c.getTime();
		String mon3 = format.format(m3);
		//LogUtil.i("过去三个月：" + mon3);
		return m3;
	}

	//过去一年
	public static Date pastYear() {
		c.setTime(new Date());
		c.add(Calendar.YEAR, -1);
		Date y = c.getTime();
		String year = format.format(y);
		//LogUtil.i("过去一年：" + year);
		return y;
	}

	/**
	 * 获得两个日期间距多少天
	 *
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static long getTimeDistance(Date beginDate, Date endDate) {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(beginDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, fromCalendar.getMinimum(Calendar.HOUR_OF_DAY));
		fromCalendar.set(Calendar.MINUTE, fromCalendar.getMinimum(Calendar.MINUTE));
		fromCalendar.set(Calendar.SECOND, fromCalendar.getMinimum(Calendar.SECOND));
		fromCalendar.set(Calendar.MILLISECOND, fromCalendar.getMinimum(Calendar.MILLISECOND));

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, fromCalendar.getMinimum(Calendar.HOUR_OF_DAY));
		toCalendar.set(Calendar.MINUTE, fromCalendar.getMinimum(Calendar.MINUTE));
		toCalendar.set(Calendar.SECOND, fromCalendar.getMinimum(Calendar.SECOND));
		toCalendar.set(Calendar.MILLISECOND, fromCalendar.getMinimum(Calendar.MILLISECOND));

		long dayDistance = (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / DAY;
		dayDistance = Math.abs(dayDistance);

		return dayDistance;
	}

	/**
	 * 获取两个日期的月数差
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public static long getDifferMonth(Date fromDate, Date toDate) {
		Calendar fromDateCal = Calendar.getInstance();
		Calendar toDateCal = Calendar.getInstance();
		fromDateCal.setTime(fromDate);
		toDateCal.setTime(toDate);

		int fromYear =  fromDateCal.get(Calendar.YEAR);
		int toYear = toDateCal.get((Calendar.YEAR));
		if (fromYear == toYear) {
			return Math.abs(fromDateCal.get(Calendar.MONTH) - toDateCal.get(Calendar.MONTH));
		} else {
			int fromMonth = 12 - (fromDateCal.get(Calendar.MONTH) + 1);
			int toMonth = toDateCal.get(Calendar.MONTH) + 1;
			return Math.abs(toYear - fromYear - 1) * 12 + fromMonth + toMonth;
		}
	}

	/**
	 * 获取是否相差一个月
	 * @param mCalendarType 日历类型 0（历史委托/历史成交）不包含当天，否则包含当天
	 * @param fromDate  选中的开始时间
	 * @param toDate    选中的结束时间
	 * @return
	 */
	public static boolean getDifferMonth(int mCalendarType, Date fromDate, Date toDate) {
		//public static final int HISTORY = 0;
		//public static final int CAPITAL_FLOW = -1;

		Date pastDate = customPastMonth(toDate); //过去一个月
		return daysBetween(fromDate, toDate) >= 0 && daysBetween(fromDate, toDate) <= daysBetween(pastDate, toDate);
	}

/*	public static boolean getDifferMonth(int mCalendarType, Date fromDate, Date toDate) {
		//public static final int HISTORY = 0;
		//public static final int CAPITAL_FLOW = -1;

		Date pastDate = null;
		Date currentDate = null;
		if(mCalendarType == 0){
			//过去一个月不包含当天
			pastDate = customPastMonth1(toDate);
			//当前时间不包含当天
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(toDate);
			currentDate = DateUtilsl.getBeforeDay(calendar).getTime();
		}else{
			//过去一个月包含当天
			pastDate = customPastMonth(toDate);
			//当前时间包含当天
			currentDate = toDate;
		}
		return daysBetween(fromDate, toDate) >= 0 && daysBetween(fromDate, toDate) <= daysBetween(pastDate, currentDate);
	}*/
}
