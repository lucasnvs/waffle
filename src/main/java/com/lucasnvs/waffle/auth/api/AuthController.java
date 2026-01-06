package com.lucasnvs.waffle.auth.api;

import com.lucasnvs.waffle.auth.domain.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for authentication-related endpoints.
 * Provides endpoints to check authentication status and get current user info.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints para autenticação e informações do usuário")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/me")
    @Operation(summary = "Obter usuário atual", description = "Retorna informações do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<?> getCurrentUser() {
        String userId = authenticationService.getCurrentUserIdOrThrow();
        String email = authenticationService.getCurrentUserEmail().orElse(null);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("email", email);
        response.put("authenticated", true);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check")
    @Operation(summary = "Verificar autenticação", description = "Verifica se o usuário está autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status de autenticação")
    })
    public ResponseEntity<?> checkAuth() {
        boolean authenticated = authenticationService.isAuthenticated();
        String userId = authenticationService.getCurrentUserId().orElse(null);
        String email = authenticationService.getCurrentUserEmail().orElse(null);
        
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", authenticated);
        response.put("userId", userId);
        response.put("email", email);
        
        return ResponseEntity.ok(response);
    }
}

