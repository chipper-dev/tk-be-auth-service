package com.mitrais.chipper.temankondangan.be.authservice.service;

public interface AuthService {
    boolean login(String email, String password);
    boolean logout(Long userId);
}
