package me.dfun.common.task;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 任务服务
 * 
 * @author justin
 * @version 1.0
 * @date 2016年8月25日
 */
public interface TaskService<T> {
	public T execute(List<Callable<T>> list);
}