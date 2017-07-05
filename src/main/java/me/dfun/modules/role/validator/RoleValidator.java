package me.dfun.modules.role.validator;

import com.jfinal.core.Controller;

import me.dfun.common.interceptor.BaseValidator;
import me.dfun.modules.role.model.Role;

/**
 * 角色校验
 */
public class RoleValidator extends BaseValidator {

	@Override
	protected void validate(Controller c) {
		Long id = c.getParaToLong("role.id");
		validateExist(c, id, "name", "名称");
		// validateExist(c, id, "code", "代码");
	}

	/**
	 * 校验已存在
	 */
	private void validateExist(Controller c, Long id, String key, String comment) {
		String value = c.getPara("role." + key);
		StringBuffer sql = new StringBuffer();
		StringBuffer msg = new StringBuffer();
		sql.append("select * from sys_role where del_flag = 0 and ").append(key).append(" = '").append(value)
				.append("'");
		if (id == null) {
			msg.append("新增");
		} else {
			sql.append(" and id <> ").append(id);
			msg.append("修改");
		}
		msg.append("角色失败，").append(comment).append("[").append(value).append("]已存在！");
		validateExist(sql.toString(), msg.toString());
	}

	@Override
	protected void handleError(Controller c) {
		setFlash("role", c.getModel(Role.class));
		String actionKey = getActionKey();
		if (actionKey.equals("/role/save")) {
			c.redirect("/role/add");
		} else if (actionKey.equals("/role/update")) {
			c.redirect("/role/edit/" + c.getParaToLong("role.id"));
		}
	}
}