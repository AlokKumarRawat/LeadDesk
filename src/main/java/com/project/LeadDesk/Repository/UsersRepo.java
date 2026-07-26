package com.project.LeadDesk.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.LeadDesk.Model.Users;

public interface UsersRepo extends JpaRepository<Users, Long> {

	boolean existsByEmail(String email);

	Users findByEmail(String email);

}
