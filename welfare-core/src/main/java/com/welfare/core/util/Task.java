package com.welfare.core.util;

public abstract class Task {

	private volatile long expireAt;// 运行时间
	private boolean isRepeat;// 是否重复运行
	private long interval;// 运行间隔

	public abstract void action();

	public Task repeat() {
		expireAt = System.currentTimeMillis() + interval;
		return this;
	}

	public Task repeat(long now) {
		expireAt = now + interval;
		return this;
	}

	public long getExpireAt() {
		return expireAt;
	}

	public boolean isRepeat() {
		return isRepeat;
	}

	public Task setRepeat(boolean isRepeat) {
		this.isRepeat = isRepeat;
		return this;
	}

	public long getInterval() {
		return interval;
	}

	public Task setInterval(long interval) {
		this.interval = interval;
		return this;
	}

	public Task setExpireAt(long expireAt) {
		this.expireAt = expireAt;
		return this;
	}

	public Task() {
		super();
	}

	public Task(long expireAt, boolean isRepeat, long interval) {
		super();
		this.expireAt = expireAt;
		this.isRepeat = isRepeat;
		this.interval = interval;
	}

	public Task(int second, long interval, boolean isRepeat) {
		super();
		this.expireAt = System.currentTimeMillis() + (second * 1000);
		this.isRepeat = isRepeat;
		this.interval = interval;
	}

}
