package com.taskManagement.Entitys;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name = "user_register")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long userId;
	private String userCode;
	private String name;

	private String email;
	private String contact;
	private String password;
	private List<String> roles;

	@JsonIgnore
	@CreationTimestamp
	@Column(name = "created_at", updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	private boolean isActive;

}
