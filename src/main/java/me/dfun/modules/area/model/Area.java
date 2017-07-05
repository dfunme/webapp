package me.dfun.modules.area.model;

import me.dfun.common.annotation.Table;
import me.dfun.common.kit.ModelExt;

/**
 * 区域
 */
@Table(name = "sys_area")
public class Area extends ModelExt<Area> {
	private static final long serialVersionUID = 1L;
	public static final Area dao = new Area();

	/**
	 * 获取全称
	 */
	public String getFullName() {
		StringBuffer sb = new StringBuffer();
		sb.append(getStr("name"));
		concatParentName(sb, this);
		return sb.toString();
	}

	/**
	 * 拼接上级区域名称
	 */
	private void concatParentName(StringBuffer sb, Area area) {
		Long pid = area.getLong("parent_id");
		if (pid != 0) {
			Area p = findFirstByCache(pid);
			if (p != null && p.getLong("id") != null) {
				sb.insert(0, p.getStr("name"));
				concatParentName(sb, p);
			}
		}
	}
}