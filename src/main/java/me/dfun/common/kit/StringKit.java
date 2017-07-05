package me.dfun.common.kit;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 */
public class StringKit extends org.apache.commons.lang3.StringUtils {

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)) {
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	/**
	 * 缩略字符串（不区分中英文字符）
	 */
	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : str.toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 返回空字符串
	 */
	public static String clean(String in) {
		String out = in;
		if (in != null) {
			out = in.trim();
			if ("".equals(out)) {
				out = null;
			}
		}
		return out;
	}

	/**
	 * 转换为String类型
	 */
	public static String toString(Object val) {
		if (val == null) {
			return "";
		}
		try {
			return val.toString();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val) {
		if (val == null) {
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val) {
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val) {
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val) {
		return toLong(val).intValue();
	}

	/**
	 * 转换为大数型
	 */
	public static BigDecimal toBigDecimal(Object val) {
		if (val == null) {
			return null;
		}
		try {
			if (val instanceof BigDecimal) {
				return (BigDecimal) val;
			}
			return new BigDecimal(trim(val.toString()));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 移除空行
	 */
	public static String removeBlankLine(String val) {
		return val.replaceAll("(?m)^\\s*$" + System.lineSeparator(), "");
	}
}