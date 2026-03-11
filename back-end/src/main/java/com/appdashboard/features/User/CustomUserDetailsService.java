package com.appdashboard.features.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.appdashboard.exception.ResourceNotFoundException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec l'email : " + email));
  }
}
