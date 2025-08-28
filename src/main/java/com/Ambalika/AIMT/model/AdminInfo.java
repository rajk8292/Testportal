package com.Ambalika.AIMT.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admininfo")
public class AdminInfo {
	
	@Column(nullable = false, length = 100)
	private String name;
	
	@Id
	@Column(length = 100)
	private String userid;
	
	@Column(nullable = false, length = 50)
	private String password;
	
	@Column(nullable = true, length = 1000)
	private String profilepic;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfilepic() {
		return profilepic;
	}

	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
	}

	
}
