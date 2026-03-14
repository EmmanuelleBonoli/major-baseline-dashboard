package com.appdashboard.features.AppStats;

import com.appdashboard.features.Store.Store;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utilitaire centralisé pour générer des données de test (Mock).
 * Permet d'éviter de dupliquer la logique de génération aléatoire dans chaque service de store.
 */
public class StatsMockGenerator {
    private static final Random random = new Random();

    public static List<AppStats> generateStats(Store store, LocalDate startDate, LocalDate endDate) {
        List<AppStats> statsList = new ArrayList<>();
        
        startDate.datesUntil(endDate.plusDays(1)).forEach(date -> {
            // Downloads
            statsList.add(createStat(store, date, AppStats.MetricType.DOWNLOADS, (long) (random.nextInt(100) + 20)));
            
            // Active Users
            statsList.add(createStat(store, date, AppStats.MetricType.ACTIVE_USERS, (long) (random.nextInt(800) + 50)));
            
            // Revenue
            double revenue = 10 + (random.nextDouble() * 90);
            AppStats revenueStat = createStat(store, date, AppStats.MetricType.REVENUE, 0L);
            revenueStat.setRevenueAmount(revenue);
            revenueStat.setCurrency(store.getPlatform() == Store.Platform.ANDROID ? "USD" : "EUR");
            statsList.add(revenueStat);

            // Ratings (une fois par jour)
            statsList.add(createStat(store, date, AppStats.MetricType.RATINGS, (long) (random.nextInt(500)))); // en centièmes (0.00 à 5.00)

            // Crashes
            statsList.add(createStat(store, date, AppStats.MetricType.CRASHES, (long) (random.nextInt(10))));
        });
        
        return statsList;
    }

    private static AppStats createStat(Store store, LocalDate date, AppStats.MetricType type, Long value) {
        AppStats stat = new AppStats();
        stat.setStore(store);
        stat.setDate(date);
        stat.setMetricType(type);
        stat.setValue(value);
        return stat;
    }
}
