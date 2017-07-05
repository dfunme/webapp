package me.dfun.common.kit;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.kit.StrKit;

/**
 * 日期工具类
 */
public class DateKit {
	protected static Logger log = LoggerFactory.getLogger(DateKit.class);
	public static String dateFormat = "yyyy-MM-dd";
	public static String timeFormat = "yyyy-MM-dd HH:mm:ss";

	public static void setDateFromat(String dateFormat) {
		if (StrKit.isBlank(dateFormat))
			throw new IllegalArgumentException("dateFormat can not be blank.");
		DateKit.dateFormat = dateFormat;
	}

	public static void setTimeFromat(String timeFormat) {
		if (StrKit.isBlank(timeFormat))
			throw new IllegalArgumentException("timeFormat can not be blank.");
		DateKit.timeFormat = timeFormat;
	}

	public static Date toDate(String dateStr) {
		return toDate(dateStr, new String[] { dateFormat, timeFormat });
	}

	public static Date toDate(String dateStr, String format) {
		return toDate(dateStr, new String[] { format });
	}

	public static Date toDate(String dateStr, String... format) {
		try {
			return DateUtils.parseDate(dateStr, format);
		} catch (ParseException e) {
			log.error("toDate error : {}", e.getMessage());
			return null;
		}
	}

	public static Timestamp toTimestamp(Date date) {
		try {
			return new Timestamp(date.getTime());
		} catch (Exception e) {
			log.error("toTimestamp error : {}", e.getMessage());
			return null;
		}
	}

	public static Timestamp toTimestamp(String dateStr) {
		return toTimestamp(dateStr, new String[] { dateFormat, timeFormat });
	}

	public static Timestamp toTimestamp(String dateStr, String format) {
		return toTimestamp(dateStr, new String[] { format });
	}

	public static Timestamp toTimestamp(String dateStr, String... format) {
		return toTimestamp(toDate(dateStr, format));
	}

	public static String toStr(Date date) {
		return toStr(date, DateKit.dateFormat);
	}

	public static String toStr(Date date, String format) {
		return DateFormatUtils.format(date, format);
	}
}