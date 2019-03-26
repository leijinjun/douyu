package com.lei2j.douyu.thread.factory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory{

	private static ThreadGroup threadGroup;
	private AtomicInteger threadNumber = new AtomicInteger(1);

	private String namePattern;

	private Boolean daemon;

	private Integer priority;

	public DefaultThreadFactory() {
		SecurityManager securityManager = System.getSecurityManager();
		if (securityManager != null) {
			threadGroup = securityManager.getThreadGroup();
		} else {
			threadGroup = Thread.currentThread().getThreadGroup();
		}
	}

	public DefaultThreadFactory(String namePattern, boolean daemon, int priority) {
		this();
		this.namePattern = namePattern;
		this.daemon = daemon;
		this.priority = priority;
	}


	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(threadGroup,r,String.format(namePattern, threadNumber.getAndIncrement()));
		if (priority != null) {
			thread.setPriority(priority);
		}
		thread.setDaemon(daemon);
		return thread;
	}

	public String getNamePattern() {
		return namePattern;
	}

	public Boolean getDaemon() {
		return daemon;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setNamePattern(String namePattern) {
		this.namePattern = namePattern;
	}

	public void setDaemon(Boolean daemon) {
		this.daemon = daemon;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

}
