package com.appdashboard.features.User;

import com.appdashboard.core.BaseEntity;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

  public static final int EMAIL_MAX_LENGTH = 320;
  public static final int PLAYER_NAME_LENGTH = 10;

  @Column(nullable = false, length = EMAIL_MAX_LENGTH, unique = true)
  private String email;

  @Column(nullable = false)
  private String hashedPassword;

  @Column(nullable = false, length = PLAYER_NAME_LENGTH)
  private String playerName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AccountType accountStatus;

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.EAGER)
  private Set<UserType> roles = new HashSet<>();

  public User(Set<UserType> roles, AccountType accountStatus, String hashedPassword, String email) {
    this.roles = roles;
    this.accountStatus = accountStatus;
    this.hashedPassword = hashedPassword;
    this.email = email;
  }
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(Enum::name).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
  }

  @Override
  public String getPassword() {
    return hashedPassword;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

    public enum AccountType {
        ACTIVE,
        DISABLED,
        DELETED,
    }

    public enum UserType {
        ROLE_DEVELOPER,
        ROLE_ADMIN,
    }


}
