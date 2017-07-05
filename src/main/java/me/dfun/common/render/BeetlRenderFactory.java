package me.dfun.common.render;

import java.io.IOException;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.resource.WebAppResourceLoader;
import org.beetl.ext.jfinal.BeetlRender;

import com.jfinal.kit.PathKit;
import com.jfinal.render.Render;
import com.jfinal.render.RenderFactory;

/**
 * Beetl渲染工厂
 */
public class BeetlRenderFactory extends RenderFactory {

	public static String viewExtension = ".html";
	public static GroupTemplate groupTemplate = null;

	public BeetlRenderFactory() {
		if (groupTemplate != null) {
			groupTemplate.close();
		}
		try {
			Configuration cfg = Configuration.defaultConfiguration();
			WebAppResourceLoader resourceLoader = new WebAppResourceLoader(PathKit.getWebRootPath());
			groupTemplate = new GroupTemplate(resourceLoader, cfg);
		} catch (IOException e) {
			throw new RuntimeException("加载GroupTemplate失败", e);
		}
	}

	public Render getRender(String view) {
		return new BeetlRender(groupTemplate, view);
	}

	public String getViewExtension() {
		return viewExtension;
	}
}