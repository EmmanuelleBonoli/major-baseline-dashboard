package com.appdashboard.features.Store;

import com.appdashboard.core.BaseEntity;
import com.appdashboard.features.Application.Application;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stores")
public class Store extends BaseEntity {

    @Column(name = "package_name", unique = true)
    private String packageName; // Pour Android

    @Column(name = "bundle_id", unique = true)
    private String bundleId; // Pour iOS

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Application application;

    @Column(name = "app_store_id")
    private String appStoreId;

    @Column(nullable = false)
    private boolean active = true;

    public enum Platform {
        ANDROID,
        IOS
    }
}
