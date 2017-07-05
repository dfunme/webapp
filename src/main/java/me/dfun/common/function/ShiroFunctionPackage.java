package me.dfun.common.function;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import me.dfun.common.kit.UserKit;
import me.dfun.modules.user.model.User;

/**
 * 权限控制
 */
public class ShiroFunctionPackage {
	/**
	 * 是否访客
	 */
	public boolean isGuest() {
		return getSubject() == null || getSubject().getPrincipal() == null;
	}

	/**
	 * 是否用户
	 */
	public boolean isUser() {
		return getSubject() != null && getSubject().getPrincipal() != null;
	}

	/**
	 * 是否登录
	 */
	public boolean isAuthenticated() {
		return getSubject() != null && getSubject().isAuthenticated();
	}

	/**
	 * 是否未登陆
	 */
	public boolean isNotAuthenticated() {
		return !isAuthenticated();
	}

	/**
	 * 是否授权
	 */
	public String principal(Map<String, Object> map) {
		String strValue = null;
		if (getSubject() != null) {
			// Get the principal to print out
			Object principal;
			String type = map != null ? (String) map.get("type") : null;
			if (type == null) {
				principal = getSubject().getPrincipal();
			} else {
				principal = getPrincipalFromClassName(type);
			}
			String property = map != null ? (String) map.get("property") : null;
			// Get the string value of the principal
			if (principal != null) {
				if (property == null) {
					strValue = principal.toString();
				} else {
					strValue = getPrincipalProperty(principal, property);
				}
			}
		}

		if (strValue != null) {
			return strValue;
		} else {
			return null;
		}
	}

	/**
	 * 是否有指定角色
	 */
	public boolean hasRole(String roleName) {
		User user = UserKit.getUser();
		return user != null && user.hasRole(roleName);
	}

	/**
	 * 是否没有指定角色
	 */
	public boolean lacksRole(String roleName) {
		boolean hasRole = getSubject() != null && getSubject().hasRole(roleName);
		return !hasRole;
	}

	/**
	 * 是否有指定一组角色
	 */
	public boolean hasAnyRole(String roleNames) {
		boolean hasAnyRole = false;
		Subject subject = getSubject();
		if (subject != null) {
			// Iterate through roles and check to see if the user has one of the
			// roles
			for (String role : roleNames.split(",")) {
				if (subject.hasRole(role.trim())) {
					hasAnyRole = true;
					break;
				}
			}
		}
		return hasAnyRole;
	}

	/**
	 * 是否有权限
	 */
	public boolean hasPermission(String p) {
		return getSubject() != null && getSubject().isPermitted(p);
	}

	/**
	 * 是否没有权限
	 */
	public boolean lacksPermission(String p) {
		return !hasPermission(p);
	}

	/**
	 * 是否有权限
	 */
	public boolean hasPermissions(String... array) {
		boolean result = false;
		for (String p : array) {
			if (getSubject() != null && getSubject().isPermitted(p)) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * 获取认证对象
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object getPrincipalFromClassName(String type) {
		Object principal = null;
		try {
			Class cls = Class.forName(type);
			principal = getSubject().getPrincipals().oneByType(cls);
		} catch (ClassNotFoundException e) {
		}
		return principal;
	}

	/**
	 * 获取认证配置
	 */
	private String getPrincipalProperty(Object principal, String property) {
		String strValue = null;
		try {
			BeanInfo bi = Introspector.getBeanInfo(principal.getClass());
			// Loop through the properties to get the string value of the
			// specified property
			boolean foundProperty = false;
			for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
				if (pd.getName().equals(property)) {
					Object value = pd.getReadMethod().invoke(principal, (Object[]) null);
					strValue = String.valueOf(value);
					foundProperty = true;
					break;
				}
			}

			if (!foundProperty) {
				final String message = "Property [" + property + "] not found in principal of type ["
						+ principal.getClass().getName() + "]";

				throw new RuntimeException(message);
			}
		} catch (Exception e) {
			final String message = "Error reading property [" + property + "] from principal of type ["
					+ principal.getClass().getName() + "]";

			throw new RuntimeException(message, e);
		}
		return strValue;
	}

	/**
	 * 获取Subject
	 */
	protected Subject getSubject() {
		return SecurityUtils.getSubject();
	}
}