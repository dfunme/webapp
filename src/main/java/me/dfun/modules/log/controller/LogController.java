package me.dfun.modules.log.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.jfinal.core.Controller;

import me.dfun.common.annotation.Route;
import me.dfun.common.kit.QueryKit;
import me.dfun.modules.log.model.Log;

/**
 * 日志管理
 */
@Route(url = "/log")
public class LogController extends Controller {

	@RequiresPermissions("log:view")
	public void index() {
		keepPara();
		setAttr("page", Log.dao.paginate(QueryKit.getParam(getRequest(), "type")));
		render("/modules/log/list.html");
	}

	@RequiresPermissions("log:edit")
	public void delete() {
		Log.dao.deleteById(getParaToInt());
		redirect("/log");
	}
}