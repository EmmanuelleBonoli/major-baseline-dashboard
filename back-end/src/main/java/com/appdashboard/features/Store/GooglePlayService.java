package com.appdashboard.features.Store;

import com.appdashboard.features.AppStats.AppStats;
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
import java.util.ArrayList;
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
    public List<AppStats> fetchStats(Store store, LocalDate startDate, LocalDate endDate) {
        List<AppStats> statsList = new ArrayList<>();

        try {
            AndroidPublisher publisher = getAndroidPublisher();

            // Note: L'API Google Play ne fournit pas directement toutes les métriques
            // Il faut utiliser Google Play Console Reporting API ou Cloud Storage exports
            // Ceci est un exemple simplifié - vous devrez adapter selon vos besoins

            // Exemple pour récupérer les statistiques d'installations
            // var response = publisher.reviews()
            // .list(store.getPackageName())
            // .execute();

            log.info("Fetching stats for {} from {} to {}",
                    store.getPackageName(), startDate, endDate);

            // TODO: Implémenter la récupération réelle des données
            // Pour l'instant, retourne une liste vide

        } catch (Exception e) {
            log.error("Error fetching Google Play stats for {}: {}",
                    store.getPackageName(), e.getMessage());
        }

        return statsList;
    }

    /**
     * Récupère les notes et avis
     */
    public AppStats fetchRatings(Store store, LocalDate date) {
        try {
            AndroidPublisher publisher = getAndroidPublisher();

            // Récupérer les avis
            var reviews = publisher.reviews()
                    .list(store.getPackageName())
                    .execute();

            AppStats stats = new AppStats();
            stats.setStore(store);
            stats.setDate(date);
            stats.setMetricType(AppStats.MetricType.RATINGS);

            // Calculer la moyenne des notes
            if (reviews.getReviews() != null && !reviews.getReviews().isEmpty()) {
                double avgRating = reviews.getReviews().stream()
                        .mapToInt(r -> r.getComments().get(0).getUserComment().getStarRating())
                        .average()
                        .orElse(0.0);

                stats.setValue((long) (avgRating * 100)); // Stocké en centièmes
            }

            return stats;

        } catch (Exception e) {
            log.error("Error fetching ratings: {}", e.getMessage());
            return null;
        }
    }
}
