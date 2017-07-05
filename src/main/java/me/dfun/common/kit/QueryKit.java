package me.dfun.common.kit;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 查询工具
 */
public class QueryKit {
	public static final String KEY_ID = "id";
	public static final String KEY_UID = "uid";
	public static final String KEY_NAME = "name";
	public static final String KEY_CODE = "code";
	public static final String KEY_TYPE = "type";
	public static final String KEY_VALUE = "value";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_PERMISSION = "permission";
	public static final String KEY_AREA_ID = "area_id";
	public static final String KEY_OFFICE_ID = "office_id";
	public static final String KEY_ROLE_ID = "role_id";
	public static final String KEY_PARENT_ID = "parent_id";
	public static final String KEY_PARENT_IDS = "parent_ids";
	public static final String KEY_PARENT_NAME = "parent_name";
	public static final String KEY_STATUS = "status";
	public static final String KEY_CREATE_BY = "create_by";
	public static final String KEY_CREATE_DATE = "create_date";
	public static final String KEY_UPDATE_BY = "update_by";
	public static final String KEY_UPDATE_DATE = "update_date";
	public static final String KEY_DEL_FLAG = "del_flag";

	/**
	 * 获取表字段/注释
	 */
	public static List<Record> columnMaps(String schema, String table) {
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select table_name, column_name, column_comment, data_type, is_nullable from information_schema.`columns` where table_schema = '");
		sql.append(schema).append("' and table_name = '");
		sql.append(table);
		sql.append("' and column_name not in('id', 'parent_ids', 'update_by', 'update_date', 'del_flag')");
		return Db.find(sql.toString());
	}

	/**
	 * 获取参数集合
	 */
	public static Map<String, Object> getParam(HttpServletRequest request) {
		return getParam(request, "");
	}

	/**
	 * 获取参数集合
	 */
	public static Map<String, Object> getParam(HttpServletRequest request, String array) {
		Map<String, Object> map = Maps.newHashMap();
		for (Map.Entry<String, String[]> e : request.getParameterMap().entrySet()) {
			if (array.contains(e.getKey())) {
				map.put(e.getKey(), request.getParameterValues(e.getKey()));
			} else {
				map.put(e.getKey(), request.getParameter(e.getKey()));
			}
		}
		return map;
	}

	/**
	 * 获取UTF-8编码字符串
	 */
	public static String getStr(String data) {
		return getStr(data, "UTF-8");
	}

	/**
	 * 获取指定编码字符串
	 */
	public static String getStr(String data, String format) {
		try {
			return new String(data.getBytes("ISO-8859-1"), StringUtils.isEmpty(format) ? "UTF-8" : format);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	/**
	 * 获取字符串参数
	 */
	public static String getStr(Map<String, Object> map, String key, String defValue) {
		Object o = map.get(key);
		try {
			return o == null || "".equals(o) ? defValue : StringEscapeUtils.escapeHtml4(o.toString().trim());
		} catch (Exception e) {
			return defValue;
		}
	}

	/**
	 * 获取字符串参数
	 */
	public static String getStr(Map<String, Object> map, String key) {
		return getStr(map, key, "");
	}

	/**
	 * 获取字符串数组参数
	 */
	public static String[] getStrArray(Map<String, Object> map, String key) {
		Object o = map.get(key);
		try {
			return (String[]) o;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取整形参数值
	 */
	public static Integer getInt(Map<String, Object> map, String key, Integer defValue) {
		Object o = map.get(key);
		try {
			return o == null || "".equals(o) ? defValue : Integer.valueOf(o.toString());
		} catch (Exception e) {
			return defValue;
		}
	}

	/**
	 * 获取整形参数值
	 */
	public static Integer getInt(Map<String, Object> map, String key) {
		return getInt(map, key, null);
	}

	/**
	 * 获取长整形参数值
	 */
	public static Long getLong(Map<String, Object> map, String key, Long defValue) {
		Object o = map.get(key);
		try {
			return o == null || "".equals(o) ? defValue : Long.valueOf(o.toString());
		} catch (Exception e) {
			return defValue;
		}

	}

	/**
	 * 获取长整形参数值
	 */
	public static Long getLong(Map<String, Object> map, String key) {
		return getLong(map, key, null);
	}

	/**
	 * 获取浮点型参数值
	 */
	public static Float getFloat(Map<String, Object> map, String key, Float defValue) {
		Object o = map.get(key);
		try {
			return o == null || "".equals(o) ? defValue : Float.valueOf(o.toString());
		} catch (Exception e) {
			return defValue;
		}

	}

	/**
	 * 获取浮点型参数值
	 */
	public static Float getFloat(Map<String, Object> map, String key) {
		return getFloat(map, key, null);
	}

	/**
	 * 获取双精度浮点型参数值
	 */
	public static Double getDouble(Map<String, Object> map, String key, Double defValue) {
		Object o = map.get(key);
		try {
			return o == null || "".equals(o) ? defValue : Double.valueOf(o.toString());
		} catch (Exception e) {
			return defValue;
		}

	}

	/**
	 * 获取双精度浮点型参数值
	 */
	public static Double getDouble(Map<String, Object> map, String key) {
		return getDouble(map, key, null);
	}

	/**
	 * 获取日期型参数
	 */
	public static Date getDate(Map<String, Object> map, String key) {
		try {
			return DateKit.toDate(map.get(key).toString());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取布尔参数
	 */
	public static boolean getBoolean(Map<String, Object> map, String key) {
		return "true".equalsIgnoreCase(getStr(map, key, "false"));
	}
}