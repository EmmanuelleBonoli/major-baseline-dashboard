package com.appdashboard.features.Dashboard;

import com.appdashboard.features.Store.StoreDTO;
import com.appdashboard.features.Application.ApplicationDTO;
import com.appdashboard.features.AppStats.StatsDTO;
import com.appdashboard.features.AppStats.AppStats;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.appdashboard.features.Application.ApplicationService;
import com.appdashboard.features.Store.StoreService;
import com.appdashboard.features.AppStats.AppStatsService;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ApplicationService applicationService;
    private final StoreService storeService;
    private final AppStatsService appStatsService;

    /**
     * GET /api/dashboard/applications - Liste tous les jeux
     */
    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationDTO>> getAllGames() {
        return ResponseEntity.ok(applicationService.getAllGames());
    }

    /**
     * GET /api/dashboard/stores - Liste toutes les stores
     */
    @GetMapping("/stores")
    public ResponseEntity<List<StoreDTO>> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    /**
     * GET /api/dashboard/stores/{id} - Dashboard pour une app spécifique
     */
    @GetMapping("/stores/{id}")
    public ResponseEntity<AppDashboardDTO> getAppDashboard(
            @PathVariable UUID id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        return ResponseEntity.ok(dashboardService.getAppDashboard(id, startDate, endDate));
    }

    /**
     * GET /api/dashboard/stores/{id}/stats - Statistiques par métrique
     */
    @GetMapping("/stores/{id}/stats")
    public ResponseEntity<List<StatsDTO>> getStats(
            @PathVariable UUID id,
            @RequestParam AppStats.MetricType metricType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(
                appStatsService.getStatsByMetric(id, metricType, startDate, endDate));
    }

    /**
     * POST /api/dashboard/stores/{id}/sync - Synchroniser les données du store
     */
    @PostMapping("/stores/{id}/sync")
    public ResponseEntity<Void> syncStoreData(@PathVariable UUID id) {
        appStatsService.syncStoreData(id);
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/dashboard/global-stats - Statistiques globales agrégées
     */
    @GetMapping("/global-stats")
    public ResponseEntity<List<StatsDTO>> getGlobalStats(
            @RequestParam AppStats.MetricType metricType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(
                appStatsService.getGlobalStats(metricType, startDate, endDate));
    }
}
