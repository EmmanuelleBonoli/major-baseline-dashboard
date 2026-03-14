package com.appdashboard.features.Dashboard;

import com.appdashboard.features.Application.Application;
import com.appdashboard.features.Application.ApplicationDTO;
import com.appdashboard.features.Store.Store;
import com.appdashboard.features.Store.StoreDTO;
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

    @PostMapping("/applications")
    public ResponseEntity<ApplicationDTO> createApplication(@RequestBody Application application) {
        return ResponseEntity.ok(applicationService.createApplication(application));
    }

    @DeleteMapping("/applications/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable UUID id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/applications/{id}/restore")
    public ResponseEntity<Void> restoreApplication(@PathVariable UUID id) {
        applicationService.restoreApplication(id);
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/dashboard/applications/{id}/stats - Dashboard agrégé pour une application
     */
    @GetMapping("/applications/{id}/stats")
    public ResponseEntity<AppDashboardDTO> getApplicationDashboard(
            @PathVariable UUID id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        return ResponseEntity.ok(dashboardService.getApplicationDashboard(id, startDate, endDate));
    }

    @PostMapping("/sync-all")
    public ResponseEntity<Void> syncAll() {
        appStatsService.syncAllActiveStores();
        return ResponseEntity.ok().build();
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




    @PostMapping("/applications/{appId}/stores")
    public ResponseEntity<StoreDTO> addStoreToApplication(@PathVariable UUID appId, @RequestBody Store store) {
        com.appdashboard.features.Application.Application app = applicationService.getGameById(appId);
        store.setApplication(app);
        return ResponseEntity.ok(storeService.createStore(store));
    }

    @DeleteMapping("/stores/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable UUID id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * GET /api/dashboard/global-summary - Dashboard global agrégé
     */
    @GetMapping("/global-summary")
    public ResponseEntity<AppDashboardDTO> getGlobalSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        return ResponseEntity.ok(dashboardService.getGlobalDashboard(startDate, endDate));
    }
}
