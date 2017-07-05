package me.dfun.modules.log.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

import me.dfun.common.annotation.Table;
import me.dfun.common.kit.QueryKit;
import me.dfun.common.kit.UserKit;
import me.dfun.modules.office.model.Office;
import me.dfun.modules.user.model.User;

/**
 * 日志
 */
@Table(name = "sys_log")
public class Log extends Model<Log> {
	private static final long serialVersionUID = 1L;
	public static final Log dao = new Log();
	public static final String TYPE_LOGIN = "1";// 登陆日志
	public static final String TYPE_ACCESS = "2";// 操作日志
	public static final String TYPE_EXCEPTION = "3";// 异常日志

	/**
	 * 分页查询
	 */
	public Page<Log> paginate(Map<String, Object> map) {
		int pageNumber = QueryKit.getInt(map, "pageNumber", 1);
		int pageSize = QueryKit.getInt(map, "pageSize", 10);
		return paginate(pageNumber, pageSize, getSqlPara("", map));
	}

	/**
	 * 获取所属单位
	 */
	public Office getOffice() {
		return Office.dao.findFirstByCache(getLong(QueryKit.KEY_OFFICE_ID));
	}

	/**
	 * 查询创建者
	 */
	public User getCreateBy() {
		return User.dao.findFirstByCache(getLong(QueryKit.KEY_CREATE_BY));
	}

	/**
	 * 保存
	 */
	public boolean save() {
		removeNullValueAttrs();
		set(QueryKit.KEY_CREATE_BY, UserKit.getUser().getLong(QueryKit.KEY_ID));
		set(QueryKit.KEY_CREATE_DATE, new Timestamp(new Date().getTime()));
		return super.save();
	}
}