package com.appdashboard.features.Dashboard;

import com.appdashboard.features.Store.StoreDTO;
import com.appdashboard.features.AppStats.AppStats;
import com.appdashboard.features.AppStats.AppStatsService;
import com.appdashboard.features.Store.Store;
import com.appdashboard.features.Store.StoreService;
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
    private final AppStatsService appStatsService;

    /**
     * Récupère le dashboard pour une store spécifique
     */
    public AppDashboardDTO getAppDashboard(UUID storeId, LocalDate startDate, LocalDate endDate) {
        Store store = storeService.getStoreById(storeId);

        // Récupérer les stats (gère le check et fetch si besoin via le AppStatsService)
        List<AppStats> stats = appStatsService.fetchAndGetStatsForDateRange(storeId, startDate, endDate);

        // Calculer le résumé
        AppDashboardDTO.Summary summary = calculateSummary(stats);

        // Préparer les données pour les graphiques
        List<AppDashboardDTO.ChartData> charts = prepareChartData(stats);

        return new AppDashboardDTO(StoreDTO.fromEntityToDTO(store), summary, charts);
    }

    /**
     * Calcule le résumé des statistiques
     */
    private AppDashboardDTO.Summary calculateSummary(List<AppStats> stats) {
        long totalDownloads = stats.stream()
                .filter(s -> s.getMetricType() == AppStats.MetricType.DOWNLOADS)
                .mapToLong(AppStats::getValue)
                .sum();

        long activeUsers = (long) stats.stream()
                .filter(s -> s.getMetricType() == AppStats.MetricType.ACTIVE_USERS)
                .mapToLong(AppStats::getValue)
                .average()
                .orElse(0);

        double totalRevenue = stats.stream()
                .filter(s -> s.getMetricType() == AppStats.MetricType.REVENUE)
                .mapToDouble(s -> s.getRevenueAmount() != null ? s.getRevenueAmount() : 0.0)
                .sum();

        String currency = stats.stream()
                .filter(s -> s.getMetricType() == AppStats.MetricType.REVENUE && s.getCurrency() != null)
                .map(AppStats::getCurrency)
                .findFirst()
                .orElse("USD");

        return new AppDashboardDTO.Summary(totalDownloads, activeUsers, totalRevenue, currency);
    }

    /**
     * Prépare les données pour les graphiques
     */
    private List<AppDashboardDTO.ChartData> prepareChartData(List<AppStats> stats) {
        List<AppDashboardDTO.ChartData> charts = new ArrayList<>();

        charts.add(createChartData("Téléchargements", stats, AppStats.MetricType.DOWNLOADS));
        charts.add(createChartDataRevenue("Revenus", stats));
        charts.add(createChartData("Utilisateurs actifs", stats, AppStats.MetricType.ACTIVE_USERS));
        charts.add(createChartData("Sessions", stats, AppStats.MetricType.SESSIONS));
        charts.add(createChartData("Rétention J+1", stats, AppStats.MetricType.RETENTION_DAY_1));
        charts.add(createChartData("Rétention J+7", stats, AppStats.MetricType.RETENTION_DAY_7));
        charts.add(createChartData("Crashes", stats, AppStats.MetricType.CRASHES));
        charts.add(createChartData("Notes moyennes", stats, AppStats.MetricType.RATINGS));

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
