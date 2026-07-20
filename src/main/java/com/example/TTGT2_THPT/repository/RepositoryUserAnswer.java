package com.example.TTGT2_THPT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TTGT2_THPT.entity.UserAnswers;
import com.example.TTGT2_THPT.entity.TestAttempts;
import java.util.List;

@Repository
public interface RepositoryUserAnswer extends JpaRepository<UserAnswers, Long> {
	List<UserAnswers> findByAttempt(TestAttempts attempt);
}
