package com.taskManagement.Controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.taskManagement.Entitys.RolePermission;
import com.taskManagement.Entitys.UserPermission;
import com.taskManagement.Service.UserPermissionService;
import com.taskManagement.ServiceImpl.UserPermissionServiceImpl;

@RestController
@RequestMapping("/api/permissions")
public class UserPermissionController {

	private final UserPermissionService userPermissionService;

	public UserPermissionController(UserPermissionServiceImpl userPermissionService) {
		this.userPermissionService = userPermissionService;
	}

	// ===================== USER PERMISSIONS =====================

	/** Save user-specific permissions */
	@PostMapping("/user")
	public ResponseEntity<UserPermission> saveUserPermission(@RequestBody UserPermission userPermission) {
		return ResponseEntity.ok(userPermissionService.saveUserPermission(userPermission));
	}

	/** Get all user-specific permissions */
	@GetMapping("/user")
	public ResponseEntity<List<UserPermission>> getAllUserPermissions() {
		return ResponseEntity.ok(userPermissionService.getAllUserPermissions());
	}

	/** Get single user-specific permission by ID */
	@GetMapping("/user/{permissionId}")
	public ResponseEntity<UserPermission> getUserPermissionById(@PathVariable UUID permissionId) {
		return ResponseEntity.ok(userPermissionService.getUserPermissionById(permissionId));
	}

	/** Delete user-specific permission by ID */
	@DeleteMapping("/user/{permissionId}")
	public ResponseEntity<String> deleteUserPermission(@PathVariable UUID permissionId) {
		return ResponseEntity.ok(userPermissionService.deleteUserPermission(permissionId));
	}

	/** Assign/Update permissions for a specific user */
	@PostMapping("/user/{userId}/assign")
	public ResponseEntity<UserPermission> assignUserSpecificPermission(@PathVariable Long userId,
			@RequestBody Map<String, List<String>> permissions) {
		return ResponseEntity.ok(userPermissionService.assignUserSpecificPermission(userId, permissions));
	}

	/** Remove user-specific permissions */
	@DeleteMapping("/user/{userId}/remove")
	public ResponseEntity<Object> removeUserSpecificPermission(@PathVariable Long userId) {
		return ResponseEntity.ok(userPermissionService.removeUserSpecificPermission(userId));
	}

	/** Get effective permissions for a user (merged user + role) */
	@GetMapping("/user/{userId}/effective")
	public ResponseEntity<Map<String, List<String>>> getEffectivePermissionsForUser(@PathVariable Long userId) {
		return ResponseEntity.ok(userPermissionService.getEffectivePermissionsForUser(userId));
	}

	// ===================== ROLE PERMISSIONS =====================

	/** Save role-based permissions */
	@PostMapping("/role")
	public ResponseEntity<RolePermission> saveRolePermission(@RequestBody RolePermission rolePermission) {
		return ResponseEntity.ok(userPermissionService.saveRolePermission(rolePermission));
	}

	/** Get permissions by role name */
	@GetMapping("/role/{roleName}")
	public ResponseEntity<Map<String, List<String>>> getPermissionsByRoleName(@PathVariable String roleName) {
		return ResponseEntity.ok(userPermissionService.getPermissionsByRoleName(roleName));
	}
}
