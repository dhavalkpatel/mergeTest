package com.mach.valtech.rnrauth.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;

import com.mach.valtech.rnrauth.jpa.Customer;

public interface MachCustomerService {

    String login(String username, String password);
    Optional<User> findByToken(String token);
    Customer findById(Long id);
    public boolean validateToken(String token);

    }
