package com.Ambalika.AIMT.Dto;

import org.springframework.web.multipart.MultipartFile;

public class AdminInfoDto {

	private String name;
	private String userid;
	private String password;
	private MultipartFile profilepic;
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
	public MultipartFile getProfilepic() {
		return profilepic;
	}
	public void setProfilepic(MultipartFile profilepic) {
		this.profilepic = profilepic;
	}
	
}
