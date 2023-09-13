package com.ead.authuser.exceptions;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID value) {
        super(generateMessage(value));
    }

    private static String generateMessage(UUID value) {
        return "User not found with value: " + value;
    }
}
