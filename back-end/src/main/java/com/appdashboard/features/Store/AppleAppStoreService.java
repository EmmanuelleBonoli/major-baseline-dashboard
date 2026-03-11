package com.appdashboard.features.Store;

import com.appdashboard.features.AppStats.AppStats;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AppleAppStoreService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;
    @Value("${stores.apple.key-id}")
    private String keyId;

    @Value("${stores.apple.issuer-id}")
    private String issuerId;

    @Value("${stores.apple.private-key}")
    private String privateKeyContent;

    private static final String APP_STORE_CONNECT_API = "https://api.appstoreconnect.apple.com/v1";

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Génère un JWT pour l'authentification App Store Connect API
     */
    private String generateJWT() throws Exception {
        // Nettoyer et décoder la clé privée
        String privateKeyPEM = privateKeyContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setHeaderParam("kid", keyId)
                .setHeaderParam("typ", "JWT")
                .setIssuer(issuerId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1200000)) // 20 minutes
                .setAudience("appstoreconnect-v1")
                .signWith(
                        Keys.hmacShaKeyFor(secretKey.getBytes()),
                        SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Crée les headers HTTP avec le JWT
     */
    private HttpHeaders createHeaders() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + generateJWT());
        headers.set("Content-Type", "application/json");
        return headers;
    }

    /**
     * Récupère les statistiques pour une store iOS
     */
    public List<AppStats> fetchStats(Store store, LocalDate startDate, LocalDate endDate) {
        List<AppStats> statsList = new ArrayList<>();

        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Récupérer les métriques de ventes
            String salesUrl = String.format(
                    "%s/salesReports?filter[frequency]=DAILY&filter[reportDate]=%s&filter[vendorNumber]=%s",
                    APP_STORE_CONNECT_API,
                    startDate,
                    store.getAppStoreId());

            // Note: L'API App Store Connect nécessite une configuration spécifique
            // et retourne des données dans un format particulier
            // Ceci est un exemple simplifié

            log.info("Fetching stats for {} from {} to {}",
                    store.getBundleId(), startDate, endDate);

            // TODO: Implémenter la récupération réelle des données

        } catch (Exception e) {
            log.error("Error fetching App Store stats for {}: {}",
                    store.getBundleId(), e.getMessage());
        }

        return statsList;
    }

    /**
     * Récupère les données d'analyse (Analytics)
     */
    public List<AppStats> fetchAnalytics(Store store, LocalDate startDate, LocalDate endDate) {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Endpoint pour les analytics
            String analyticsUrl = String.format(
                    "%s/analyticsReportRequests",
                    APP_STORE_CONNECT_API);

            // TODO: Créer et soumettre une requête d'analytics

            log.info("Fetching analytics for {}", store.getBundleId());

        } catch (Exception e) {
            log.error("Error fetching analytics: {}", e.getMessage());
        }

        return new ArrayList<>();
    }
}
