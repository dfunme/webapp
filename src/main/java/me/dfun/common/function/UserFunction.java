package me.dfun.common.function;

import org.beetl.core.Context;
import org.beetl.core.Function;

import me.dfun.common.kit.UserKit;

/**
 * 获取当前用户
 */
public class UserFunction implements Function {

	public Object call(Object[] arg0, Context arg1) {
		return UserKit.getUser();
	}
}