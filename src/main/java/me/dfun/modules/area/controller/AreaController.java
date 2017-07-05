package me.dfun.modules.area.controller;

import com.jfinal.core.Controller;

import me.dfun.common.annotation.Route;
import me.dfun.modules.area.model.Area;

/**
 * 区域管理
 */
@Route(url = "/area", view = "/modules/area")
public class AreaController extends Controller {

	/**
	 * 列表页面
	 */
	public void index() {
		render("index.html");
	}

	/**
	 * 新增页面
	 */
	public void add() {
		Long parentId = getParaToLong();
		if (parentId != null) {
			setAttr("area", new Area().set("parent_id", parentId));
		}
		render("form.html");
	}

	/**
	 * 保存操作
	 */
	public void save() {
		getModel(Area.class).save();
		redirect("/area");
	}

	/**
	 * 编辑页面
	 */
	public void edit() {
		setAttr("area", Area.dao.findById(getParaToLong()));
		render("form.html");
	}

	/**
	 * 更新操作
	 */
	public void update() {
		getModel(Area.class).update();
		redirect("/area");
	}

	/**
	 * 删除操作
	 */
	public void delete() {
		Area.dao.deleteById(getParaToLong());
		redirect("/area");
	}

	/**
	 * 浏览页面
	 */
	public void view() {
		setAttr("area", Area.dao.findById(getParaToLong()));
		render("form.html");
	}

	/**
	 * 选择树
	 */
	public void tree() {
		renderJson(Area.dao.getTree(getParaToLong("parentId", 0L)));
	}
}