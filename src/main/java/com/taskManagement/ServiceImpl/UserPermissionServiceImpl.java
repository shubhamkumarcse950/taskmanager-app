package com.taskManagement.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Service;

import com.taskManagement.Entitys.RolePermission;
import com.taskManagement.Entitys.User;
import com.taskManagement.Entitys.UserPermission;
import com.taskManagement.Repository.RolePermissionRepository;
import com.taskManagement.Repository.UserPermissionRepository;
import com.taskManagement.Repository.UserRepo;
import com.taskManagement.Service.UserPermissionService;
import com.taskManagement.exceptions.DataNotFoundException;
import com.taskManagement.exceptions.InvalidInputException;
import com.taskManagement.exceptions.UserNotFoundException;
import com.taskManagement.responsemodel.AppConstants;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserPermissionServiceImpl implements UserPermissionService {

	private final UserPermissionRepository userPermissionRepository;
	private final RolePermissionRepository rolePermissionRepository;
	private final UserRepo userRepository;

	public UserPermissionServiceImpl(UserPermissionRepository userPermissionRepository,
			RolePermissionRepository rolePermissionRepository, UserRepo userRepository) {
		this.userPermissionRepository = userPermissionRepository;
		this.rolePermissionRepository = rolePermissionRepository;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public UserPermission saveUserPermission(UserPermission userPermission) {
		try {
			if (userPermission.getUser().getUserId() == null) {
				throw new InvalidInputException("User ID is required for user-specific permissions");
			}
			return userPermissionRepository.saveAndFlush(userPermission);
		} catch (InvalidInputException | HibernateException e) {
			log.error(AppConstants.EXCEPTION_DEFOULT_MESSG_POST, e.getMessage());
			throw e;
		}
	}

	@Transactional
	@Override
	public RolePermission saveRolePermission(RolePermission rolePermission) {

		if (rolePermission.getRoleName() == null || rolePermission.getRoleName().trim().isEmpty()) {
			throw new InvalidInputException("Role name is required for role permissions");
		}
		return rolePermissionRepository.saveAndFlush(rolePermission);

	}

	@Override
	public List<UserPermission> getAllUserPermissions() {

		return this.userPermissionRepository.findAll();
	}

	@Override
	public UserPermission getUserPermissionById(UUID permissionId) {
		return userPermissionRepository.findById(permissionId)
				.orElseThrow(() -> new DataNotFoundException(AppConstants.INVALID_INPUT_ID));
	}

	@Override
	public String deleteUserPermission(UUID permissionId) {
		Optional<UserPermission> userPermission = userPermissionRepository.findById(permissionId);
		if (userPermission.isEmpty()) {
			throw new DataNotFoundException("UserPermission with the given ID not found");
		}
		userPermissionRepository.deleteById(permissionId);
		return AppConstants.DELETED_SUCCESFULLY;
	}

	@Override
	public Map<String, List<String>> getPermissionsByRoleName(String roleName) {
		RolePermission rolePermission = rolePermissionRepository.findByRoleName(roleName)
				.orElseThrow(() -> new DataNotFoundException(AppConstants.NO_DATA_FOUND));
		return rolePermission.getPermissions();
	}

	@Override
	public Map<String, List<String>> getEffectivePermissionsForUser(Long userId) {
		Optional<UserPermission> userPerm = userPermissionRepository.findByUser_UserId(userId);
		Map<String, List<String>> effectivePermissions = new HashMap<>();
		String roleName = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"))
				.getRoles().get(0);

		RolePermission rolePerm = rolePermissionRepository.findByRoleName(roleName)
				.orElseThrow(() -> new DataNotFoundException("Role permissions not found"));
		effectivePermissions.putAll(rolePerm.getPermissions());

		if (userPerm.isPresent() && !userPerm.get().getPermissions().isEmpty()) {
			Map<String, List<String>> userSpecific = userPerm.get().getPermissions();
			for (Map.Entry<String, List<String>> entry : userSpecific.entrySet()) {
				String key = entry.getKey();
				List<String> value = entry.getValue();
				if (effectivePermissions.containsKey(key)) {
					List<String> roleValues = effectivePermissions.get(key);
					for (String perm : value) {
						if (!roleValues.contains(perm)) {
							roleValues.add(perm);
						}
					}
				} else {
					effectivePermissions.put(key, value);
				}
			}
		}
		return effectivePermissions;
	}

	@Transactional
	@Override
	public UserPermission assignUserSpecificPermission(Long userId, Map<String, List<String>> permissions) {
		try {
			userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"));

			Optional<UserPermission> existingPerm = userPermissionRepository.findByUser_UserId(userId);
			UserPermission userPermission;

			if (existingPerm.isPresent()) {
				userPermission = existingPerm.get();
				userPermission.setPermissions(permissions);
			} else {
				userPermission = new UserPermission();
				User user = userRepository.findById(userId)
						.orElseThrow(() -> new UserNotFoundException("User not found with this userId" + userId));
				userPermission.setUser(user);
				userPermission.setPermissions(permissions);
			}

			return userPermissionRepository.saveAndFlush(userPermission);
		} catch (DataNotFoundException | HibernateException e) {
			log.error(AppConstants.EXCEPTION_DEFOULT_MESSG_POST, e.getMessage());
			return new UserPermission();
		}
	}

	@Transactional
	@Override
	public String removeUserSpecificPermission(Long userId) {
		Optional<UserPermission> userPermission = userPermissionRepository.findByUser_UserId(userId);
		if (userPermission.isEmpty()) {
			throw new DataNotFoundException("No user-specific permissions found for the given user");
		}
		userPermissionRepository.deleteById(userPermission.get().getPermissionId());
		return AppConstants.DELETED_SUCCESFULLY;
	}
}