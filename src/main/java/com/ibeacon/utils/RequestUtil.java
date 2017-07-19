package com.ibeacon.utils;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * <pre>
 *
 * @author xiaheng
 *
 * @version 0.1
 *
 * 修改日期: Sep 12, 2013
 * 修改人 :  xiaheng
 * 修改说明: 初步完成
 * 复审人 ：
 * </pre>
 */
public class RequestUtil {

	/**
	 * 获取request对象中的键/值对，存入map中并返回
	 *
	 * @param request
	 * @return map
	 */
	public static Map<String, String> getParam(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement().toString();
			String paramValue = request.getParameter(paramName);
			map.put(paramName, paramValue);
		}
		return map;
	}

	/**
	 * 从request中拿参数(过滤所有有可能会产生XSS攻击的特殊字符) 如果参数在request中不存在，则返回defaultValue
	 *
	 * @param request
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getString(HttpServletRequest request, String key,
								   String defaultValue) {
		String value = request.getParameter(key);
		if (value == null || "".equals(value.trim())) {
			return defaultValue;
		}
		return StringUtil.escapeHTMLTag(value.trim());
	}

	/**
	 * 为textarea进行录入的方法
	 *
	 * @param request
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getStringFromAreaText(HttpServletRequest request,
											   String key, String defaultValue) {
		String value = request.getParameter(key);
		if (value == null || "".equals(value.trim())) {
			return defaultValue;
		}
		return StringUtil.parserToHTMLForTextArea(value);
	}

	/**
	 * 获得请求的数值内容
	 *
	 * @param request
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Integer getInt(HttpServletRequest request, String key,
								 Integer defaultValue) {
		String value = request.getParameter(key);
		if (value == null || "".equals(value.trim())) {
			return defaultValue;
		}
		return Integer.valueOf(value.trim());
	}

	/**
	 * 判断是否是IP地址
	 *
	 * @param paramString
	 * @return
	 */
	public static boolean isLicitIp(String paramString) {
		if ((paramString == null) || (paramString.length() == 0))
			return false;
		String str = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";
		Pattern localPattern = Pattern.compile(str);
		Matcher localMatcher = localPattern.matcher(paramString);
		return (localMatcher.find());
	}

	/**
	 * 得到剔除过<html>，</html>,<script>,</script>过后的的字符串
	 *
	 * @param value
	 *            可以为null，则返回null
	 * @param request
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getSecureString(HttpServletRequest request,
										 String key, String defaultValue) {
		String value = getString(request, key, defaultValue);

		if (value == null)
			return value;
		value.trim();

		return filterScript(value);

	}

	/**
	 * 去掉输入中的<html>，</html>,<script>,</script>过后的的字符串,把结果返回
	 *
	 * @param value
	 *            可以为null，则返回null
	 * @return
	 */
	public static String filterScript(String value) {

		if (value == null)
			return null;

		String regex = "<html>|<script>|</html>|</script>";
		// 不区分大小写
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(value);

		String result = matcher.replaceAll("");

		return result;
	}

	/**
	 * 输入一个字符串数组，返回一个剔除掉<html>，</html>,<script>,</script>的字符串数组
	 * 如果输入为null，则返回null
	 *
	 * @param values
	 * @return
	 */
	public static String[] filterScript(String[] values) {
		if (values == null)
			return null;

		String[] newString = new String[values.length];

		int i = 0;
		for (String value : values) {
			String mid = filterScript(value);
			newString[i] = mid;
			i++;
		}

		return newString;
	}
}
