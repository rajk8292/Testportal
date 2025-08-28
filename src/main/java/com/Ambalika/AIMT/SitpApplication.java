package com.Ambalika.AIMT;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.Ambalika.AIMT.Repository.TestRepo;
import com.Ambalika.AIMT.model.Test;

@SpringBootApplication
@EnableScheduling
public class SitpApplication {

	public static void main(String[] args) {
		SpringApplication.run(SitpApplication.class, args);
	}
		public CommandLineRunner initData(TestRepo repository) {
	        return args -> {
	            Test test = new Test();
	            test.setTestname("Python");
	            test.setCourse("B.Tech");
	            test.setBranch("Computer Science");
	            test.setNumberofquestions(2);
	            test.setStarttime(LocalDateTime.now());
	            test.setEndtime(2); // duration in hours
	            test.setTeststatus("Scheduled");

	            repository.save(test);
	            System.out.println("Inserted test: " + test.getTestname());
	        };
	}
}
