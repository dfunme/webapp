package me.dfun.common.function;

import org.beetl.core.Context;
import org.beetl.core.Function;

import me.dfun.common.kit.DictKit;

/**
 * 获取数据字典
 */
public class DictFunction implements Function {

	public Object call(Object[] obj, Context context) {
		return (Object) DictKit.getName((String) obj[0], (String) obj[1], (String) obj[2]);
	}
}