package me.dfun.common.function;

import org.beetl.core.Context;
import org.beetl.core.Function;

import me.dfun.common.kit.UserKit;

/**
 * 数据字典列表
 */
public class DictListFunction implements Function {

	public Object call(Object[] obj, Context context) {
		return (Object) UserKit.getDictList((String) obj[0]);
	}
}