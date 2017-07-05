package me.dfun.modules.office.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

import me.dfun.common.annotation.Route;
import me.dfun.common.kit.QueryKit;
import me.dfun.modules.office.model.Office;
import me.dfun.modules.office.validator.OfficeValidator;
import me.dfun.modules.user.model.User;

/**
 * 单位管理
 */
@Route(url = "/office", view = "/modules/office")
public class OfficeController extends Controller {

	/**
	 * 列表页面
	 */
	@RequiresPermissions("office:view")
	public void index() {
		keepPara();
		setAttr("page", Office.dao.paginate(QueryKit.getParam(getRequest())));
		render("list.html");
	}

	/**
	 * 新增页面
	 */
	@RequiresPermissions("office:edit")
	public void add() {
		render("form.html");
	}

	/**
	 * 保存操作
	 */
	@RequiresPermissions("office:edit")
	@Before(OfficeValidator.class)
	public void save() {
		Office office = getModel(Office.class);
		office.save();
		User user = getModel(User.class);
		if (user.getStr("username") != null) {
			user.set("office_id", office.getLong("id"));
			user.set("role_id", 2);
			user.save();
		}
		redirect("/office");
	}

	/**
	 * 编辑页面
	 */
	@RequiresPermissions("office:edit")
	public void edit() {
		setAttr("office", Office.dao.findById(getParaToLong()));
		render("form.html");
	}

	/**
	 * 更新操作
	 */
	@RequiresPermissions("office:edit")
	@Before(OfficeValidator.class)
	public void update() {
		getModel(Office.class).update();
		redirect("/office");
	}

	/**
	 * 删除操作
	 */
	@RequiresPermissions("office:edit")
	public void delete() {
		Office.dao.deleteById(getParaToLong());
		redirect("/office");
	}

	/**
	 * 浏览页面
	 */
	@RequiresPermissions("office:view")
	public void view() {
		setAttr("office", Office.dao.findById(getParaToLong()));
		render("form.html");
	}

	/**
	 * 单位选择列表
	 */
	@RequiresUser
	public void datalist() {
		renderJson(Office.dao.paginate(QueryKit.getParam(getRequest())));
	}
}