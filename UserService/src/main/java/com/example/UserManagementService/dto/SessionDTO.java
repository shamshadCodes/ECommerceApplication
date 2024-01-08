package com.example.UserManagementService.dto;

import com.example.UserManagementService.model.Session;
import com.example.UserManagementService.model.SessionStatus;
import com.example.UserManagementService.model.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SessionDTO {
    private String token;
    private Date issuedAt;
    private Date lastLoginAt;
    private Date expiringAt;
    private User user;
    private SessionStatus sessionStatus;

    public SessionDTO(Session session) {
        this.token = session.getToken();
        this.issuedAt = session.getIssuedAt();
        this.lastLoginAt = session.getLastLoginAt();
        this.expiringAt = session.getExpiringAt();
        this.user = session.getUser();
        this.sessionStatus = session.getSessionStatus();
    }
}
