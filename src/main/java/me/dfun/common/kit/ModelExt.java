package me.dfun.common.kit;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
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
	 * 获取缓存对象
	 */
	public M findFirstByCache(Long id) {
		List<Object> list = Lists.newArrayList();
		list.add(getTable());
		list.add(id);
		return findFirstByCache(getCacheKey(), id, getSql("common.findById"), list);
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
			List<Object> pl = Lists.newArrayList();
			pl.add(getTable());
			pl.add("%," + getLong(QueryKit.KEY_ID) + ",%");
			List<M> rl = find(getSql("common.findByParentIds"), pl);
			for (M m : rl) {
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
		return set(QueryKit.KEY_ID, id).set(QueryKit.KEY_DEL_FLAG, "1").update();
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
		return set(QueryKit.KEY_ID, id).set(QueryKit.KEY_DEL_FLAG, "0").update();
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
		List<Object> pl = Lists.newArrayList();
		String table = getTable();
		pl.add(table);
		pl.add(table);
		pl.add(parentId);
		return Db.find(getSql("common.tree"), pl);
	}
}