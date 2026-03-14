package com.appdashboard.features.AppStats;

import com.appdashboard.features.Store.Store;
import com.appdashboard.features.Store.StoreService;
import com.appdashboard.features.Store.AppleAppStoreService;
import com.appdashboard.features.Store.GooglePlayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppStatsService {

    private final AppStatsRepository statsRepository;
    private final StoreService storeService;
    private final GooglePlayService googlePlayService;
    private final AppleAppStoreService appleAppStoreService;


    @Transactional
    public void syncAllActiveStores() {
        log.info("Lancement de la synchronisation globale...");
        List<Store> activeStores = storeService.getAllStoreEntities().stream()
                .filter(s -> s.isActive() && s.getApplication().isActive())
                .collect(Collectors.toList());

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);

        for (Store store : activeStores) {
            fetchAndSaveStats(store, startDate, endDate);
        }
        log.info("Synchronisation globale terminée ({} stores synchronisés)", activeStores.size());
    }


    public List<AppStats> getGlobalStatsByDateRange(LocalDate startDate, LocalDate endDate) {
        return statsRepository.findGlobalStatsByDateRange(startDate, endDate);
    }

    public List<AppStats> fetchAndGetStatsForDateRange(UUID storeId, LocalDate startDate, LocalDate endDate) {
        if (shouldFetchNewData(storeId, startDate, endDate)) {
            Store store = storeService.getStoreById(storeId);
            fetchAndSaveStats(store, startDate, endDate);
        }
        return statsRepository.findStatsByDateRange(storeId, startDate, endDate);
    }

    private boolean shouldFetchNewData(UUID storeId, LocalDate startDate, LocalDate endDate) {
        List<AppStats> existingStats = statsRepository.findStatsByDateRange(storeId, startDate, endDate);

        if (existingStats.isEmpty()) {
            log.info("Aucune donnée en cache pour la période demandée");
            return true;
        }

        LocalDate yesterday = LocalDate.now().minusDays(1);
        if (!endDate.isBefore(yesterday)) {
            LocalDate mostRecentDate = existingStats.stream()
                    .map(AppStats::getDate)
                    .max(LocalDate::compareTo)
                    .orElse(LocalDate.MIN);

            if (mostRecentDate.isBefore(yesterday)) {
                log.info("Les données sont obsolètes (dernière date: {})", mostRecentDate);
                return true;
            }
        }

        long requestedDays = startDate.datesUntil(endDate.plusDays(1)).count();
        long distinctDates = existingStats.stream()
                .map(AppStats::getDate)
                .distinct()
                .count();

        double coverage = (double) distinctDates / requestedDays;
        if (coverage < 0.8) {
            log.info("Couverture insuffisante: {}% ({}/{} jours)",
                    Math.round(coverage * 100), distinctDates, requestedDays);
            return true;
        }

        log.info("Données en cache suffisantes et récentes");
        return false;
    }

    @Transactional
    private void fetchAndSaveStats(Store store, LocalDate startDate, LocalDate endDate) {
        List<AppStats> newStats = new ArrayList<>();
        try {
            if (store.getPlatform() == Store.Platform.ANDROID) {
                newStats = googlePlayService.fetchStats(store, startDate, endDate);
            } else if (store.getPlatform() == Store.Platform.IOS) {
                newStats = appleAppStoreService.fetchStats(store, startDate, endDate);
            }

            for (AppStats stat : newStats) {
                statsRepository.findByStoreIdAndDateAndMetricType(
                        store.getId(), stat.getDate(), stat.getMetricType()).ifPresentOrElse(
                                existing -> {
                                    existing.setValue(stat.getValue());
                                    existing.setRevenueAmount(stat.getRevenueAmount());
                                    existing.setCurrency(stat.getCurrency());
                                    statsRepository.save(existing);
                                },
                                () -> statsRepository.save(stat));
            }

            log.info("Récupéré et sauvegardé {} stats pour {}", newStats.size(), store.getApplication().getName());

        } catch (Exception e) {
            log.error("Erreur lors du fetch des données pour {}: {}", store.getApplication().getName(), e.getMessage());
        }
    }
}
