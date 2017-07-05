package me.dfun.common.kit;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import me.dfun.modules.user.model.User;

/**
 * 用户工具类
 */
public class UserKit {
	public static final String CACHE_DICT_MAP = "cache_dict_map";
	public static final String CACHE_FLASH_MAP = "cache_flash_map";
	public static final String SECRET_KEY = "HBRS0LJC";

	/**
	 * 获取当前登录用户
	 */
	public static User getUser() {
		User user = (User) SecurityKit.getSubject().getPrincipal();
		if (user == null) {
			user = new User();
			SecurityKit.getSubject().logout();
		}
		return user;
	}

	/**
	 * 选择当前角色
	 */
	public static void chooseRole(long roleId) {
		User u = getUser();
		u.setRole(roleId);
		SecurityKit.clearCachedAuthorizationInfo();
	}

	/**
	 * 获取字典列表
	 */
	public static List<Record> getDictList(String code) {
		@SuppressWarnings("unchecked")
		Map<String, List<Record>> dictMap = (Map<String, List<Record>>) getCache(CACHE_DICT_MAP);
		if (dictMap == null) {
			dictMap = Maps.newHashMap();
			List<Record> dictList = Db.find(Db.getSqlPara("", 1));
			setDictMap(dictMap, dictList);
			setCache(CACHE_DICT_MAP, dictMap);
		}
		List<Record> dictList = dictMap.get(code);
		if (dictList == null) {
			dictList = Lists.newArrayList();
		}
		return dictList;
	}

	/**
	 * 添加字典缓存
	 */
	private static void setDictMap(Map<String, List<Record>> dictMap, List<Record> dictList) {
		for (Record r : dictList) {
			if (StringUtils.isNotEmpty(r.getStr("isParent"))) {
				List<Record> subList = Db.find(Db.getSqlPara("", r.getLong(QueryKit.KEY_ID)));
				dictMap.put(r.getStr(QueryKit.KEY_CODE), subList);
				setDictMap(dictMap, subList);
			}
		}
	}

	/**
	 * 设置缓存
	 */
	public static void setFlash(String key, Object value) {
		Map<String, Object> map = getFlashMap();
		map.put(key, value);
	}

	/**
	 * 获取缓存集合
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getFlashMap() {
		Object obj = UserKit.getCache(CACHE_FLASH_MAP);
		Map<String, Object> map = null;
		if (obj != null) {
			map = (Map<String, Object>) obj;
		} else {
			map = new ConcurrentHashMap<String, Object>();
			UserKit.setCache(CACHE_FLASH_MAP, map);
		}
		return map;
	}

	// ============== User Cache ==============

	public static Object getCache(String key) {
		return getCache(key, null);
	}

	public static Object getCache(String key, Object defaultValue) {
		Object obj = null;
		if (SecurityKit.getSubject() != null && SecurityKit.getSubject().getSession() != null) {
			obj = SecurityKit.getSubject().getSession().getAttribute(key);
		}
		return obj == null ? defaultValue : obj;
	}

	public static void setCache(String key, Object value) {
		if (SecurityKit.getSubject() != null && SecurityKit.getSubject().getSession() != null) {
			SecurityKit.getSubject().getSession().setAttribute(key, value);
		}
	}

	public static void removeCache(String key) {
		if (SecurityKit.getSubject() != null && SecurityKit.getSubject().getSession() != null) {
			SecurityKit.getSubject().getSession().removeAttribute(key);
		}
	}
}