package me.dfun.common.plugin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Model;

import me.dfun.common.annotation.Table;
import me.dfun.common.kit.ClassFinder;

/**
 * Model扫描注册
 */
public class ModelPlugin implements IPlugin {
	private static Logger logger = LoggerFactory.getLogger(ModelPlugin.class);

	private ActiveRecordPlugin arp;
	private boolean isStarted = false;
	private String scanPath = "";

	public ModelPlugin(ActiveRecordPlugin arp) {
		this.arp = arp;
	}

	public ModelPlugin(ActiveRecordPlugin arp, String scanPath) {
		this.arp = arp;
		this.scanPath = scanPath;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean start() {
		if (isStarted)
			return true;
		// 查找具有Table注解继承自Model的类
		List<Class<? extends Model>> list = ClassFinder.findAll(scanPath, Table.class, Model.class);
		for (Class<? extends Model> c : list) {
			// 获取注解对象
			Table annotation = (Table) c.getAnnotation(Table.class);
			logger.info("model: [{}] {}", annotation.name(), c.getName());
			// 注册映射
			arp.addMapping(annotation.name(), (Class<? extends Model<?>>) c);
		}
		return false;
	}

	public boolean stop() {
		isStarted = false;
		return true;
	}
}