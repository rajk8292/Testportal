package com.Ambalika.AIMT.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.Ambalika.AIMT.model.StudentInfo;

@Repository
public interface StudentInfoRepo extends JpaRepository<StudentInfo, Long> {

    // Fetch student by email
    StudentInfo findByEmail(String email);

    // Fetch students by status
    List<StudentInfo> findStudentsByStatus(@Param("status") String status);

    // Find students who do not have test results
    @Query("SELECT s FROM StudentInfo s WHERE s.testname NOT IN (SELECT r.testname FROM TestResult r)")
    List<StudentInfo> findStudentWithoutResult();

    // Search with optional filters
    @Query(value = "SELECT * FROM studentinfo " +
                   "WHERE (:testname IS NULL OR testname LIKE %:testname%) " +
                   "AND (:course IS NULL OR course LIKE %:course%) " +
                   "AND (:branch IS NULL OR branch LIKE %:branch%) " +
                   "AND (status = :status)", 
           nativeQuery = true)
    List<StudentInfo> findByDropDownSelect(@Param("testname") String testname, 
                                           @Param("course") String course, 
                                           @Param("branch") String branch, 
                                           @Param("status") String status);

    StudentInfo getByEmail(String email);

    // ✅ Corrected delete method
    void delete(StudentInfo stdinfo);

    // ✅ Optional: Delete by ID
    void deleteById(Long id);

    // ✅ Custom delete query
    @Transactional
    @Modifying
    @Query("DELETE FROM StudentInfo s WHERE s.id = :id")
    void deleteByCustomId(@Param("id") Long id);



}
