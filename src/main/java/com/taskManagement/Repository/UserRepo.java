package com.taskManagement.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taskManagement.Entitys.User;

public interface UserRepo extends JpaRepository<User, Long> {

	User findByEmail(String username);

	Optional<User> findByUserCode(String userCode);

	Optional<User> findByEmailOrUserId(String email, Long userId);

	void deleteByUserCode(String userCode);

	List<User> findByName(String name);

}
