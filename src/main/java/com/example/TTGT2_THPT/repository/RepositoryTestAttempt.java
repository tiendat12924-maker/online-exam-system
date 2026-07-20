package com.example.TTGT2_THPT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TTGT2_THPT.entity.TestAttempts;
import com.example.TTGT2_THPT.entity.User;
import java.util.List;

@Repository
public interface RepositoryTestAttempt extends JpaRepository<TestAttempts, Long> {
	List<TestAttempts> findByUserOrderBySubmittedAtDesc(User user);
}
