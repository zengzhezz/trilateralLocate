package com.ibeacon.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;

public class ConvertUtils {

	static {
		registerDateConverter();
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数), 组合成List.
	 *
	 * @param collection
	 *            来源集合.
	 * @param propertyName
	 *            要提取的属性名.
	 */
	@SuppressWarnings("unchecked")
	public static List convertElementPropertyToList(
			final Collection collection, final String propertyName) {
		List list = new ArrayList();

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}

		return list;
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数), 组合成由分割符分隔的字符串.
	 *
	 * @param collection
	 *            来源集合.
	 * @param propertyName
	 *            要提取的属性名.
	 * @param separator
	 *            分隔符.
	 */
	@SuppressWarnings("unchecked")
	public static String convertElementPropertyToString(
			final Collection collection, final String propertyName,
			final String separator) {
		List list = convertElementPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	/**
	 * 转换字符串到相应类型.
	 *
	 * @param value
	 *            待转换的字符串.
	 * @param toType
	 *            转换目标类型.
	 */
	public static Object convertStringToObject(String value, Class<?> toType) {
		try {
			return org.apache.commons.beanutils.ConvertUtils.convert(value,
					toType);
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 定义日期Converter的格式: yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss
	 */
	private static void registerDateConverter() {
		DateConverter dc = new DateConverter();
		dc.setUseLocaleFormat(true);
		dc.setPatterns(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" });
		org.apache.commons.beanutils.ConvertUtils.register(dc, Date.class);
	}

	public static void main(String[] args) {
		String ss = "$GPGGA,065342,3030.63828,N,11425.34850,E,1,03,1.0,48.37,M,0.0,M,0,";
		System.out.println(getCheckSum(ss));
	}

	// 校验和转换
	public static String getCheckSum(String cmd) {
		String check = null;

		char[] a = cmd.toCharArray();
		int i;
		int result;

		for (result = a[1], i = 2; i < a.length; i++) {
			result ^= a[i];
		}

		// 转换为16进制
		check = Integer.toHexString(result);
		return check;
	}

	// 转换经纬度格式ddmm.mmmm
	public static String convertLogLat(String str) {

		StringBuffer bu = new StringBuffer();

		String[] spStr = str.split("\\.");

		String headData = spStr[0];
		String lastData = spStr[1];

		double d_appendDate = Double.valueOf("0." + lastData);

		DecimalFormat df = new DecimalFormat("#.00000");
		String res = df.format(d_appendDate * 60);

		bu.append(headData);
		bu.append(res);

		return bu.toString();
	}

}
