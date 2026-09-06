package com.example.finance_manager.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.finance_manager.entity.User;



public interface UserRepository extends JpaRepository<User, Long> {

	
	Optional<User> findByEmail(String email);
}