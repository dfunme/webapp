package me.dfun.common.kit;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.plugin.activerecord.Record;

/**
 * 字典工具类
 */
public class DictKit {

	/**
	 * 根据数据值和代码获取名称
	 * 
	 * @param value
	 * @param code
	 * @param defaultName
	 * @return
	 */
	public static String getName(String value, String code, String defaultName) {
		if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(value)) {
			for (Record r : UserKit.getDictList(code)) {
				if (value.equals(r.getStr(QueryKit.KEY_CODE))) {
					return r.getStr(QueryKit.KEY_NAME);
				}
			}
		}
		return defaultName;
	}

	/**
	 * 根据名称和代码获取数据值
	 * 
	 * @param name
	 * @param code
	 * @param defaultValue
	 * @return
	 */
	public static String getValue(String name, String code, String defaultValue) {
		if (StringUtils.isNotBlank(code) && StringUtils.isNotBlank(name)) {
			for (Record r : UserKit.getDictList(code)) {
				if (name.equals(r.getStr(QueryKit.KEY_NAME))) {
					return r.getStr(QueryKit.KEY_CODE);
				}
			}
		}
		return defaultValue;
	}
}