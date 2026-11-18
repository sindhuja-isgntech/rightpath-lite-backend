package com.isigntech.rightpath.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="full_name" ,nullable = false)
	private String fullName;
	
	@Column(name = "email", unique = true)
	private String email;
	
	@Column(name = "mobile_number")
	private String mobileNumber;

	@Column(name = "password_hash", nullable = false)
    private String passwordHash;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum Role {
        CANDIDATE, ADMIN
    }
	
}
