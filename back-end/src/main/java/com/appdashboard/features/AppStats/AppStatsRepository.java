package com.appdashboard.features.AppStats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppStatsRepository extends JpaRepository<AppStats, UUID> {


        Optional<AppStats> findByStoreIdAndDateAndMetricType(
                        UUID storeId,
                        LocalDate date,
                        AppStats.MetricType metricType);

        @Query("SELECT s FROM AppStats s WHERE s.store.id = :storeId " +
                        "AND s.date >= :startDate AND s.date <= :endDate " +
                        "ORDER BY s.date ASC")
        List<AppStats> findStatsByDateRange(
                        @Param("storeId") UUID storeId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);


        @Query("SELECT s FROM AppStats s WHERE s.date >= :startDate AND s.date <= :endDate ORDER BY s.date ASC")
        List<AppStats> findGlobalStatsByDateRange(
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);
}
