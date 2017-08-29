package me.dfun.modules.role.model;

import java.util.List;
import java.util.Map;

import me.dfun.common.annotation.Table;
import me.dfun.common.kit.ModelExt;
import me.dfun.common.kit.Response;

/**
 * 角色
 */
@Table(name = "sys_role")
public class Role extends ModelExt<Role> {
	private static final long serialVersionUID = 1L;
	public static final Role dao = new Role();

	/**
	 * 分页查询
	 */
	public Response paginate(Map<String, Object> map) {
		return paginate("role.paginate", map);
	}

	/**
	 * 列表查询
	 */
	public List<Role> findList(Map<String, Object> map) {
		return find(getSqlPara("role.list", map));
	}
}