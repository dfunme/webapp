package me.dfun.common.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.authz.aop.AuthenticatedAnnotationHandler;
import org.apache.shiro.authz.aop.GuestAnnotationHandler;
import org.apache.shiro.authz.aop.PermissionAnnotationHandler;
import org.apache.shiro.authz.aop.RoleAnnotationHandler;
import org.apache.shiro.authz.aop.UserAnnotationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.config.Routes;
import com.jfinal.config.Routes.Route;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

/**
 * Shiro拦截器
 */
public class ShiroInterceptor implements Interceptor {
	private static final Logger logger = LoggerFactory.getLogger(ShiroInterceptor.class);
	private static final String SLASH = "/";
	private String loginUrl = "/login";
	private String savedRequestUrl = "";
	private String unauthenticatedUrl = "/403";

	/**
	 * 权限注解
	 */
	@SuppressWarnings("unchecked")
	private static final Class<? extends Annotation>[] ANNOTATION_ARRAY = new Class[] { RequiresAuthentication.class,
			RequiresGuest.class, RequiresPermissions.class, RequiresRoles.class, RequiresUser.class };

	/**
	 * 注解集合
	 */
	ConcurrentMap<String, List<Annotation>> authMap = Maps.newConcurrentMap();

	public ShiroInterceptor(Routes routes) {
		scan(routes);
	}

	public ShiroInterceptor(Routes routes, String loginUrl, String savedRequestUrl, String unauthenticatedUrl) {
		this.loginUrl = loginUrl;
		this.savedRequestUrl = savedRequestUrl;
		this.unauthenticatedUrl = unauthenticatedUrl;
		scan(routes);
	}

	/**
	 * 扫描注册
	 */
	@SuppressWarnings("static-access")
	private boolean scan(Routes routes) {
		Set<String> excludeMethodName = excludeBaseMethod();
		for (Routes rs : routes.getRoutesList()) {
			for (Route r : rs.getRouteItemList()) {
				Class<? extends Controller> cc = r.getControllerClass();
				String controllerName = cc.getName();
				String controllerKey = r.getControllerKey();
				// 获取类注解。
				List<Annotation> cal = getAnnotationList(cc);
				for (Method method : cc.getMethods()) {
					// 排除Controller基类的所有方法，并且只关注没有参数的方法。
					if (!excludeMethodName.contains(method.getName()) && method.getParameterTypes().length == 0) {
						// 获取方法注解。
						String actionKey = getActionKey(controllerName, controllerKey, method);
						List<Annotation> mal = getAnnotationList(method);
						if (cal != null && !cal.isEmpty()) {
							mal.addAll(cal);
						}
						// 添加映射
						authMap.put(actionKey, mal);
						logger.info("annotation scan: {}", actionKey);
					}
				}
			}
		}
		return true;
	}

	/**
	 * 排除Controller基类方法
	 */
	private Set<String> excludeBaseMethod() {
		Set<String> set = Sets.newHashSet();
		Method[] methods = Controller.class.getMethods();
		for (Method m : methods) {
			if (m.getParameterTypes().length == 0) {
				set.add(m.getName());
			}
		}
		return set;
	}

	/**
	 * 获取类注解
	 */
	private List<Annotation> getAnnotationList(Class<? extends Controller> cc) {
		List<Annotation> al = Lists.newArrayList();
		for (Class<? extends Annotation> ac : ANNOTATION_ARRAY) {
			Annotation a = cc.getAnnotation(ac);
			if (a != null) {
				al.add(a);
			}
		}
		return al;
	}

	/**
	 * 获取方法注解
	 */
	private List<Annotation> getAnnotationList(Method m) {
		List<Annotation> al = Lists.newArrayList();
		for (Class<? extends Annotation> ac : ANNOTATION_ARRAY) {
			Annotation a = m.getAnnotation(ac);
			if (a != null) {
				al.add(a);
			}
		}
		return al;
	}

	/**
	 * 获取ActionKey
	 */
	private String getActionKey(String controllerName, String controllerKey, Method method) {
		ActionKey ak = method.getAnnotation(ActionKey.class);
		String methodName = method.getName();
		String actionKey = "";
		if (ak != null) {
			actionKey = ak.value().trim();
			if ("".equals(actionKey))
				throw new IllegalArgumentException(
						controllerName + "." + methodName + "(): The argument of ActionKey can not be blank.");

			if (!actionKey.startsWith(SLASH))
				actionKey = SLASH + actionKey;
		} else if (methodName.equals("index")) {
			actionKey = controllerKey;
		} else {
			actionKey = controllerKey.equals(SLASH) ? SLASH + methodName : controllerKey + SLASH + methodName;
		}
		return actionKey;
	}

	/**
	 * 校验权限
	 */
	public void intercept(Invocation inv) {
		// 根据actionKey获取注解
		String actionKey = inv.getActionKey();
		List<Annotation> al = authMap.get(actionKey);
		try {
			if (al != null && !al.isEmpty()) {
				for (Annotation a : al) {
					// 注解处理器判断注解
					if (a instanceof RequiresAuthentication) {
						new AuthenticatedAnnotationHandler().assertAuthorized(a);
					} else if (a instanceof RequiresGuest) {
						new GuestAnnotationHandler().assertAuthorized(a);
					} else if (a instanceof RequiresPermissions) {
						new PermissionAnnotationHandler().assertAuthorized(a);
					} else if (a instanceof RequiresRoles) {
						new RoleAnnotationHandler().assertAuthorized(a);
					} else if (a instanceof RequiresUser) {
						new UserAnnotationHandler().assertAuthorized(a);
					}
				}
			}
			logger.debug("authentication [{}] pass", actionKey);
		} catch (UnauthenticatedException uae) {
			// RequiresGuest，RequiresAuthentication，RequiresUser，未满足时，抛出未经授权的异常。
			// 如果没有进行身份验证，返回HTTP401状态码,或者跳转到默认登录页面
			if (StrKit.notBlank(loginUrl)) {
				// 保存登录前的页面信息,只保存GET请求。其他请求不处理。
				if (inv.getController().getRequest().getMethod().equalsIgnoreCase("GET")) {
					inv.getController().setSessionAttr(savedRequestUrl, actionKey);
				}
				inv.getController().redirect(loginUrl);
			} else {
				inv.getController().renderError(401);
			}
			logger.info("authentication [{}] 401", actionKey);
			return;
		} catch (AuthorizationException ae) {
			// RequiresRoles，RequiresPermissions授权异常
			// 如果没有权限访问对应的资源，返回HTTP状态码403，或者调转到为授权页面
			if (StrKit.notBlank(unauthenticatedUrl)) {
				inv.getController().redirect(unauthenticatedUrl);
			} else {
				inv.getController().renderError(403);
			}
			logger.info("authentication [{}] 403", actionKey);
			return;
		}
		// 执行正常逻辑
		inv.invoke();
	}
}