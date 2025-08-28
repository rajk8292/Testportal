package com.Ambalika.AIMT.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Ambalika.AIMT.model.Courses;

@Repository
public interface CourseRepo extends JpaRepository<Courses, Long>{

	boolean existsByCoursename(String coursename);

	
}
