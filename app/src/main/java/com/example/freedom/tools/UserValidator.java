package com.example.freedom.tools;

public class UserValidator {
    public static ValidationResult validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            return new ValidationResult(false, "Username cannot be empty");
        }
        if (username.length() < 3) {
            return new ValidationResult(false, "Username should have at least 3 characters");
        }
        // Add more validation rules for username if needed
        return new ValidationResult(true, "");
    }

    public static ValidationResult validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return new ValidationResult(false, "Email cannot be empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new ValidationResult(false, "Invalid email format");
        }
        // Add more validation rules for email if needed
        return new ValidationResult(true, "");
    }

    public static ValidationResult validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            return new ValidationResult(false, "Password cannot be empty");
        }
        if (password.length() < 8) {
            return new ValidationResult(false, "Password should have at least 8 characters");
        }
        // Add more validation rules for password if needed
        return new ValidationResult(true, "");
    }

    public static ValidationResult validateConfirmPassword(String password, String confirmPassword) {
        if (confirmPassword == null || confirmPassword.isEmpty()) {
            return new ValidationResult(false, "Confirm password cannot be empty");
        }
        if (!password.equals(confirmPassword)) {
            return new ValidationResult(false, "Passwords do not match");
        }
        return new ValidationResult(true, "");
    }

    public static class ValidationResult {
        private final boolean isValid;
        private final String errorMessage;

        public ValidationResult(boolean isValid, String errorMessage) {
            this.isValid = isValid;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}

