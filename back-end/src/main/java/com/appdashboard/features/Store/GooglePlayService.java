package com.appdashboard.features.Store;

import com.appdashboard.features.AppStats.AppStats;
import com.appdashboard.features.AppStats.StatsMockGenerator;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GooglePlayService {

    @Value("${stores.google-play.service-account-json}")
    private String serviceAccountJson;

    private AndroidPublisher androidPublisher;

    /**
     * Initialise le client Google Play Developer API
     */
    private AndroidPublisher getAndroidPublisher() throws Exception {
        if (androidPublisher == null) {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new ByteArrayInputStream(serviceAccountJson.getBytes()))
                    .createScoped(Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER));

            androidPublisher = new AndroidPublisher.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials)).setApplicationName("App Stats Dashboard").build();
        }
        return androidPublisher;
    }

    /**
     * Récupère les statistiques pour une store Android
     */
    /**
     * Récupère les statistiques pour une store Android
     */
    public List<AppStats> fetchStats(Store store, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching stats for {} from {} to {}", store.getPackageName(), startDate, endDate);
        
        // Simulation pour l'instant : on redirige vers le générateur de mock centralisé
        // TODO: Implémenter le vrai appel vers Android Publisher API
        return StatsMockGenerator.generateStats(store, startDate, endDate);
    }

    /**
     * Récupère les notes et avis (Placeholder pour future intégration réelle)
     */
    public AppStats fetchRatings(Store store, LocalDate date) {
        log.info("Fetching ratings for {} (Mock mode)", store.getPackageName());
        return null;
    }
}
