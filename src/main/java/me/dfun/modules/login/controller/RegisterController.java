package me.dfun.modules.login.controller;

import com.jfinal.core.Controller;

import me.dfun.common.annotation.Route;

/**
 * 注册控制器
 */
@Route(url = "/register")
public class RegisterController extends Controller {

	/**
	 * 首页
	 */
	public void index() {
		render("/register.html");
	}
}