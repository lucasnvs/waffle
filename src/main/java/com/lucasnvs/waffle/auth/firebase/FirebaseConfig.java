package com.lucasnvs.waffle.auth.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * Configuration class for Firebase Admin SDK.
 * Initializes Firebase on application startup if the service account file is available.
 *
 * Firebase initialization is optional and will be skipped if:
 * - firebase.enabled property is set to false
 * - firebase-service-account.json file is not found
 * - firebase-service-account.json file is invalid
 */
@Configuration
@ConditionalOnProperty(
    name = "firebase.enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class FirebaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);
    private static final String SERVICE_ACCOUNT_FILE = "firebase-service-account.json";

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                ClassPathResource resource = new ClassPathResource(SERVICE_ACCOUNT_FILE);

                // Check if file exists
                if (!resource.exists()) {
                    logger.warn("Firebase service account file not found at: {}", SERVICE_ACCOUNT_FILE);
                    logger.warn("Firebase authentication is disabled. Set firebase.enabled=false in application.yml to suppress this warning.");
                    return;
                }

                try (InputStream serviceAccount = resource.getInputStream()) {
                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                            .build();

                    FirebaseApp.initializeApp(options);
                    logger.info("Firebase Admin SDK initialized successfully");
                }
            }
        } catch (IOException e) {
            logger.error("Failed to initialize Firebase Admin SDK. Check that {} is valid. Error: {}",
                    SERVICE_ACCOUNT_FILE, e.getMessage());
            logger.error("Firebase authentication is disabled. To enable, provide a valid {} file.",
                    SERVICE_ACCOUNT_FILE);
            logger.debug("Detailed error:", e);
            // Don't throw exception - allow application to run without Firebase
        } catch (Exception e) {
            logger.error("Unexpected error during Firebase initialization: {}", e.getMessage());
            logger.debug("Detailed error:", e);
            // Don't throw exception - allow application to run without Firebase
        }
    }
}

