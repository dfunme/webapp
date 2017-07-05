package me.dfun.modules.select.controller;

import com.jfinal.core.Controller;

import me.dfun.common.annotation.Route;

/**
 * 选择
 */
@Route(url = "/select", view = "/modules/select")
public class SelectController extends Controller {

	/**
	 * 树选择
	 */
	public void tree() {
		keepPara();
		render("tree.html");
	}

	/**
	 * 表格选择
	 */
	public void table() {
		keepPara();
		if (getAttr("columns") != null) {
			String[] columns = getPara("columns").split(",");
			setAttr("columns", columns);
		}
		render("table.html");
	}
}