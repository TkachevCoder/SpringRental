package com.example.rental.model.enums;


import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
        ADMIN,
        USER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name(); // Префикс "ROLE_" обязателен для Spring Security
    }
    }

