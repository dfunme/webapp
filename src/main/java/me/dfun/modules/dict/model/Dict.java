package me.dfun.modules.dict.model;

import me.dfun.common.annotation.Table;
import me.dfun.common.kit.ModelExt;

/**
 * 字典
 */
@Table(name = "sys_dict")
public class Dict extends ModelExt<Dict> {
	private static final long serialVersionUID = 1L;
	public static final Dict dao = new Dict();
}