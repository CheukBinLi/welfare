package com.welfare.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class TaskScheduling {

	private static TaskScheduling TaskScheduling = new TaskScheduling(30000);

	public static TaskScheduling newInstance() {
		return TaskScheduling;
	}

	BlockingDeque<Task> taskQueue = new LinkedBlockingDeque<Task>();
	BlockingDeque<Task> tempQueue = new LinkedBlockingDeque<Task>();

	public ExecutorService executorService = Executors.newCachedThreadPool();

	private volatile boolean suspend = false;
	private volatile long interval;

	public void addTask(Task task) throws InterruptedException {
		taskQueue.putLast(task);
	}

	public void reset() {
		suspend = true;
		executorService.shutdownNow();
		executorService = Executors.newCachedThreadPool();
		init(interval);
	}

	private void init(long interval) {
		suspend = false;
		executorService.execute(new worker());
		executorService.execute(new exchange(interval));
	}

	public TaskScheduling(long interval) {
		super();
		this.interval = interval;
		init(interval);
	}

	class worker implements Runnable {
		public void run() {
			Task task;
			long now;
			try {
				while (!suspend) {
					if (null != (task = taskQueue.pollFirst(10, TimeUnit.MILLISECONDS))) {
						now = System.currentTimeMillis();
						if (task.getExpireAt() <= System.currentTimeMillis()) {
							task.action();
							if (task.isRepeat()) {
								tempQueue.putLast(task.repeat(now));
							}
						}
						tempQueue.putLast(task);
					}
				}
			} catch (InterruptedException e) {
			}
		}
	}

	class exchange implements Runnable {
		private volatile long interval;

		public void run() {
			try {
				while (!suspend) {
					synchronized (this) {
						wait(this.interval);
					}
					synchronized (tempQueue) {
						taskQueue.addAll(tempQueue);
						tempQueue.clear();
					}
				}
			} catch (InterruptedException e) {
			}
		}

		public exchange(long interval) {
			super();
			this.interval = interval;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		TaskScheduling.newInstance().addTask(new Task(5, 0, false) {

			@Override
			public void action() {
				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			}
		});
	}

}
