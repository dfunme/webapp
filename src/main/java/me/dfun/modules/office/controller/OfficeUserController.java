package me.dfun.modules.office.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import me.dfun.common.annotation.Route;
import me.dfun.common.kit.QueryKit;
import me.dfun.modules.office.model.Office;
import me.dfun.modules.user.model.User;
import me.dfun.modules.user.validator.UserValidator;

/**
 * 机构用户管理
 */
@Route(url = "/office/user", view = "/modules/office/user")
public class OfficeUserController extends Controller {

	/**
	 * 列表页面
	 */
	@RequiresPermissions("office:view")
	public void index() {
		keepPara();
		Long oid = getParaToLong(0);
		Map<String, Object> map = QueryKit.getParam(getRequest());
		map.put("office_id", oid);
		setAttr("page", User.dao.paginate(map));
		setAttr("office", Office.dao.findById(oid));
		render("list.html");
	}

	/**
	 * 新增页面
	 */
	@RequiresPermissions("office:edit")
	public void add() {
		Long oid = getParaToLong(0);
		setAttr("user", new User().set("office_id", oid));
		setAttr("office", Office.dao.findById(oid));
		setAttr("roleList", getRoleList(oid));
		render("form.html");
	}

	/**
	 * 保存操作
	 */
	@RequiresPermissions("office:edit")
	@Before(UserValidator.class)
	public void save() {
		getModel(User.class).save();
		redirect("/office/user/" + getPara("user.office_id"));
	}

	/**
	 * 编辑页面
	 */
	@RequiresPermissions("office:edit")
	public void edit() {
		User user = User.dao.findById(getParaToLong());
		setAttr("user", user);
		Long oid = user.getLong("office_id");
		setAttr("office", Office.dao.findById(oid));
		setAttr("roleList", getRoleList(oid));
		render("form.html");
	}

	/**
	 * 更新操作
	 */
	@RequiresPermissions("office:edit")
	@Before(UserValidator.class)
	public void update() {
		getModel(User.class).update();
		redirect("/office/user/" + getPara("user.office_id"));
	}

	/**
	 * 删除操作
	 */
	@RequiresPermissions("office:edit")
	public void delete() {
		User.dao.deleteById(getParaToLong(0));
		redirect("/office/user/" + getParaToLong(1));
	}

	/**
	 * 浏览页面
	 */
	@RequiresPermissions("office:view")
	public void view() {
		setAttr("user", User.dao.findById(getParaToLong()));
		render("form.html");
	}

	/**
	 * 获取角色列表
	 */
	private List<Record> getRoleList(Long officeId) {
		String condition = "";
		if (officeId != 1) {
			condition = " and id <> 1";
		}
		return Db.find("select * from sys_role where del_flag = 0" + condition);
	}
}