package me.dfun.modules.login.validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;

import com.jfinal.core.Controller;
import com.jfinal.i18n.I18n;
import com.jfinal.validate.Validator;

import me.dfun.common.kit.SecurityKit;

/**
 * 登陆校验
 */
public class LoginValidator extends Validator {

	@Override
	protected void validate(Controller c) {
		c.keepPara();
		String username = c.getPara("username");
		String password = c.getPara("password");
		boolean rememberMe = c.getParaToBoolean("rememberMe", false);
		// 验证登陆
		if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
			int errorTimes = c.getCookieToInt("errorTimes", 0);// 错误次数
			boolean result = c.validateCaptcha("captcha");
			if (!result && errorTimes > 3) {
				addError("error", "验证码不正确");
			} else {
				String host = SecurityKit.getHost(c.getRequest());
				UsernamePasswordToken token = new UsernamePasswordToken(username, password.toCharArray(), rememberMe,
						host);
				try {
					SecurityKit.getSubject().login(token);
					c.removeCookie("errorTimes");
				} catch (Exception e) {
					String ex = e.getMessage();
					String error = "";
					if (ex.contains("Authentication failed for token submission")
							|| ex.contains("did not match the expected credentials.")) {
						error = I18n.use(c.getRequest().getLocale().toString()).get("error.authentication");
					} else {
						error = ex;
					}
					addError("error", error);
					c.setCookie("errorTimes", String.valueOf(++errorTimes), 30000, true);
					if (errorTimes > 2) {
						c.setAttr("needCaptcha", true);
					}
				}
			}
		}
	}

	@Override
	protected void handleError(Controller c) {
		// 返回登录页面
		c.render("/login.html");
	}
}