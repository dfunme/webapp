package me.dfun.modules.office.model;

import java.util.Map;

import com.jfinal.plugin.activerecord.Page;

import me.dfun.common.annotation.Table;
import me.dfun.common.kit.ModelExt;
import me.dfun.common.kit.QueryKit;
import me.dfun.modules.area.model.Area;

/**
 * 单位
 */
@Table(name = "sys_office")
public class Office extends ModelExt<Office> {
	private static final long serialVersionUID = 1L;
	public static final Office dao = new Office();

	/**
	 * 分页查询
	 */
	public Page<Office> paginate(Map<String, Object> map) {
		int pageNumber = QueryKit.getInt(map, "pageNumber", 1);
		int pageSize = QueryKit.getInt(map, "pageSize", 10);
		return paginate(pageNumber, pageSize, getSqlPara("office.paginate", map));
	}

	/**
	 * 获取地区
	 */
	public Area getArea() {
		return Area.dao.findFirstByCache(getLong(QueryKit.KEY_AREA_ID));
	}
}