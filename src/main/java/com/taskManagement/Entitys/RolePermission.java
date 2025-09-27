package com.taskManagement.Entitys;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role_permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolePermission {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID rolePermissionId;

	@Column(unique = true, nullable = false)
	private String roleName;

	@ElementCollection
	@CollectionTable(name = "role_permission_details")
	@MapKeyColumn(name = "permission_type")
	@Column(name = "permission_value")
	private Map<String, List<String>> permissions;

	private boolean isActive = true;
}
