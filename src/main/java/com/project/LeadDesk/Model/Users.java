package com.project.LeadDesk.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Users {
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private long id;
		@Column(nullable = false)
		private String email;
		@Column(nullable = false)
		private String password;
		
		@Enumerated(EnumType.STRING)
		private userRole role;
		
		public enum userRole{
			admin,user
		}
		
		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public userRole getRole() {
			return role;
		}

		public void setRole(userRole role) {
			this.role = role;
		}
		
		
		
		
}
