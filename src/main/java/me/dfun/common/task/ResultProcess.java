package me.dfun.common.task;

import java.util.concurrent.CompletionService;

/**
 * 结果处理
 */
public interface ResultProcess<T> {
	public T process(int threads, CompletionService<T> cs);
}