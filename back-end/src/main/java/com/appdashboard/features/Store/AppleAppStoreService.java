package com.appdashboard.features.Store;

import com.appdashboard.features.AppStats.AppStats;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
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
        keyFactory.generatePrivate(keySpec); 

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
        log.info("Fetching stats for {} from {} to {}", store.getBundleId(), startDate, endDate);
        
        // Simulation pour l'instant : on redirige vers le générateur de mock centralisé
        // TODO: Implémenter le vrai appel vers App Store Connect API
        return com.appdashboard.features.AppStats.StatsMockGenerator.generateStats(store, startDate, endDate);
    }

    /**
     * Récupère les données d'analyse (Placeholder pour future intégration réelle)
     */
    public List<AppStats> fetchAnalytics(Store store, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching analytics for {} (Mock mode)", store.getBundleId());
        return java.util.Collections.emptyList();
    }
}
