package me.dfun.common.realm;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.jfinal.plugin.activerecord.Db;

import me.dfun.common.kit.QueryKit;
import me.dfun.common.kit.SecurityKit;
import me.dfun.modules.user.model.User;

/**
 * 本地安全认证实现类
 */
public class LocalAuthorizingRealm extends AuthorizingRealm {

	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

		String username = token.getUsername();
		if (username == null) {
			throw new AuthenticationException("Null usernames are not allowed by this realm.");
		}
		User user = User.dao.findFirst(Db.getSql("user.login"), username);
		String password = user.getStr(QueryKit.KEY_PASSWORD);
		try {
			byte[] salt = SecurityKit.getSalt(password);
			return new SimpleAuthenticationInfo(user, SecurityKit.getHashPassword(password),
					ByteSource.Util.bytes(salt), getName());
		} catch (Exception e) {
			throw new AuthenticationException(e);
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User user = (User) getAvailablePrincipal(principals);
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			String permission = user.getRole().getStr(QueryKit.KEY_PERMISSION);
			if (StringUtils.isNotBlank(permission)) {
				for (String p : permission.split(",")) {
					// 添加基于Permission的权限信息
					info.addStringPermission(p);
				}
			}
			return info;
		} else {
			return null;
		}
	}

	/**
	 * 清空缓存授权信息
	 */
	public void clearCachedAuthorizationInfoAll(PrincipalCollection principals) {
		this.clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 设定密码校验的Hash算法与迭代次数
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SecurityKit.HASH_ALGORITHM);
		matcher.setHashIterations(SecurityKit.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
	}

	/**
	 * 清空用户关联权限认证，待下次使用时重新加载
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清空所有关联认证
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}
}