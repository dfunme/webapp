package me.dfun.common.task.impl;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.dfun.common.task.ResultProcess;
import me.dfun.common.task.TaskService;

/**
 * 并行任务服务
 */
public class MutiTaskService<T> implements TaskService<T> {
	private int threads = 0;
	private ExecutorService es = null;
	private CompletionService<T> cs = null;
	private ResultProcess<T> rp = new FirstReturnProcess<T>();

	public MutiTaskService() {
		this.threads = Runtime.getRuntime().availableProcessors();
		this.es = Executors.newFixedThreadPool(threads);
		this.cs = new ExecutorCompletionService<T>(es);
	}

	public MutiTaskService(ResultProcess<T> rp) {
		this.threads = Runtime.getRuntime().availableProcessors();
		this.es = Executors.newFixedThreadPool(threads);
		this.cs = new ExecutorCompletionService<T>(es);
		this.rp = rp;
	}

	/**
	 * 执行任务
	 */
	public T execute(List<Callable<T>> list) {
		if (!es.isShutdown()) {
			// 分配任务
			for (Callable<T> c : list) {
				cs.submit(c);
			}
		}
		// 处理结果
		T result = rp.process(threads, cs);
		es.shutdownNow();
		return result;
	}
}