package me.dfun.common.kit;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import me.dfun.common.annotation.Table;

/**
 * 模型扩展类
 */
public class ModelExt<M extends ModelExt<?>> extends Model<M> {
	private static final long serialVersionUID = 1L;

	/**
	 * 获取表名
	 */
	public String getTable() {
		// 获取注解对象
		Table annotation = (Table) getClass().getAnnotation(Table.class);
		return annotation.name();
	}

	/**
	 * 分页查询
	 */
	public Response paginate(String sql, Map<String, Object> map) {
		int page = QueryKit.getInt(map, "page", 1);
		int limit = QueryKit.getInt(map, "limit", 30);
		Page<M> p = paginate(page, limit, getSqlPara(sql, map));
		Response r = new Response();
		if (p.getList() != null && !p.getList().isEmpty()) {
			r.setCount(p.getTotalRow());
			r.setData(p.getList());
		} else {
			r.setMsg("无数据");
		}
		return r;
	}

	/**
	 * 获取缓存对象
	 */
	public M findFirstByCache(Long id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(getTable()).append(" where id = ?");
		return findFirstByCache(getCacheKey(), id, sql.toString(), id);
	}

	/**
	 * 获取缓存标识
	 */
	public String getCacheKey() {
		return getTable() + "_cache";
	}

	/**
	 * 保存
	 */
	public boolean save() {
		preSave();
		setParent();
		return super.save();
	}

	/**
	 * 更新
	 */
	public boolean update() {
		setParent();
		preUpdate();
		if (get(QueryKit.KEY_PARENT_IDS) != null) {
			String oldParentIds = getStr(QueryKit.KEY_PARENT_IDS);
			// 更新子节点 parentIds
			Kv param = Kv.by("table", getTable()).set("parentIds", "%," + getLong(QueryKit.KEY_ID) + ",%");
			List<M> sl = find(getSqlPara("common.findByParentIds", param));
			for (M m : sl) {
				m.set(QueryKit.KEY_PARENT_IDS,
						m.getStr(QueryKit.KEY_PARENT_IDS).replace(oldParentIds, getStr(QueryKit.KEY_PARENT_IDS)));
				m.update();
			}
		}
		return super.update();
	}

	/**
	 * 逻辑删除
	 */
	public boolean deleteById(Long id) {
		return set(QueryKit.KEY_ID, id).set(QueryKit.KEY_STATUS, "0").update();
	}

	/**
	 * 批量逻辑删除
	 */
	public void deleteByIds(Long... ids) {
		for (Long id : ids) {
			deleteById(id);
		}
	}

	/**
	 * 逻辑恢复
	 */
	public boolean resumeById(Long id) {
		return set(QueryKit.KEY_ID, id).set(QueryKit.KEY_STATUS, "1").update();
	}

	/**
	 * 批量逻辑恢复
	 */
	public void resumeByIds(Long... ids) {
		for (Long id : ids) {
			resumeById(id);
		}
	}

	/**
	 * 保存前设置操作人信息
	 */
	public void preSave() {
		if (get(QueryKit.KEY_CREATE_BY) == null) {
			Long id = UserKit.getUser().getLong(QueryKit.KEY_ID);
			set(QueryKit.KEY_CREATE_BY, id);
			set(QueryKit.KEY_UPDATE_BY, id);
		}
		Date d = new Date();
		Timestamp ts = new Timestamp(d.getTime());
		set(QueryKit.KEY_CREATE_DATE, ts);
		set(QueryKit.KEY_UPDATE_DATE, ts);
	}

	/**
	 * 更新前设置操作人信息
	 */
	public void preUpdate() {
		if (get(QueryKit.KEY_UPDATE_BY) == null) {
			set(QueryKit.KEY_UPDATE_BY, UserKit.getUser().getLong(QueryKit.KEY_ID));
		}
		set(QueryKit.KEY_UPDATE_DATE, new Timestamp(new Date().getTime()));
	}

	/**
	 * 获取加密主键
	 */
	public String getSid() {
		String sid = null;
		try {
			sid = SecurityKit.encrypt(String.valueOf(getLong(QueryKit.KEY_ID)), UserKit.SECRET_KEY);
		} catch (Exception e) {
		}
		return sid;
	}

	/**
	 * 设置父级信息
	 */
	public void setParent() {
		M p = getParent();
		if (p != null) {
			set(QueryKit.KEY_PARENT_IDS, p.getStr(QueryKit.KEY_PARENT_IDS) + p.getLong(QueryKit.KEY_ID) + ",");
		}
	}

	/**
	 * 获取父级信息
	 */
	public M getParent() {
		Long parentId = getLong(QueryKit.KEY_PARENT_ID);
		return parentId != null ? findById(parentId) : null;
	}

	/**
	 * 获取树列表
	 */
	public List<Record> getTree(Long parentId) {
		Kv param = Kv.by("table", getTable()).set("parentId", parentId);
		return Db.find(getSqlPara("common.tree", param));
	}
}