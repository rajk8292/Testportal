package com.Ambalika.AIMT.Dto;

import java.time.LocalDateTime;

public class TestDto {
	
	private long id;
	
	private String testname;
	
	private String course;
	
	private String branch;

	private String year;
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	private int numberofquestions;
	
	private String starttime;
	
	private int endtime;
	
	private String teststatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTestname() {
		return testname;
	}

	public void setTestname(String testname) {
		this.testname = testname;
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

	public int getNumberofquestions() {
		return numberofquestions;
	}

	public void setNumberofquestions(int numberofquestions) {
		this.numberofquestions = numberofquestions;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public int getEndtime() {
		return endtime;
	}

	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}

	public String getTeststatus() {
		return teststatus;
	}

	public void setTeststatus(String teststatus) {
		this.teststatus = teststatus;
	}

}
