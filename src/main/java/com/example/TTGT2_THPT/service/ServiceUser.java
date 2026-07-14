package com.example.TTGT2_THPT.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TTGT2_THPT.entity.User;
import com.example.TTGT2_THPT.repository.RepositoryUser;

@Service
public class ServiceUser {
	@Autowired
	RepositoryUser repoUser;
	public void save(User user) {
		repoUser.save(user);
	}

	
}
