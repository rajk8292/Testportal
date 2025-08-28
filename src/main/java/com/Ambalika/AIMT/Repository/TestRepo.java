package com.Ambalika.AIMT.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Ambalika.AIMT.model.Test;

public interface TestRepo extends JpaRepository<Test, Long> {

    // ✅ Fetch all active or scheduled tests
    @Query("SELECT t FROM Test t WHERE t.teststatus = 'Active' OR t.teststatus = 'Scheduled'")
    List<Test> findByTeststatus();

    // ✅ Check if a test exists by name
    boolean existsByTestname(String testname);

    // ✅ Get test by exact testname (case sensitive)
    Test findByTestname(String testname);

    // ✅ Optional: Case-insensitive + trimmed match
    @Query("SELECT t FROM Test t WHERE LOWER(TRIM(t.testname)) = LOWER(TRIM(:testname))")
    Test findByTestnameIgnoreCase(@Param("testname") String testname);

    // ✅ Get all tests (Spring Data already has findAll() from JpaRepository, but you can keep this)
    @Query("SELECT t FROM Test t")
    List<Test> findTestname();
}
