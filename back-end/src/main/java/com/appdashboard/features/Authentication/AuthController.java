package com.appdashboard.features.Authentication;

import com.appdashboard.features.User.UserLoginResponseDTO;
import com.appdashboard.features.User.UserRegistrationRequestDTO;
import com.appdashboard.features.User.User;
import com.appdashboard.features.User.UserService;
import com.appdashboard.security.LoginRateLimitFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final UserService userService;
  private final AuthService authService;
  private final LoginRateLimitFilter loginRateLimitFilter;

  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> authenticate(@Valid @RequestBody UserRegistrationRequestDTO userLoginDTO,
      HttpServletRequest request,
      HttpServletResponse res) {
    String token = authService.authenticate(userLoginDTO.email(), userLoginDTO.password());
    User user = userService.findByEmail(userLoginDTO.email());
    String key = "ip:" + (request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For")
        : request.getRemoteAddr());

    loginRateLimitFilter.resetBucket(key);
    res.setHeader("X-Rate-Limit-Remaining", String.valueOf(loginRateLimitFilter.getRemainingAttempt(key)));

    Map<String, Object> response = new HashMap<>();

    response.put("token", token);
    response.put("user", UserLoginResponseDTO.fromEntityToDTO(user));

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
