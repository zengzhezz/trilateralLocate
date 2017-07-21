package com.ibeacon.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author chenwentao
 * @version 0.1
 * @date 2014-10-17
 * @fucntion <pre>
 * </pre>
 *
 */
public abstract class TimeUtil {

	public static final String DEAULT_FORMAT = "yyyy-MM-dd hh:mm:ss";

	/**
	 * 对当前时间，以秒为单位向前或向后推送
	 *
	 * @param now
	 *            当前时间
	 *
	 * @param second
	 *            相加或减少秒
	 *
	 * @return 返回推送后的时间
	 */
	public static Date plusSecond(Date now, int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.SECOND, second);
		return cal.getTime();
	}

	/**
	 * 对当前时间，以秒为单位向前或向后推送
	 *
	 * @param now
	 *            当前时间
	 *
	 * @param second
	 *            相加或减少秒
	 *
	 * @return 返回推送后的时间
	 */
	public static Date subtractionSecond(Date now, int second) {
		return plusSecond(now, -second);
	}

	/**
	 * 计算时间相差多少秒
	 *
	 * @param begin
	 * @param end
	 *
	 * @return 相差多少秒
	 */
	public static int computeTimeSecond(Date begin, Date end) {
		return computeTime(begin, end);
	}

	/**
	 * 计算时间相差多少分钟
	 *
	 * @param begin
	 * @param end
	 *
	 * @return 相差多少分钟
	 */
	public static int computeTimeMinute(Date begin, Date end) {
		return (computeTime(begin, end) / 60);
	}

	/**
	 * 计算时间相差多少分钟
	 *
	 * @param begin
	 * @param end
	 *
	 * @return 相差多少分钟
	 */
	public static int computeTimeMinute(String begin, String end) {
		SimpleDateFormat format = new SimpleDateFormat(DEAULT_FORMAT);
		try {
			return (computeTime(format.parse(begin), format.parse(end)) / 60);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 计算两个时间差
	 *
	 * @param begin
	 * @param end
	 *
	 * @return 相差多少秒
	 */
	public static int computeTime(Date begin, Date end) {
		return computeTime(begin.getTime(), end.getTime());
	}

	/**
	 * 计算两个时间差
	 *
	 * @param begin
	 * @param end
	 *
	 * @return 相差多少秒
	 */
	public static int computeTime(long begin, long end) {
		Long interSeconds = (end - begin) / 1000L;
		return interSeconds.intValue();
	}

	/**
	 *
	 * 比较时间差
	 *
	 * @param begin
	 * @param end
	 * @param duration
	 *
	 * @return 如果时间差大于等于间隔返回true，否则返回false
	 */
	public static boolean compare(Date begin, Date end, int duration) {
		int durationReal = computeTimeSecond(begin, end);
        return durationReal >= duration;
	}

	/**
	 *
	 * 比较时间差
	 *
	 * @param begin
	 * @param end
	 *
	 * @param duration
	 *            秒为单位
	 *
	 * @return 如果时间差大于等于间隔返回true，否则返回false
	 */
	public static boolean compare(long begin, long end, int duration) {
		int durationReal = computeTime(begin, end);
        return durationReal >= duration;
	}

	public static String format(Date recTime, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(recTime);
	}

	/**
	 * @param beginTime
	 * @return
	 */
	public static Date parse(String beginTime) {
		SimpleDateFormat format = new SimpleDateFormat(DEAULT_FORMAT);
		try {
			return format.parse(beginTime);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Date date = new Date();
		System.out.println(plusSecond(date, -900));

		// long begin = new Date().getTime();
		// TimeUnit.MILLISECONDS.sleep(3000);
		// long end= new Date().getTime();
		// System.out.println(compare(begin,end,3));
	}

	// 获取UTC格式时间hhmmss
	public static String getUtcTime() {
		String utcTime = null;
		// 1、取得本地时间：
		Calendar cal = Calendar.getInstance();
		// 2、取得时间偏移量：
		int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
		// 3、取得夏令时差：
		int dstOffset = cal.get(Calendar.DST_OFFSET);
		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

		// 获取最终的时间
		SimpleDateFormat dateformat = new SimpleDateFormat("hhmmss");
		utcTime = dateformat.format(cal.getTime());

		return utcTime;
	}
}
