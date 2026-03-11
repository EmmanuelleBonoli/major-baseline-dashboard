package com.appdashboard.features.Store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.appdashboard.exception.ResourceNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final StoreRepository storeRepository;

    public List<StoreDTO> getAllStores() {
        return storeRepository.findAll().stream()
                .map(StoreDTO::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    public Store getStoreById(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store introuvable"));
    }
}
