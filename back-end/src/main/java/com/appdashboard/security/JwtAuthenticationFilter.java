package com.appdashboard.security;

import static com.appdashboard.security.SecurityConstants.PUBLIC_URLS;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import com.appdashboard.features.User.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final CustomUserDetailsService customUserDetailsService;
  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
    this.jwtService = jwtService;
    this.customUserDetailsService = customUserDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (isPublicUrl(request) && parseJwt(request) == null) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      String jwt = parseJwt(request);
      if (jwt != null) {
        if (jwtService.validateJwtToken(jwt, response)) {
          String email = jwtService.extractEmail(jwt);
          UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());

          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
          return;
        }
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
    }

    filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");
    String bearer = "Bearer ";
    if (headerAuth != null && headerAuth.startsWith(bearer)) {
      return headerAuth.substring(bearer.length());
    }
    return null;
  }

  private boolean isPublicUrl(HttpServletRequest request) {
    String requestURI = request.getRequestURI();
    return PUBLIC_URLS.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
  }
}
