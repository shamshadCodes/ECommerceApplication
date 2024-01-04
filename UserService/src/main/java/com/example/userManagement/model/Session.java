package com.example.userManagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Session extends BaseModel{
    private String token;
    private Date issuedAt;
    private Date lastLoginAt;
    private Date expiringAt;
    @ManyToOne
    private User user;

    //TODO:  Check how EnumType.ORDINAL works here once DB has some data
    @Enumerated(EnumType.STRING)
    private SessionStatus sessionStatus;
}
