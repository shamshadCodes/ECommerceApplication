package com.example.UserManagementService.repository;

import com.example.UserManagementService.model.Session;
import com.example.UserManagementService.model.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    Set<Session> findAllByUserIdAndSessionStatus(UUID userId, SessionStatus sessionStatus);

    Optional<Session> findByTokenAndUserId(String token, UUID userID);
}