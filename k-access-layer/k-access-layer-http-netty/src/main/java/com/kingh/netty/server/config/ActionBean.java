package com.kingh.netty.server.config;

public class ActionBean {

	private String name;
	private String url;
	private String className;

	public ActionBean() {
		super();
	}

	public ActionBean(String name, String url, String className) {
		super();
		this.name = name;
		this.url = url;
		this.className = className;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "Action [name=" + name + ", url=" + url + ", className=" + className + "]";
	}

}
