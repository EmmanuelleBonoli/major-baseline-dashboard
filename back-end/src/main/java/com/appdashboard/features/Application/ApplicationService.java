package com.appdashboard.features.Application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.appdashboard.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public List<ApplicationDTO> getAllGames() {
        return applicationRepository.findAll().stream()
                .map(ApplicationDTO::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    public Application getGameById(UUID id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application introuvable"));
    }

    public ApplicationDTO createApplication(Application app) {
        Application saved = applicationRepository.save(app);
        return ApplicationDTO.fromEntityToDTO(saved);
    }

    public void deleteApplication(UUID id) {
        Application app = getGameById(id);
        app.setActive(false);
        if (app.getStores() != null) {
            app.getStores().forEach(store -> store.setActive(false));
        }
        applicationRepository.save(app);
    }

    public void restoreApplication(UUID id) {
        Application app = getGameById(id);
        app.setActive(true);
        applicationRepository.save(app);
    }
}
