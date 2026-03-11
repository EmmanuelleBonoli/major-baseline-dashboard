package com.appdashboard.features.Dashboard;

import com.appdashboard.features.Store.StoreDTO;

import java.time.LocalDate;
import java.util.List;

public record AppDashboardDTO(
        StoreDTO store,
        Summary summary,
        List<ChartData> charts) {

    public record Summary(
            long totalDownloads,
            long activeUsers,
            double totalRevenue,
            String currency) {
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
