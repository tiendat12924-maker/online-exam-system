package com.example.TTGT2_THPT.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TTGT2_THPT.entity.QuestionsGroup;
import com.example.TTGT2_THPT.entity.Subjects;

@Repository
public interface RepositoryQuestionGroup extends JpaRepository<QuestionsGroup, Integer> {

	List<QuestionsGroup> findByTestId(Integer id);

}
