package com.Ambalika.AIMT.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "testinfo", uniqueConstraints = @UniqueConstraint(columnNames = "testname"))
public class Test {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(length = 500, nullable = false, unique = true, name = "testname")
	private String testname;
	
	@Column(length = 100, nullable = false)
	private String course;
	
	@Column(length = 150,nullable = false)
	private String branch;

	@Column(length = 60, nullable = false)
	private String year;
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}


	@Column(nullable = false)
	private int numberofquestions;
	
	@Column(nullable = false)
	private LocalDateTime starttime;
	
	@Column(nullable = false)
	private int endtime;
	
	@Column(nullable = false)
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

	public LocalDateTime getStarttime() {
		return starttime;
	}

	public void setStarttime(LocalDateTime starttime) {
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


	// Derived field: End time (computed as 24 hours after start time)
	public LocalDateTime getEndTime()
	{
			return starttime.plusHours(endtime);
	}
	
		
}
