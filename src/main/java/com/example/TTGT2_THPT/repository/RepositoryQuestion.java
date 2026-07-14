package com.example.TTGT2_THPT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TTGT2_THPT.entity.Questions;
import com.example.TTGT2_THPT.entity.Test;

@Repository
public interface RepositoryQuestion extends JpaRepository<Questions, Long> {
	 int countByTest(Test test);
}
