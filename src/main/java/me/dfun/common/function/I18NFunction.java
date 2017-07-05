package me.dfun.common.function;

import javax.servlet.http.HttpServletRequest;

import org.beetl.core.Context;
import org.beetl.core.Function;

import com.jfinal.i18n.I18n;

/**
 * I18N国际化
 */
public class I18NFunction implements Function {

	public Object call(Object[] obj, Context context) {
		HttpServletRequest req = (HttpServletRequest) context.getGlobal("request");
		return I18n.use(req.getLocale().toString()).get((String) obj[0]);
	}
}