package me.dfun.common.kit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.dfun.common.realm.LocalAuthorizingRealm;

/**
 * 系统安全认证工具类
 */
public class SecurityKit extends SecurityUtils {
	private static final Logger logger = LoggerFactory.getLogger(SecurityKit.class);

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	public static final String IPV4_LOCALHOST = "127.0.0.1";
	public static final String IPV6_LOCALHOST = "0:0:0:0:0:0:0:1";
	public static final long a1 = getIpNum("10.0.0.0");
	public static final long a2 = getIpNum("10.255.255.255");
	public static final long b1 = getIpNum("172.16.0.0");
	public static final long b2 = getIpNum("172.31.255.255");
	public static final long c1 = getIpNum("192.168.0.0");
	public static final long c2 = getIpNum("192.168.255.255");

	private final static String DES = "DES";
	private final static String DES_CBC = "DES/CBC/PKCS5Padding";

	/**
	 * 获取访问IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getHost(HttpServletRequest request) {
		String host = request.getHeader("x-forwarded-for");
		if (StringUtils.isEmpty(host) || "unknown".equalsIgnoreCase(host)) {
			host = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(host) || "unknown".equalsIgnoreCase(host)) {
			host = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isEmpty(host) || "unknown".equalsIgnoreCase(host)) {
			host = request.getRemoteAddr();
			if (IPV4_LOCALHOST.equals(host) || IPV6_LOCALHOST.equals(host)) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					logger.error("SecurityUtils.getHost is error.{}", e.getMessage());
				}
				host = inet.getHostAddress();
			}

		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (host != null && host.length() > 15) {
			if (host.indexOf(",") > 0) {
				host = host.substring(0, host.indexOf(","));
			}
		}
		return host;
	}

	/**
	 * 获取IP计算值
	 * 
	 * @param ipAddress
	 * @return
	 */
	private static long getIpNum(String ipAddress) {
		String[] ip = ipAddress.split("\\.");
		long a = Integer.parseInt(ip[0]);
		long b = Integer.parseInt(ip[1]);
		long c = Integer.parseInt(ip[2]);
		long d = Integer.parseInt(ip[3]);
		return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
	}

	/**
	 * 是否内网IP
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isInnerIP(String ip) {
		if (StringUtils.isBlank(ip)) {
			return false;
		}
		long n = getIpNum(ip);
		return (n >= a1 && n <= a2) || (n >= b1 && n <= b2) || (n >= c1 && n <= c2);
	}

	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 * 
	 * @throws Exception
	 */
	public static String entryptPassword(String plainPassword) throws Exception {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
	}

	/**
	 * 获取盐值
	 * 
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static byte[] getSalt(String password) throws Exception {
		if (password == null || password.length() < 16) {
			return new byte[] {};
		}
		return Encodes.decodeHex(password.substring(0, 16));
	}

	/**
	 * 获取加密密文
	 * 
	 * @param password
	 * @return
	 */
	public static String getHashPassword(String password) {
		if (password == null || password.length() < 16) {
			return "";
		}
		return password.substring(16);
	}

	/**
	 * 验证密码
	 * 
	 * @param plainPassword
	 *            明文密码
	 * @param password
	 *            密文密码
	 * @return 验证成功返回true
	 * @throws Exception
	 */
	public static boolean validatePassword(String plainPassword, String password) throws Exception {
		byte[] salt = getSalt(password);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
	}

	/**
	 * 加密键
	 */
	public static String encryptKey() {
		return DateKit.toStr(new Date(), "yyyyMMdd");
	}

	/**
	 * 根据键值进行加密
	 * 
	 * @param data明文
	 * @param key加密键
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) throws Exception {
		byte[] bt = encrypt(data.getBytes(), key.getBytes());
		return encodeHex(bt);
	}

	/**
	 * 根据键值进行解密
	 * 
	 * @param data密文
	 * @param key加密键
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) throws Exception {
		if (data == null) {
			return null;
		}
		byte[] buf = decodeHex(data);
		byte[] bt = decrypt(buf, key.getBytes());
		return new String(bt);
	}

	/**
	 * 根据键值进行加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey secretKey = keyFactory.generateSecret(dks);
		// 向量
		IvParameterSpec iv = new IvParameterSpec(key);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES_CBC);
		// 用密钥初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		return cipher.doFinal(data);

	}

	/**
	 * 根据键值进行解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		// 从原始密钥数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey secretKey = keyFactory.generateSecret(dks);
		// 向量
		IvParameterSpec iv = new IvParameterSpec(key);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES_CBC);
		// 用密钥初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		return cipher.doFinal(data);
	}

	/**
	 * 十六进制编码
	 * 
	 * @param input
	 * @return
	 */
	private static String encodeHex(byte[] input) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < input.length; i++) {
			String plainText = Integer.toHexString(0xff & input[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}
		return hexString.toString();
	}

	/**
	 * 十六进制解码
	 * 
	 * @param input
	 * @return
	 */
	private static byte[] decodeHex(String input) {
		byte digest[] = new byte[input.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String s = input.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(s, 16);
			digest[i] = (byte) byteValue;
		}
		return digest;
	}

	/**
	 * 清空缓存授权信息
	 */
	public static void clearCachedAuthorizationInfo() {
		RealmSecurityManager securityManager = (RealmSecurityManager) getSecurityManager();
		LocalAuthorizingRealm realm = (LocalAuthorizingRealm) securityManager.getRealms().iterator().next();
		realm.clearCachedAuthorizationInfoAll(getSubject().getPrincipals());
	}
}