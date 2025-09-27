package com.taskManagement.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.taskManagement.Entitys.RolePermission;
import com.taskManagement.Entitys.UserPermission;

public interface UserPermissionService {

	List<UserPermission> getAllUserPermissions();

	UserPermission getUserPermissionById(UUID permissionId);

	String deleteUserPermission(UUID permissionId);

	UserPermission saveUserPermission(UserPermission userPermission);

	Map<String, List<String>> getPermissionsByRoleName(String roleName);

	Map<String, List<String>> getEffectivePermissionsForUser(Long userId);

	UserPermission assignUserSpecificPermission(Long userId, Map<String, List<String>> permissions);

	Object removeUserSpecificPermission(Long userId);

	RolePermission saveRolePermission(RolePermission rolePermission);

}
