package me.dfun.modules.user.model;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;

import me.dfun.common.annotation.Table;
import me.dfun.common.kit.ModelExt;
import me.dfun.common.kit.QueryKit;
import me.dfun.common.kit.SecurityKit;
import me.dfun.modules.office.model.Office;
import me.dfun.modules.role.model.Role;

/**
 * 用户
 */
@Table(name = "sys_user")
public class User extends ModelExt<User> {
	private static final long serialVersionUID = 1L;
	public static final User dao = new User();
	public Role role = null;// 当前角色

	/**
	 * 分页查询
	 */
	public Page<User> paginate(Map<String, Object> map) {
		int pageNumber = QueryKit.getInt(map, "pageNumber", 1);
		int pageSize = QueryKit.getInt(map, "pageSize", 10);
		return paginate(pageNumber, pageSize, getSqlPara("user.paginate", map));
	}

	/**
	 * 保存用户
	 */
	public boolean save() {
		// 密码加密
		String password = getStr(QueryKit.KEY_PASSWORD);
		if (password != null) {
			try {
				set(QueryKit.KEY_PASSWORD, SecurityKit.entryptPassword(password));
			} catch (Exception e) {
			}
		}
		return super.save();
	}

	/**
	 * 更新用户
	 */
	public boolean update() {
		// 密码加密
		String password = getStr(QueryKit.KEY_PASSWORD);
		if (password != null) {
			try {
				set(QueryKit.KEY_PASSWORD, SecurityKit.entryptPassword(password));
			} catch (Exception e) {
			}
		}
		removeNullValueAttrs();
		boolean r = super.update();
		// 清空缓存
		CacheKit.remove(getCacheKey(), getLong(QueryKit.KEY_ID));
		return r;
	}

	/**
	 * 获取所属单位
	 */
	public Office getOffice() {
		return Office.dao.findFirstByCache(getLong(QueryKit.KEY_OFFICE_ID));
	}

	/**
	 * 获取用户角色
	 */
	public List<Role> getRoleList() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("user_id", getLong(QueryKit.KEY_ID));
		return Role.dao.findList(map);
	}

	/**
	 * 设置当前用户角色
	 */
	public void setRole(Long roleId) {
		for (Role r : getRoleList()) {
			if (r.getLong(QueryKit.KEY_ID).equals(roleId)) {
				role = r;
			}
		}
	}

	/**
	 * 获取用户角色
	 */
	public Role getRole() {
		if (role == null) {
			List<Role> rl = getRoleList();
			if (rl != null && !rl.isEmpty()) {
				role = rl.get(0);
			}
		}
		return role;
	}

	/**
	 * 判断用户是否拥有特定角色
	 */
	public boolean hasRole(String roleName) {
		long userId = getLong("id");
		String sql = "select r.* from sys_user u, sys_role r where u.role_id = r.id and u.id = " + userId
				+ " and r.name= ?";
		Role role = Role.dao.findFirst(sql, roleName);
		if (role != null)
			return true;
		else
			return false;
	}
}