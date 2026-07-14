package com.example.TTGT2_THPT.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.TTGT2_THPT.entity.Test;

public interface RepositoryTest extends JpaRepository<Test, Integer> {
	List<Test> findBySubjectId(Long subjectId);
}