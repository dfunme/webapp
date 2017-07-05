package me.dfun.common.kit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.kit.PropKit;

import me.dfun.common.config.Global;

/**
 * 文件工具类
 */
public class FileKit extends com.jfinal.kit.FileKit {
	/**
	 * 默认路径分隔符
	 */
	public final static String DEFAULT_PATH_SEPARATOR = "/";

	/**
	 * 文件下载
	 */
	public static void download(File file, String fileName, HttpServletResponse response) throws Exception {
		try {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		String type = getSuffix(fileName);
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
		response.setContentType(ContentTypeKit.getContentType(type));
		if (file.exists()) {
			OutputStream os = null;
			FileInputStream is = null;
			try {
				os = response.getOutputStream();
				is = new FileInputStream(file);
				byte[] buffer = new byte[1024];
				int i = 0;
				while ((i = is.read(buffer)) != -1) {
					os.write(buffer, 0, i);
				}
				os.flush();
			} catch (Exception e) {
				throw e;
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (Exception e) {
					}
				}
				if (is != null) {
					try {
						is.close();
					} catch (Exception e) {
					}
				}
			}
		}
	}

	/**
	 * 获取文件后缀
	 */
	public static String getSuffix(String name) {
		String suffix = "";
		if (StringUtils.isNotEmpty(name)) {
			suffix = name.substring(name.lastIndexOf(".") + 1);
		}
		return StringUtils.lowerCase(suffix);
	}

	/**
	 * 获取新文件名
	 */
	public static String getNewName(String suffix) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + suffix;
	}

	/**
	 * 获取文件存储根路径
	 */
	public static String getBasePath() {
		StringBuffer sb = new StringBuffer();
		String basePath = PropKit.use("config.properties").get(Global.FILE_PATH);
		if (basePath != null) {
			basePath = basePath.trim();
			sb.append(basePath);
		}
		if (!basePath.endsWith("/") && !basePath.endsWith("\\")) {
			sb.append(File.separator);
		}
		return sb.toString();
	}

	/**
	 * 获取文件夹路径
	 */
	public static String getFolderPath(String folder) {
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		sb.append(File.separator).append(folder);
		sb.append(File.separator).append(sdf.format(new Date()));
		return sb.toString();
	}

	/**
	 * 校验文件夹
	 */
	public static void checkFolder(String absolutePath) {
		if (StringUtils.isNotEmpty(absolutePath)) {
			File folder = new File(absolutePath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
		}
	}

	/**
	 * 校验文件
	 */
	public static File checkFile(String absolutePath) {
		if (StringUtils.isNotEmpty(absolutePath)) {
			File folder = new File(absolutePath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			return folder;
		}
		return null;
	}

	/**
	 * 删除文件
	 */
	public static void deleteFile(File file) {
		if (file != null && file.exists()) {
			file.delete();
		}
	}

	/**
	 * 显示文件大小
	 */
	public static String getFileSize(long size) {
		if (size < 0) {
			return "0";
		}
		DecimalFormat df = new DecimalFormat("#.00");
		String result = "";
		if (size < 1024) {
			result = df.format((double) size) + "B";
		} else if (size < 1048576) {
			result = df.format((double) size / 1024) + "KB";
		} else if (size < 1073741824) {
			result = df.format((double) size / 1048576) + "MB";
		} else {
			result = df.format((double) size / 1073741824) + "GB";
		}
		return result;
	}

	/**
	 * 读取文件
	 */
	public static String readFile(String Path) {
		BufferedReader reader = null;
		String laststr = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return laststr;
	}
}