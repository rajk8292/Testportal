package com.Ambalika.AIMT.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "studentinfo", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class StudentInfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	////////////
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
//////////////
	@Column(nullable = false, length = 200)
	private String testname;
	
	@Column(nullable = false, length = 20)
	private String enrollmentno;
	
	@Column(nullable = false, length = 100)
	private String name;
	
	@Column(nullable = false, length = 13)
	private String contactno;
	
	@Column(length = 100, name = "email", unique = true, nullable = false)
	private String email;
	
	@Column(nullable = false, length = 50)
	private String password;

	
	@Column(nullable = false, length = 50)
	private String course;
	
	@Column(nullable = false, length = 50)
	private String branch;
	
	@Column(nullable = false, length = 30)
	private String year;
	
	@Column(nullable = false, length = 10)
	private String status;
	
	@Column(nullable = true, length = 1000)
	private String profilepic;
	
	@Column(nullable = false, length = 25)
	private String regdate;

	public String getEnrollmentno() {
		return enrollmentno;
	}

	public void setEnrollmentno(String enrollmentno) {
		this.enrollmentno = enrollmentno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactno() {
		return contactno;
	}

	public void setContactno(String contactno) {
		this.contactno = contactno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProfilepic() {
		return profilepic;
	}

	public void setProfilepic(String profilepic) {
		this.profilepic = profilepic;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public String getTestname() {
		return testname;
	}

	public void setTestname(String testname) {
		this.testname = testname;
	}

	

}
