package com.taskManagement.Repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.UserPermission;

@Repository
public interface UserPermissionRepository extends JpaRepository<UserPermission, UUID> {

	Optional<UserPermission> findByUser_UserId(Long userId);

}
