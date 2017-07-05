package me.dfun.modules.user.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import me.dfun.common.annotation.Route;
import me.dfun.common.kit.QueryKit;
import me.dfun.modules.user.model.User;
import me.dfun.modules.user.validator.UserValidator;

/**
 * 用户管理
 */
@Route(url = "/user", view = "/modules/user")
public class UserController extends Controller {

	/**
	 * 列表页面
	 */
	@RequiresPermissions("user:view")
	public void index() {
		keepPara();
		setAttr("page", User.dao.paginate(QueryKit.getParam(getRequest())));
		render("list.html");
	}

	/**
	 * 新增页面
	 */
	@RequiresPermissions("user:edit")
	public void add() {
		render("form.html");
	}

	/**
	 * 保存操作
	 */
	@RequiresPermissions("user:edit")
	@Before(UserValidator.class)
	public void save() {
		getModel(User.class).save();
		redirect("/user");
	}

	/**
	 * 编辑页面
	 */
	@RequiresPermissions("user:edit")
	public void edit() {
		setAttr("user", User.dao.findById(getParaToLong()));
		render("form.html");
	}

	/**
	 * 更新操作
	 */
	@RequiresPermissions("user:edit")
	@Before(UserValidator.class)
	public void update() {
		getModel(User.class).update();
		redirect("/user");
	}

	/**
	 * 删除操作
	 */
	@RequiresPermissions("user:edit")
	public void delete() {
		User.dao.deleteById(getParaToLong());
		redirect("/user");
	}

	/**
	 * 浏览页面
	 */
	@RequiresPermissions("user:view")
	public void view() {
		setAttr("user", User.dao.findById(getParaToLong()));
		render("form.html");
	}
}