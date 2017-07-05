package me.dfun.modules.user.validator;

import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import me.dfun.common.interceptor.BaseValidator;
import me.dfun.modules.user.model.User;

/**
 * 用户校验
 */
public class UserValidator extends BaseValidator {

	@Override
	protected void validate(Controller c) {
		Long id = c.getParaToLong("user.id");
		String username = c.getPara("user.username");
		String password = c.getPara("user.password");
		if (id == null && password == null) {
			addError("error", "新增用户密码不能为空！");
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select * from sys_user where del_flag = 0 and username = '").append(username).append("'");
		String msg = "新增";
		if (id != null) {
			sql.append(" and id <> ").append(id);
			msg = "修改";
		}
		msg = msg + "用户失败，帐号[" + username + "]已存在！";
		List<Record> userList = Db.find(sql.toString());
		if (userList.size() > 0) {
			addError("error", msg);
		}
	}

	@Override
	protected void handleError(Controller c) {
		setFlash("user", c.getModel(User.class));
		String actionKey = getActionKey();
		if ("/user/save".equals(actionKey)) {
			c.redirect("/user/add");
		} else if ("/user/update".equals(actionKey)) {
			c.redirect("/user/edit/" + c.getParaToLong("user.id"));
		} else if ("/office/user/save".equals(actionKey)) {
			c.redirect("/office/user/add/" + c.getParaToLong("user.office_id"));
		} else if ("/office/user/update".equals(actionKey)) {
			c.redirect("/office/user/edit/" + c.getParaToLong("user.id"));
		}
	}
}