package com.appdashboard.features.Application;

import com.appdashboard.features.Store.StoreDTO;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record ApplicationDTO(
                UUID id,
                String name,
                String icon,
                boolean active,
                List<StoreDTO> stores) {

        public static ApplicationDTO fromEntityToDTO(Application application) {
                return new ApplicationDTO(
                                application.getId(),
                                application.getName(),
                                application.getIcon(),
                                application.isActive(),
                                application.getStores() != null ? application.getStores().stream()
                                                .map(StoreDTO::fromEntityToDTO)
                                                .collect(Collectors.toList()) : List.of());
        }
}
