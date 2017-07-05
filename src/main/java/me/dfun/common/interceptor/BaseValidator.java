package me.dfun.common.interceptor;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.validate.Validator;

import me.dfun.common.kit.UserKit;

/**
 * 校验工具
 */
public abstract class BaseValidator extends Validator {

	/**
	 * 添加错误信息
	 */
	protected void addError(String errorKey, String errorMessage) {
		UserKit.setFlash(errorKey, errorMessage);
		super.addError(errorKey, errorMessage);
	}

	/**
	 * 添加缓存
	 */
	protected void setFlash(String key, Object value) {
		UserKit.setFlash(key, value);
	}

	/**
	 * 校验已存在
	 */
	protected void validateExist(String sql, String error) {
		List<Record> list = Db.find(sql);
		if (list.size() > 0) {
			addError("error", error);
		}
	}
}