package me.dfun.modules.office.validator;

import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import me.dfun.common.interceptor.BaseValidator;
import me.dfun.modules.office.model.Office;
import me.dfun.modules.user.model.User;

/**
 * 用户校验
 * 
 * @author justin
 * @version 1.0
 * @date 2016年1月21日
 */
public class OfficeValidator extends BaseValidator {

	@Override
	protected void validate(Controller c) {
		Long id = c.getParaToLong("office.id");
		String officeName = c.getPara("office.name");
		StringBuffer sql = new StringBuffer();
		sql.append("select * from sys_office where del_flag = 0 and name = '")
				.append(officeName).append("'");
		String msg = "新增";
		if (id != null) {
			sql.append(" and id <> ").append(id);
			msg = "修改";
		}
		List<Record> list = Db.find(sql.toString());
		if (list.size() > 0) {
			addError("error", msg + "单位失败，单位名称[" + officeName + "]已存在！");
		}
		if (id == null) {
			String username = c.getPara("user.username");
			List<Record> l = Db
					.find("select * from sys_user where del_flag = 0 and username = '"
							+ username + "'");
			if (l.size() > 0) {
				addError("error", "新增用户失败，帐号[" + username + "]已存在！");
			}
		}
	}

	@Override
	protected void handleError(Controller c) {
		setFlash("office", c.getModel(Office.class));
		setFlash("user", c.getModel(User.class));
		String actionKey = getActionKey();
		if (actionKey.equals("/office/save")) {
			c.redirect("/office/add");
		} else if (actionKey.equals("/office/update")) {
			c.redirect("/office/edit/" + c.getParaToLong("office.id"));
		}
	}
}