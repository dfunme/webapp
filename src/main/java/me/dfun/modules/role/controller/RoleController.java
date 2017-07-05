package me.dfun.modules.role.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;

import me.dfun.common.annotation.Route;
import me.dfun.common.kit.FileKit;
import me.dfun.common.kit.QueryKit;
import me.dfun.modules.role.model.Role;
import me.dfun.modules.role.validator.RoleValidator;

/**
 * 角色管理
 */
@Route(url = "/role", view = "/modules/role")
public class RoleController extends Controller {

	/**
	 * 列表页面
	 */
	@RequiresPermissions("role:view")
	public void index() {
		keepPara();
		setAttr("page", Role.dao.paginate(QueryKit.getParam(getRequest())));
		render("list.html");
	}

	/**
	 * 新增页面
	 */
	@RequiresPermissions("role:edit")
	public void add() {
		setAttr("menuList", getMenuList());
		render("form.html");
	}

	/**
	 * 保存操作
	 */
	@RequiresPermissions("role:edit")
	@Before(RoleValidator.class)
	public void save() {
		Role role = getModel(Role.class);
		role.set("permission", getParaConcat("permission"));
		role.save();
		redirect("/role");
	}

	/**
	 * 编辑页面
	 */
	@RequiresPermissions("role:edit")
	public void edit() {
		setAttr("role", Role.dao.findById(getParaToLong()));
		setAttr("menuList", getMenuList());
		render("form.html");
	}

	/**
	 * 更新操作
	 */
	@RequiresPermissions("role:edit")
	@Before(RoleValidator.class)
	public void update() {
		Role role = getModel(Role.class);
		role.set("permission", getParaConcat("permission"));
		role.update();
		redirect("/role");
	}

	/**
	 * 删除操作
	 */
	@RequiresPermissions("role:edit")
	public void delete() {
		Role.dao.deleteById(getParaToLong());
		redirect("/role");
	}

	/**
	 * 浏览页面
	 */
	@RequiresPermissions("role:view")
	public void view() {
		setAttr("role", Role.dao.findById(getParaToLong()));
		setAttr("menuList", getMenuList());
		render("form.html");
	}

	/**
	 * 获取菜单列表
	 */
	private List<Map<String, Object>> getMenuList() {
		List<Map<String, Object>> list = Lists.newArrayList();
		String permission = FileKit.readFile(PathKit.getRootClassPath() + "/permission.json");
		if (StringUtils.isNotEmpty(permission)) {
			JSONArray array = JSONArray.parseArray(permission);
			for (int i = 0; i < array.size(); i++) {
				JSONObject o = array.getJSONObject(i);
				addModule(list, o.getString("module"), o.getJSONArray("permissions").toArray());
			}
		}
		return list;
	}

	/**
	 * 添加模块
	 */
	private void addModule(List<Map<String, Object>> list, String module, Object[] permissions) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("module", module);
		map.put("permissions", permissions);
		list.add(map);
	}

	/**
	 * 拼接参数
	 */
	private String getParaConcat(String name) {
		StringBuffer sb = new StringBuffer();
		String[] array = getParaValues(name);
		if (array != null) {
			for (String s : array) {
				sb.append(s).append(",");
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		return sb.toString();
	}
}