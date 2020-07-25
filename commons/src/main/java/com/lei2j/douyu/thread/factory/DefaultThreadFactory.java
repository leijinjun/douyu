/*
* Copyright (c) [2020] [jinjun lei]
* [douyu danmu] is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*          http://license.coscl.org.cn/MulanPSL2
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
* See the Mulan PSL v2 for more details.
*/

package com.lei2j.douyu.thread.factory;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory{

	private static ThreadGroup threadGroup;
	private static AtomicInteger threadNumber = new AtomicInteger(1);

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
		Objects.requireNonNull(r, "r is null");
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
