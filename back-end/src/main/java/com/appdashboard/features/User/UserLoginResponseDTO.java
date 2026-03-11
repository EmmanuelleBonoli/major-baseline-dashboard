package com.appdashboard.features.User;

public record UserLoginResponseDTO(String email, String playerName) {
  public static UserLoginResponseDTO fromEntityToDTO(User user) {
    return new UserLoginResponseDTO(user.getEmail(), user.getPlayerName());
  }
}
