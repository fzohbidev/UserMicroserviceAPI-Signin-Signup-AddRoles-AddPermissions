package com.example.UserMicroserviceAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.UserMicroserviceAPI.model.UserAuthority;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
}
