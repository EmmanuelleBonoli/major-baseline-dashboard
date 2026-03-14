package com.appdashboard.features.Dashboard;

import com.appdashboard.features.Store.StoreDTO;
import com.appdashboard.features.AppStats.AppStats;
import com.appdashboard.features.AppStats.AppStatsService;
import com.appdashboard.features.Store.Store;
import com.appdashboard.features.Store.StoreService;
import com.appdashboard.features.Application.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StoreService storeService;
    private final ApplicationService applicationService;
    private final AppStatsService appStatsService;

    /**
     * Récupère le dashboard global (tous les jeux, toutes les stores)
     */
    public AppDashboardDTO getGlobalDashboard(LocalDate startDate, LocalDate endDate) {
        List<AppStats> allStats = appStatsService.getGlobalStatsByDateRange(startDate, endDate);
        
        // Calculer la période précédente pour les variations
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        List<AppStats> prevStats = appStatsService.getGlobalStatsByDateRange(startDate.minusDays(days), startDate.minusDays(1));

        // Calculer le résumé global avec variations
        AppDashboardDTO.Summary summary = calculateSummary(allStats, prevStats);

        // Préparer les données pour les graphiques (uniquement les essentiels pour le global)
        List<AppDashboardDTO.ChartData> charts = prepareChartData(allStats, true);

        return new AppDashboardDTO(null, summary, charts, new ArrayList<>());
    }

    /**
     * Récupère le dashboard pour une store spécifique
     */
    public AppDashboardDTO getAppDashboard(UUID storeId, LocalDate startDate, LocalDate endDate) {
        Store store = storeService.getStoreById(storeId);
        List<AppStats> stats = appStatsService.fetchAndGetStatsForDateRange(storeId, startDate, endDate);
        
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        List<AppStats> prevStats = appStatsService.fetchAndGetStatsForDateRange(storeId, startDate.minusDays(days), startDate.minusDays(1));

        AppDashboardDTO.Summary summary = calculateSummary(stats, prevStats);
        List<AppDashboardDTO.ChartData> charts = prepareChartData(stats, false);

        double latestRating = getLatestRating(stats);
        AppDashboardDTO.StoreSummary storeSum = new AppDashboardDTO.StoreSummary(
                store.getId(), summary.totalDownloads(), summary.totalRevenue(), summary.currency(), latestRating);

        return new AppDashboardDTO(StoreDTO.fromEntityToDTO(store), summary, charts, List.of(storeSum));
    }

    /**
     * Récupère le dashboard agrégé pour une application entière (tous ses stores)
     */
    public AppDashboardDTO getApplicationDashboard(UUID applicationId, LocalDate startDate, LocalDate endDate) {
        com.appdashboard.features.Application.Application application = applicationService.getGameById(applicationId);
        
        List<AppStats> allStats = new ArrayList<>();
        List<AppStats> allPrevStats = new ArrayList<>();
        List<AppDashboardDTO.StoreSummary> storeSummaries = new ArrayList<>();
        
        long diffDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;

        for (Store store : application.getStores()) {
            List<AppStats> storeStats = appStatsService.fetchAndGetStatsForDateRange(store.getId(), startDate, endDate);
            List<AppStats> storePrevStats = appStatsService.fetchAndGetStatsForDateRange(store.getId(), startDate.minusDays(diffDays), startDate.minusDays(1));
            
            allStats.addAll(storeStats);
            allPrevStats.addAll(storePrevStats);
            
            AppDashboardDTO.Summary storeSum = calculateSummary(storeStats, storePrevStats);
            double latestRating = getLatestRating(storeStats);
            
            storeSummaries.add(new AppDashboardDTO.StoreSummary(
                    store.getId(), storeSum.totalDownloads(), storeSum.totalRevenue(), storeSum.currency(), latestRating));
        }

        AppDashboardDTO.Summary summary = calculateSummary(allStats, allPrevStats);
        List<AppDashboardDTO.ChartData> charts = prepareChartData(allStats, false);

        return new AppDashboardDTO(null, summary, charts, storeSummaries);
    }

    /**
     * Calcule le résumé des statistiques avec variations
     */
    private AppDashboardDTO.Summary calculateSummary(List<AppStats> current, List<AppStats> previous) {
        long currentDownloads = sumMetric(current, AppStats.MetricType.DOWNLOADS);
        long prevDownloads = sumMetric(previous, AppStats.MetricType.DOWNLOADS);
        
        long currentActiveUsers = (long) avgMetric(current, AppStats.MetricType.ACTIVE_USERS);
        long prevActiveUsers = (long) avgMetric(previous, AppStats.MetricType.ACTIVE_USERS);
        
        double currentRevenue = sumRevenue(current);
        double prevRevenue = sumRevenue(previous);

        long currentCrashes = sumMetric(current, AppStats.MetricType.CRASHES);
        long prevCrashes = sumMetric(previous, AppStats.MetricType.CRASHES);

        String currency = current.stream()
                .filter(s -> s.getMetricType() == AppStats.MetricType.REVENUE && s.getCurrency() != null)
                .map(AppStats::getCurrency)
                .findFirst()
                .orElse("USD");

        return new AppDashboardDTO.Summary(
                currentDownloads, calculateVariation(currentDownloads, prevDownloads),
                currentActiveUsers, calculateVariation(currentActiveUsers, prevActiveUsers),
                currentRevenue, calculateVariation(currentRevenue, prevRevenue),
                currency,
                currentCrashes, calculateVariation(currentCrashes, prevCrashes)
        );
    }

    private long sumMetric(List<AppStats> stats, AppStats.MetricType type) {
        return stats.stream()
                .filter(s -> s.getMetricType() == type)
                .mapToLong(AppStats::getValue)
                .sum();
    }

    private double avgMetric(List<AppStats> stats, AppStats.MetricType type) {
        return stats.stream()
                .filter(s -> s.getMetricType() == type)
                .mapToLong(AppStats::getValue)
                .average()
                .orElse(0);
    }

    private double sumRevenue(List<AppStats> stats) {
        return stats.stream()
                .filter(s -> s.getMetricType() == AppStats.MetricType.REVENUE)
                .mapToDouble(s -> s.getRevenueAmount() != null ? s.getRevenueAmount() : 0.0)
                .sum();
    }

    private double getLatestRating(List<AppStats> stats) {
        return stats.stream()
                .filter(s -> s.getMetricType() == AppStats.MetricType.RATINGS)
                .sorted((s1, s2) -> s2.getDate().compareTo(s1.getDate()))
                .mapToDouble(AppStats::getValue)
                .findFirst()
                .orElse(0.0);
    }

    private double calculateVariation(double current, double previous) {
        if (previous == 0) return current > 0 ? 100.0 : 0.0;
        return ((current - previous) / previous) * 100.0;
    }

    /**
     * Prépare les données pour les graphiques
     */
    private List<AppDashboardDTO.ChartData> prepareChartData(List<AppStats> stats, boolean onlyGlobal) {
        List<AppDashboardDTO.ChartData> charts = new ArrayList<>();

        charts.add(createChartData("Téléchargements", stats, AppStats.MetricType.DOWNLOADS));
        charts.add(createChartDataRevenue("Revenus", stats));
        charts.add(createChartData("Utilisateurs actifs", stats, AppStats.MetricType.ACTIVE_USERS));
        
        if (!onlyGlobal) {
            charts.add(createChartData("Crashes", stats, AppStats.MetricType.CRASHES));
        }

        return charts;
    }

    private AppDashboardDTO.ChartData createChartData(
            String label,
            List<AppStats> stats,
            AppStats.MetricType metricType) {
        List<AppDashboardDTO.DataPoint> dataPoints = stats.stream()
                .filter(s -> s.getMetricType() == metricType)
                .map(s -> new AppDashboardDTO.DataPoint(s.getDate(), s.getValue()))
                .collect(Collectors.toList());

        return new AppDashboardDTO.ChartData(label, dataPoints);
    }

    private AppDashboardDTO.ChartData createChartDataRevenue(String label, List<AppStats> stats) {
        List<AppDashboardDTO.DataPoint> dataPoints = stats.stream()
                .filter(s -> s.getMetricType() == AppStats.MetricType.REVENUE)
                .map(s -> new AppDashboardDTO.DataPoint(s.getDate(), s.getRevenueAmount()))
                .collect(Collectors.toList());

        return new AppDashboardDTO.ChartData(label, dataPoints);
    }
}
