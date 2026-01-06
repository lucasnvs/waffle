package com.lucasnvs.waffle.auth.domain;

import java.util.Optional;

/**
 * Interface for authentication services.
 * This abstraction allows for different authentication providers (Firebase, Auth0, custom, etc.)
 */
public interface AuthenticationProvider {

    /**
     * Get the currently authenticated user's ID
     * @return Optional containing the user ID if authenticated, empty otherwise
     */
    Optional<String> getCurrentUserId();

    /**
     * Get the currently authenticated user's ID
     * @return The user ID
     * @throws IllegalStateException if no user is authenticated
     */
    String getCurrentUserIdOrThrow();

    /**
     * Check if there is a currently authenticated user
     * @return true if a user is authenticated, false otherwise
     */
    boolean isAuthenticated();

    /**
     * Get the currently authenticated user's email (if available)
     * @return Optional containing the email if available, empty otherwise
     */
    Optional<String> getCurrentUserEmail();
}

