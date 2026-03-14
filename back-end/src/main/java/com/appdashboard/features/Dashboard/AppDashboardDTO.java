package com.appdashboard.features.Dashboard;

import com.appdashboard.features.Store.StoreDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record AppDashboardDTO(
        StoreDTO store,
        Summary summary,
        List<ChartData> charts,
        List<StoreSummary> storeSummaries) {

    public record StoreSummary(
            java.util.UUID storeId,
            long downloads,
            double revenue,
            String currency,
            double averageRating) {
    }

    public record Summary(
            long totalDownloads,
            double downloadsVariation,
            long activeUsers,
            double activeUsersVariation,
            double totalRevenue,
            double revenueVariation,
            String currency,
            long totalCrashes,
            double crashesVariation) {
    }

    public record ChartData(
            String label,
            List<DataPoint> data) {
    }

    public record DataPoint(
            LocalDate date,
            Number value) {
    }
}
