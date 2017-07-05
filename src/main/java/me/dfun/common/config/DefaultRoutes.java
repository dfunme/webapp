package me.dfun.common.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.config.Routes;
import com.jfinal.core.Controller;

import me.dfun.common.kit.ClassFinder;

/**
 * 路由扫描注册
 */
public class DefaultRoutes extends Routes {
	private static Logger logger = LoggerFactory.getLogger(DefaultRoutes.class);

	private String scanPath = "";

	public DefaultRoutes() {
	}

	public DefaultRoutes(String scanPath) {
		this.scanPath = scanPath;
	}

	@Override
	public void config() {
		// 查找具有Route注解继承自Controller的类
		List<Class<? extends Controller>> list = ClassFinder.findAll(scanPath, me.dfun.common.annotation.Route.class,
				Controller.class);
		for (Class<? extends Controller> c : list) {
			// 获取注解对象
			me.dfun.common.annotation.Route annotation = (me.dfun.common.annotation.Route) c
					.getAnnotation(me.dfun.common.annotation.Route.class);
			logger.info("route: [{}] {}", annotation.url(), c.getName());
			// 注册映射
			add(annotation.url(), c, annotation.view());
		}
	}
}