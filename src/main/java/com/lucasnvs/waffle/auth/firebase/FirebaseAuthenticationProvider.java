package com.lucasnvs.waffle.auth.firebase;

import com.lucasnvs.waffle.auth.domain.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Firebase implementation of the authentication provider.
 * This class handles Firebase-specific authentication logic.
 */
@Component
public class FirebaseAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Optional<String> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
            authentication.getPrincipal() instanceof String) {
            return Optional.of((String) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    @Override
    public String getCurrentUserIdOrThrow() {
        return getCurrentUserId()
                .orElseThrow(() -> new IllegalStateException("No authenticated user found"));
    }

    @Override
    public boolean isAuthenticated() {
        return getCurrentUserId().isPresent();
    }

    @Override
    public Optional<String> getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getDetails() instanceof FirebaseUserDetails) {
            FirebaseUserDetails details = (FirebaseUserDetails) authentication.getDetails();
            return Optional.ofNullable(details.getEmail());
        }
        return Optional.empty();
    }
}

