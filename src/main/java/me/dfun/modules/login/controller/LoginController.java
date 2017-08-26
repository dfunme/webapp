package me.dfun.modules.login.controller;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import me.dfun.common.annotation.Route;
import me.dfun.common.kit.QueryKit;
import me.dfun.common.kit.SecurityKit;
import me.dfun.common.kit.UserKit;
import me.dfun.modules.login.validator.LoginValidator;
import me.dfun.modules.user.model.User;

/**
 * 登陆控制器
 * 
 * @author justin
 * @version 1.0
 * @date 2015年7月10日
 */
@Route
public class LoginController extends Controller {

	/**
	 * 首页
	 */
	@RequiresUser
	public void index() {
		render("/index.html");
	}

	/**
	 * 登录操作
	 */
	@Before(LoginValidator.class)
	public void login() {
		Subject subject = SecurityKit.getSubject();
		if (subject.isAuthenticated()) {
			// 已经认证，进入角色选择
			User u = UserKit.getUser();
			if (u.getRoleList().size() == 1) {
				redirect("/choose/" + u.getRoleList().get(0).getLong(QueryKit.KEY_ID));
			} else {
				setAttr("roleList", u.getRoleList());
				render("/choose.html");
			}
		} else {
			// 返回登录页面
			render("/login.html");
		}
	}

	/**
	 * 验证码
	 */
	public void captcha() {
		renderCaptcha();
	}

	/**
	 * 选择登陆角色
	 */
	public void choose() {
		UserKit.chooseRole(getParaToLong());
		redirect("/index");
	}

	/**
	 * 登出操作
	 */
	public void logout() {
		Subject subject = SecurityKit.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout();
		}
		redirect("/login");
	}
}