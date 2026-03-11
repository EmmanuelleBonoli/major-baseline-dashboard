package com.appdashboard.features.Store;

import com.appdashboard.features.Store.Store.Platform;

import java.util.UUID;

public record StoreDTO(
                UUID id,
                String packageName,
                String bundleId,
                Platform platform) {

        public static StoreDTO fromEntityToDTO(Store store) {
                return new StoreDTO(
                                store.getId(),
                                store.getPackageName(),
                                store.getBundleId(),
                                store.getPlatform());
        }
}
