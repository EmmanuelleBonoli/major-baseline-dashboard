package com.appdashboard.features.AppStats;

import com.appdashboard.features.AppStats.AppStats.MetricType;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StatsDTO(
                LocalDate date,
                MetricType metricType,
                Long value,
                Double revenueAmount,
                String currency) {
        public static StatsDTO fromEntityToDTO(AppStats stats) {
                return new StatsDTO(
                                stats.getDate(),
                                stats.getMetricType(),
                                stats.getValue(),
                                stats.getRevenueAmount(),
                                stats.getCurrency());
        }
}
