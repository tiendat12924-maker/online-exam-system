package com.example.TTGT2_THPT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TTGT2_THPT.entity.UserAnswers;

@Repository
public interface RepositoryUserAnswer extends JpaRepository<UserAnswers, Long> {

}
