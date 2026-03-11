package com.appdashboard.features.Application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public List<ApplicationDTO> getAllGames() {
        return applicationRepository.findAll().stream()
                .map(ApplicationDTO::fromEntityToDTO)
                .collect(Collectors.toList());
    }
}
