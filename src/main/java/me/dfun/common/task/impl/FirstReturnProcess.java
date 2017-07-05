package me.dfun.common.task.impl;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;

import me.dfun.common.task.ResultProcess;

/**
 * 优先返回处理
 */
public class FirstReturnProcess<T> implements ResultProcess<T> {

	public T process(int threads, CompletionService<T> cs) {
		T result = null;
		// 返回优先完成任务
		for (int i = 0; i < threads;) {
			try {
				result = cs.take().get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			break;
		}
		return result;
	}
}