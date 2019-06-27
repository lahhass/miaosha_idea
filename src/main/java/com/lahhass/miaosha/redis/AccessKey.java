package com.lahhass.miaosha.redis;

public class AccessKey extends BasePrefix{

	private AccessKey(int expireSeconds, String prefix) {
		super(expireSeconds,prefix);
	}

	public static AccessKey withExpire(int expireSeconds) {
		return new AccessKey(expireSeconds, "access");
	}
	//限制5s之内访问5次
	//public static AccessKey access=new AccessKey(5,"access");
}
