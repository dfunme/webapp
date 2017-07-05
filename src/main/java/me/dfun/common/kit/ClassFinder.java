package me.dfun.common.kit;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.jfinal.kit.PathKit;

/**
 * 查找Class工具
 */
public class ClassFinder {
	private static Logger logger = LoggerFactory.getLogger(ClassFinder.class);
	private static final String CLASS_ROOT_FOLDER = "/classes";
	private static final String JAR_ROOT_FOLDER = "/WEB-INF/lib";
	private static final String CLASS_SUFFIX = ".class";

	/**
	 * 查找指定路径下符合注解和父类的类
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<Class<? extends T>> findFiles(String classPath, Class<? extends Annotation> annotation,
			Class<T> clazz) {
		logger.debug("class finder from file: {}", classPath);
		List<Class<? extends T>> list = Lists.newArrayList();
		// 判断目录是否存在
		File baseDir = new File(classPath);
		if (baseDir.exists() && baseDir.isDirectory()) {
			for (File file : baseDir.listFiles()) {
				if (file.isDirectory()) {
					list.addAll(findFiles(file.getAbsolutePath(), annotation, clazz));
				} else {
					if (file.getName().endsWith(CLASS_SUFFIX)) {
						String temp = file.getAbsolutePath().replaceAll("\\\\", "/");
						String className = temp.substring(
								temp.indexOf(CLASS_ROOT_FOLDER) + CLASS_ROOT_FOLDER.length() + 1,
								temp.indexOf(CLASS_SUFFIX));
						try {
							Class<?> c = Class.forName(className.replaceAll("/", "."));
							if (clazz.isAssignableFrom(c) && clazz != c && c.isAnnotationPresent(annotation)) {
								list.add((Class<? extends T>) c);
								logger.debug("class finder: {}", c.getName());
							}
						} catch (ClassNotFoundException e) {
							logger.error("findFiles error: {}", e.getMessage());
						}
					}
				}
			}
		} else {
			logger.error("findFiles: {} is not a dir", classPath);
		}
		return list;
	}

	/**
	 * 查找指定jar包下符合注解和父类的类
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<Class<? extends T>> findJars(String basePath, String classPath,
			Class<? extends Annotation> annotation, Class<T> clazz) {
		logger.debug("class finder from jar: {}", classPath);
		List<Class<? extends T>> list = Lists.newArrayList();
		// 判断目录是否存在
		File baseDir = new File(basePath);
		if (baseDir.exists() && baseDir.isDirectory()) {
			for (File file : baseDir.listFiles()) {
				try {
					JarFile jar = new JarFile(file);
					Enumeration<JarEntry> entries = jar.entries();
					while (entries.hasMoreElements()) {
						JarEntry jarEntry = entries.nextElement();
						String entryName = jarEntry.getName();
						if (jarEntry.isDirectory() || entryName.indexOf(classPath) == -1) {
							logger.debug("class finder exclude folder: {}", entryName);
							continue;
						} else if (entryName.endsWith(CLASS_SUFFIX)) {
							String className = entryName.replaceAll("/", ".").substring(0, entryName.length() - 6);
							try {
								Class<?> c = Class.forName(className.replaceAll("/", "."));
								if (clazz.isAssignableFrom(c) && clazz != c && c.isAnnotationPresent(annotation)) {
									list.add((Class<? extends T>) c);
									logger.debug("class finder: {}", c.getName());
								}
							} catch (ClassNotFoundException e) {
								logger.error("findJars error: {}", e.getMessage());
							}
						}
					}
					jar.close();
				} catch (IOException ex) {
					logger.error("findJars error: {}", ex.getMessage());
				}
			}
		} else {
			logger.error("findJars error: {} is not a dir", basePath);
		}
		return list;
	}

	/**
	 * 查找根路径下所有class
	 */
	public static <T> List<Class<? extends T>> findAll(Class<? extends Annotation> ac, Class<T> clazz) {
		return findAll("", ac, clazz);
	}

	/**
	 * 查找指定路径下所有class
	 */
	public static <T> List<Class<? extends T>> findAll(String basePath, Class<? extends Annotation> ac,
			Class<T> clazz) {
		List<Class<? extends T>> list = Lists.newArrayList();
		list.addAll(findFiles(PathKit.getRootClassPath() + File.separator + basePath, ac, clazz));
		list.addAll(findJars(PathKit.getWebRootPath() + JAR_ROOT_FOLDER, basePath, ac, clazz));
		return list;
	}
}