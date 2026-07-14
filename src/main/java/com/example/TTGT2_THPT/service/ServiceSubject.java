package com.example.TTGT2_THPT.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TTGT2_THPT.entity.Subjects;
import com.example.TTGT2_THPT.repository.RepositorySubject;

@Service
public class ServiceSubject {
	@Autowired
	RepositorySubject repoSubject;
	
	public List<Subjects> findAll() {
		return repoSubject.findAll();
	}

}
