package me.dfun.common.config;

/**
 * 全局配置
 */
public class Global {

	// JDBC
	public static final String JDBC_DRIVER = "jdbc.driver";
	public static final String JDBC_URL = "jdbc.url";
	public static final String JDBC_USERNAME = "jdbc.username";
	public static final String JDBC_PASSWORD = "jdbc.password";

	// POOL
	public static final String POOL_INITIAL_SIZE = "pool.initialsize";
	public static final String POOL_MIN_IDLE = "pool.minidle";
	public static final String POOL_MAX_ACTIVE = "pool.maxactive";

	// PATH
	public static final String ROUTE_PATH = "route.path";
	public static final String MODEL_PATH = "model.path";
	public static final String SQL_PATH = "/db";
	public static final String SQL_TEMPLATE = "all.sql";

	// DEVELOPER MODE
	public static final String DEV_MODE = "dev_mode";
	public static final String SHOW_SQL = "show_sql";
	public static final String FILE_PATH = "file_path";
	public static final String FILE_URL = "file_url";
	public static final String FILE_SIZE = "file_size";

	// 验证码
	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
	// 验证码错误
	public static final String CAPTCHA_ERROR = "captcha.error";
}