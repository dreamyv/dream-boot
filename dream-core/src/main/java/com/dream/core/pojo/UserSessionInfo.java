
package com.dream.core.pojo;


import com.dream.util.DateUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * session中存放的用户信息
 */
public class UserSessionInfo implements Serializable {

	private static final long serialVersionUID = 1568932092933110922L;
	public static final String SessionKey = "UserSessionInfo";
	private static final String __Admin = "admin";

	//最后一次登陆时间
	private Date lastLoginTime;
	//用户ID
	private long id;
	//用户名称
	private String name;
	//用户权限ID
	private Set<Long> authorities = new HashSet<>();

	private Long lenId;

	public UserSessionInfo() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Long> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Long> authorities) {
		this.authorities = authorities;
	}

	public boolean isHaveAuthority(BackendAuth BackendAuth) {
		return authorities.contains(BackendAuth.getValue());
	}

	public boolean hasAuth(String code) {
		long l = Long.parseLong(code);
		return authorities.contains(l);
	}

	public boolean isHaveAuthority(BackendAuth[] BackendAuth) {
		for (BackendAuth a : BackendAuth) {
			// 如果有一个不包含，直接返回false
			if (!authorities.contains(a.getValue())) {
				return false;
			}
		}
		// 如果都没有返回false，否则返回true
		return true;
	}

	public boolean isAdmin() {
		return __Admin.equals(this.name);
	}

	public Long getLenId() {
		return lenId;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public String getLastLoginTimeCn() {
		if (lastLoginTime == null) {
			return "首次登陆";
		}
		return DateUtil.getYYYY_DD_MM_SS(lastLoginTime);
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public void setLenId(Long lenId) {
		this.lenId = lenId;
	}

}
