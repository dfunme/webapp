package me.dfun.modules.role.model;

import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Page;

import me.dfun.common.annotation.Table;
import me.dfun.common.kit.ModelExt;
import me.dfun.common.kit.QueryKit;

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
	public Page<Role> paginate(Map<String, Object> map) {
		int pageNumber = QueryKit.getInt(map, "pageNumber", 1);
		int pageSize = QueryKit.getInt(map, "pageSize", 10);
		return paginate(pageNumber, pageSize, getSqlPara("role.paginate", map));
	}

	/**
	 * 列表查询
	 */
	public List<Role> findList(Map<String, Object> map) {
		return find(getSqlPara("role.list", map));
	}
}