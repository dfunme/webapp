package me.dfun.common.interceptor;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import me.dfun.common.kit.DateKit;
import me.dfun.common.kit.QueryKit;
import me.dfun.common.kit.SecurityKit;
import me.dfun.common.kit.StringKit;
import me.dfun.common.kit.UserKit;
import me.dfun.modules.log.model.Log;
import me.dfun.modules.user.model.User;

/**
 * 日志拦截器
 */
public class LogInterceptor implements Interceptor {
	private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);

	private Set<String> methodSet = new HashSet<String>();

	public LogInterceptor(String... methods) {
		if (methods == null || methods.length == 0)
			throw new IllegalArgumentException("methods can not be null.");

		for (String method : methods)
			methodSet.add(method.trim());
	}

	public void intercept(Invocation inv) {
		Controller controller = (Controller) inv.getTarget();
		HttpServletRequest request = controller.getRequest();
		String uri = request.getRequestURI();
		String ex = null;
		boolean flag = true;
		try {
			// 获取缓存集合
			Map<String, Object> map = UserKit.getFlashMap();
			if (map != null && !map.isEmpty()) {
				for (Entry<String, Object> e : map.entrySet()) {
					controller.setAttr(e.getKey(), e.getValue());
				}
				// 清空缓存集合
				map.clear();
				// 不记录日志
				flag = false;
			}
			controller.setAttr("operate", inv.getMethodName());
			// 执行正常逻辑
			inv.invoke();
		} catch (Exception e) {
			// 拦截异常信息
			ex = e.toString();
			log.error(uri + " error: {}", e);
		} finally {
			// 记录操作日志
			User user = UserKit.getUser();
			if (user.get(QueryKit.KEY_ID) != null && flag && methodSet.contains(inv.getMethodName())) {
				String type = Log.TYPE_ACCESS;
				if ("login".equals(inv.getMethodName())) {
					type = Log.TYPE_LOGIN;
				}
				if (ex != null) {
					type = Log.TYPE_EXCEPTION;
				}
				StringBuilder params = new StringBuilder();
				int index = 0;
				for (Object param : request.getParameterMap().keySet()) {
					params.append((index++ == 0 ? "" : "&") + param + "=");
					params.append(StringKit.abbr(StringKit.endsWithIgnoreCase((String) param, "password") ? ""
							: request.getParameter((String) param), 100));
				}
				Log l = new Log();
				l.set("office_id", user.getLong(QueryKit.KEY_OFFICE_ID));
				l.set("create_by", user.getLong(QueryKit.KEY_ID));
				l.set("create_date", DateKit.toTimestamp(new Date()));
				l.set("type", type);
				l.set("user_ip", SecurityKit.getHost(request));
				l.set("user_agent", request.getHeader("user-agent"));
				l.set("request_uri", uri);
				l.set("method", request.getMethod());
				l.set("params", params.toString());
				l.set("exception", ex);
				try {
					l.save();
				} catch (Exception e) {
					log.error(uri + " error: {}", e.getMessage());
				}
			}
		}
	}
}