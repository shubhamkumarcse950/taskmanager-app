package com.taskManagement.Entitys;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_permissions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPermission {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID permissionId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	@ElementCollection
	@CollectionTable(name = "permission_details_table")
	@MapKeyColumn(name = "permission_type")
	@Column(name = "permission_value")
	private Map<String, List<String>> permissions;

	private boolean isActive = true;
}
