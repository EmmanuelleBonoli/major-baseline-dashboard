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

        List<AppStats> findByStoreIdAndMetricTypeAndDateBetween(
                        UUID storeId,
                        AppStats.MetricType metricType,
                        LocalDate startDate,
                        LocalDate endDate);

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

        @Query("SELECT new com.appdashboard.features.AppStats.StatsDTO(s.date, s.metricType, SUM(s.value), SUM(s.revenueAmount), s.currency) "
                        +
                        "FROM AppStats s " +
                        "WHERE s.metricType = :metricType " +
                        "AND s.date >= :startDate AND s.date <= :endDate " +
                        "GROUP BY s.date, s.metricType, s.currency " +
                        "ORDER BY s.date ASC")
        List<StatsDTO> findGlobalStatsByMetricAndDateRange(
                        @Param("metricType") AppStats.MetricType metricType,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate);
}
