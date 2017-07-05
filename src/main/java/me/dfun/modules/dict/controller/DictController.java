package me.dfun.modules.dict.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;

import com.jfinal.core.Controller;

import me.dfun.common.annotation.Route;
import me.dfun.modules.dict.model.Dict;

/**
 * 字典管理
 */
@Route(url = "/dict", view = "/modules/dict")
public class DictController extends Controller {

	/**
	 * 列表页面
	 */
	@RequiresPermissions("dict:view")
	public void index() {
		setAttr("list", Dict.dao.getTree(0L));
		render("list.html");
	}

	/**
	 * 新增页面
	 */
	@RequiresPermissions("dict:edit")
	public void add() {
		setAttr("dict", new Dict().set("parent_id", getParaToLong(0, 1L)));
		render("form.html");
	}

	/**
	 * 保存操作
	 */
	@RequiresPermissions("dict:edit")
	public void save() throws Exception {
		getModel(Dict.class).save();
		redirect("/dict");
	}

	/**
	 * 编辑页面
	 */
	@RequiresPermissions("dict:edit")
	public void edit() {
		setAttr("dict", Dict.dao.findById(getParaToLong()));
		render("form.html");
	}

	/**
	 * 更新操作
	 */
	@RequiresPermissions("dict:edit")
	public void update() {
		getModel(Dict.class).update();
		redirect("/dict");
	}

	/**
	 * 删除操作
	 */
	@RequiresPermissions("dict:edit")
	public void delete() {
		Dict.dao.deleteById(getParaToLong());
		redirect("/dict");
	}

	/**
	 * 浏览页面
	 */
	@RequiresPermissions("dict:view")
	public void view() {
		setAttr("dict", Dict.dao.findById(getParaToLong()));
		render("form.html");
	}

	/**
	 * 选择树
	 */
	@RequiresUser
	public void tree() {
		renderJson(Dict.dao.getTree(getParaToLong("parentId", 0L)));
	}
}