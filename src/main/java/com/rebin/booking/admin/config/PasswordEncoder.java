package com.rebin.booking.admin.config;

public interface PasswordEncoder {
    String encode(String password);

    boolean matches(String password, String hashed);
}
