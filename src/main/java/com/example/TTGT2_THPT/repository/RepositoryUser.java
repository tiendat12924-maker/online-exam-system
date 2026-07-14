package com.example.TTGT2_THPT.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.TTGT2_THPT.entity.User;

@Repository
public interface RepositoryUser extends JpaRepository<User, Long> {

	 User findByEmail(String email);
}
