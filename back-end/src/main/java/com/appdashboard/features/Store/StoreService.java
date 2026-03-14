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
        return getAllStoreEntities().stream()
                .map(StoreDTO::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    public List<Store> getAllStoreEntities() {
        return storeRepository.findAll();
    }

    public Store getStoreById(UUID storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store introuvable"));
    }

    public StoreDTO createStore(Store store) {
        Store saved = storeRepository.save(store);
        return StoreDTO.fromEntityToDTO(saved);
    }

    public void deleteStore(UUID storeId) {
        Store store = getStoreById(storeId);
        store.setActive(false);
        storeRepository.save(store);
    }
}
