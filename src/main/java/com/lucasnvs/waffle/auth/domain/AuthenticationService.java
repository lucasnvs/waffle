package com.lucasnvs.waffle.auth.domain;

import com.lucasnvs.waffle.auth.firebase.FirebaseUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for managing authentication operations.
 * Acts as a facade to the underlying authentication provider.
 */
@Service
public class AuthenticationService {

    private final AuthenticationProvider authenticationProvider;

    public AuthenticationService(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    /**
     * Get the currently authenticated user's ID
     * @return Optional containing the user ID if authenticated, empty otherwise
     */
    public Optional<String> getCurrentUserId() {
        return authenticationProvider.getCurrentUserId();
    }

    /**
     * Get the currently authenticated user's ID
     * @return The user ID
     * @throws IllegalStateException if no user is authenticated
     */
    public String getCurrentUserIdOrThrow() {
        return authenticationProvider.getCurrentUserIdOrThrow();
    }

    /**
     * Check if there is a currently authenticated user
     * @return true if a user is authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        return authenticationProvider.isAuthenticated();
    }

    /**
     * Get the currently authenticated user's email (if available)
     * @return Optional containing the email if available, empty otherwise
     */
    public Optional<String> getCurrentUserEmail() {
        return authenticationProvider.getCurrentUserEmail();
    }

    /**
     * Check if the current user is an admin
     * @return true if the current user has admin role, false otherwise
     */
    public boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof FirebaseUserDetails) {
            FirebaseUserDetails details = (FirebaseUserDetails) authentication.getDetails();
            return details.isAdmin();
        }
        return false;
    }
}

