package com.example.TTGT2_THPT.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TTGT2_THPT.repository.RepositoryTest;

@Service
public class ServiceTest {
	@Autowired
    private RepositoryTest testRepository;

    public void deleteById(Integer id){
        testRepository.deleteById(id);
    }
}
