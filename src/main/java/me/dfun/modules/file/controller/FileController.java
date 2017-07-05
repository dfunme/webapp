package me.dfun.modules.file.controller;

import org.apache.shiro.authz.annotation.RequiresUser;

import com.jfinal.core.Controller;

import me.dfun.common.annotation.Route;

/**
 * 文件管理
 */
@Route(url = "/file")
public class FileController extends Controller {
	@RequiresUser
	public void upload() throws Exception {

	}
}