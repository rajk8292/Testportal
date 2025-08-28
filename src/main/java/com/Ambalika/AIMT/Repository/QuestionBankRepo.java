package com.Ambalika.AIMT.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Ambalika.AIMT.model.QuestionBank;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Repository
public interface QuestionBankRepo extends JpaRepository<QuestionBank, Integer> {

    @SuppressWarnings("unchecked")
    default List<QuestionBank> findQuestionByTestname(String testname, int numberOfQuestion, EntityManager entityManager) {
        if (testname == null || testname.trim().isEmpty()) {
            System.out.println("❌ Test name is null or empty.");
            return List.of();
        }

        System.out.println("➡️ Fetching " + numberOfQuestion + " questions for test: " + testname);

        String sql = "SELECT * FROM questionbank WHERE LOWER(testname) = LOWER(:testname) ORDER BY RAND() LIMIT " + numberOfQuestion;
        Query query = entityManager.createNativeQuery(sql, QuestionBank.class);
        query.setParameter("testname", testname);

        List<QuestionBank> results = query.getResultList();

        System.out.println("✅ Questions fetched from DB: " + results.size());
        return results;
    }

    // Used elsewhere in the app
    List<QuestionBank> findByYear(String year);
    List<QuestionBank> findByBranch(String branch);
    List<QuestionBank> findByCourse(String course);
    

    boolean existsByTestname(String testname);

    List<QuestionBank> findQuestionByTestname(String testname); // This is not used in the random fetch
}
