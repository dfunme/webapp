package me.dfun.common.config;

import java.util.Locale;

import org.beetl.core.GroupTemplate;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.tx.TxByMethods;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;

import me.dfun.common.function.DictFunction;
import me.dfun.common.function.DictListFunction;
import me.dfun.common.function.I18NFunction;
import me.dfun.common.function.ShiroFunctionPackage;
import me.dfun.common.function.UserFunction;
import me.dfun.common.interceptor.LogInterceptor;
import me.dfun.common.interceptor.ShiroInterceptor;
import me.dfun.common.plugin.ModelPlugin;
import me.dfun.common.render.BeetlRenderFactory;

/**
 * JFinal引导式配置
 */
public class DefaultConfig extends JFinalConfig {
	/**
	 * 供Shiro插件使用。
	 */
	private Routes routes;

	/**
	 * 常量配置
	 */
	@Override
	public void configConstant(Constants me) {
		loadPropertyFile("config.properties");
		me.setDevMode(getPropertyToBoolean(Global.DEV_MODE, false));
		me.setI18nDefaultBaseName("i18n");
		me.setI18nDefaultLocale(Locale.SIMPLIFIED_CHINESE.toString());
		// Beetl
		me.setRenderFactory(new BeetlRenderFactory());
		GroupTemplate gt = BeetlRenderFactory.groupTemplate;
		// 设置i18n方法
		gt.registerFunction("i18n", new I18NFunction());
		// 设置字典方法
		gt.registerFunction("dict", new DictFunction());
		// 设置字典列表方法
		gt.registerFunction("dictList", new DictListFunction());
		// 设置用户方法
		gt.registerFunction("user", new UserFunction());
		// 设置用户权限
		gt.registerFunctionPackage("shiro", new ShiroFunctionPackage());
	}

	/**
	 * 路由配置
	 */
	@Override
	public void configRoute(Routes me) {
		routes = me;
		me.add(new DefaultRoutes(getProperty(Global.ROUTE_PATH, "me/dfun/modules")));
	}

	/**
	 * 模板配置
	 */
	@Override
	public void configEngine(Engine me) {
	}

	/**
	 * 插件配置
	 */
	@Override
	public void configPlugin(Plugins me) {
		// 配置C3p0数据库连接池连接属性
		String jdbcDriver = getProperty(Global.JDBC_DRIVER);
		String jdbcUrl = getProperty(Global.JDBC_URL);
		String username = getProperty(Global.JDBC_USERNAME);
		String password = getProperty(Global.JDBC_USERNAME);
		int initialSize = Integer.valueOf(prop.get(Global.POOL_INITIAL_SIZE));
		int minIdle = Integer.valueOf(prop.get(Global.POOL_MIN_IDLE));
		int maxActive = Integer.valueOf(prop.get(Global.POOL_MAX_ACTIVE));
		DruidPlugin druidPlugin = new DruidPlugin(jdbcUrl, username, password, jdbcDriver);
		druidPlugin.setInitialSize(initialSize);
		druidPlugin.setMinIdle(minIdle);
		druidPlugin.setMaxActive(maxActive);
		me.add(druidPlugin);
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin("mysql", druidPlugin);
		// 配置SQL模板管理
		arp.setBaseSqlTemplatePath(PathKit.getRootClassPath() + Global.SQL_PATH);
		arp.addSqlTemplate(Global.SQL_TEMPLATE);
		// 配置开发模式
		arp.setDevMode(getPropertyToBoolean(Global.DEV_MODE, false));
		// 是否显示SQL
		arp.setShowSql(getPropertyToBoolean(Global.SHOW_SQL, true));
		arp.setDialect(new MysqlDialect());
		// 配置属性名(字段名)大小写不敏感容器工厂
		// arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));
		// arp.setContainerFactory(new PropertyNameContainerFactory());
		// 配置model插件
		new ModelPlugin(arp, getProperty(Global.MODEL_PATH, "me/dfun/modules")).start();
		me.add(arp);

		// 配置ehcache缓存插件
		me.add(new EhCachePlugin());

		// 配置quartz插件
		// QuartzPlugin quartzPlugin = new QuartzPlugin("job.properties");
		// me.add(quartzPlugin);
	}

	/**
	 * 全局拦截器配置
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		// 配置shiro权限拦截器配置
		me.add(new ShiroInterceptor(this.routes));
		// 添加事物，对save、update和delete自动进行拦截
		me.add(new TxByMethods("save", "update", "delete"));
		// 记录日志，对login、save、update和delete自动进行拦截
		me.add(new LogInterceptor("login", "save", "update", "delete"));
	}

	/**
	 * 处理器配置
	 */
	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("ctx"));
	}
}