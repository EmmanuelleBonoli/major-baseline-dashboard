package com.appdashboard.features.AppStats;

import com.appdashboard.core.BaseEntity;
import com.appdashboard.features.Store.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_stats", uniqueConstraints = @UniqueConstraint(columnNames = { "store_id", "date",
        "metric_type" }))
public class AppStats extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type", nullable = false)
    private MetricType metricType;

    @Column(nullable = false)
    private Long value;

    @Column(name = "revenue_amount")
    private Double revenueAmount;

    private String currency;

    @Column(name = "additional_data", columnDefinition = "TEXT")
    private String additionalData; // JSON pour données supplémentaires

    public enum MetricType {
        DOWNLOADS,
        INSTALLATIONS,
        ACTIVE_USERS,
        SESSIONS,
        REVENUE,
        IN_APP_PURCHASES,
        RETENTION_DAY_1,
        RETENTION_DAY_7,
        RETENTION_DAY_30,
        CRASHES,
        RATINGS,
        REVIEWS
    }

}
