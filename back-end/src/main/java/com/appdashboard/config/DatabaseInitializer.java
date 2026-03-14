package com.appdashboard.config;

import com.appdashboard.features.AppStats.AppStats;
import com.appdashboard.features.Store.Store;
import com.appdashboard.features.User.User;
import com.appdashboard.features.AppStats.AppStatsRepository;
import com.appdashboard.features.User.UserRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import com.appdashboard.features.Application.Application;
import com.appdashboard.features.Application.ApplicationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("development")
public class DatabaseInitializer {

  private final UserRepository userRepository;
  private final AppStatsRepository appStatsRepository;
  private final ApplicationRepository applicationRepository;

  public DatabaseInitializer(UserRepository userRepository,
      AppStatsRepository appStatsRepository,
      ApplicationRepository applicationRepository) {
    this.userRepository = userRepository;
    this.appStatsRepository = appStatsRepository;
    this.applicationRepository = applicationRepository;
  }

  @Bean
  CommandLineRunner init() {
    return args -> {
      // 1. Users
      if (userRepository.count() == 0) {
        List<User> users = Arrays.asList(
            new User(
                "manu@gmail.com",
                "$2a$10$jw6BeI/txUaC1BQNGYZn4.hs5wpmLhe2uYpTBB40oUveFE3ZRQYQq", // password is "Password"
                "MajorManu",
                User.AccountType.ACTIVE,
                new HashSet<>(List.of(User.UserType.ROLE_DEVELOPER, User.UserType.ROLE_ADMIN))));

        userRepository.saveAll(users);
      }

      // 2. Games & Applications
      if (applicationRepository.count() == 0) {
        // Application 1: Super Runner (Android & iOS)
        Application app1 = new Application();
        app1.setName("Super Runner");
        app1.setIcon("🏃");
        app1.setActive(true);

        Store store1 = new Store();
        store1.setPackageName("com.example.superrunner");
        store1.setPlatform(Store.Platform.ANDROID);
        store1.setApplication(app1);
        store1.setActive(true);

        Store store2 = new Store();
        store2.setBundleId("com.example.superrunner.ios");
        store2.setAppStoreId("123456789");
        store2.setPlatform(Store.Platform.IOS);
        store2.setApplication(app1);
        store2.setActive(true);

        app1.setStores(Arrays.asList(store1, store2));
        applicationRepository.save(app1);

        // Application 2: Puzzle Master (Android only)
        Application app2 = new Application();
        app2.setName("Puzzle Master");
        app2.setIcon("🧩");
        app2.setActive(true);

        Store store3 = new Store();
        store3.setPackageName("com.example.puzzlemaster");
        store3.setPlatform(Store.Platform.ANDROID);
        store3.setApplication(app2);
        store3.setActive(true);

        app2.setStores(Arrays.asList(store3));
        applicationRepository.save(app2);

        // Application 3: Zombie Defense (Archived)
        Application app3 = new Application();
        app3.setName("Zombie Defense");
        app3.setIcon("🧟");
        app3.setActive(false);

        Store store4 = new Store();
        store4.setPackageName("com.example.zombie");
        store4.setPlatform(Store.Platform.ANDROID);
        store4.setApplication(app3);
        store4.setActive(false);

        app3.setStores(Arrays.asList(store4));
        applicationRepository.save(app3);
      }

      // 3. Stats Data (last 365 days to allow testing long periods like 3 months or 1
      // year)
      if (appStatsRepository.count() < 1000) {
        System.out.println("Génération des statistiques de test (365 jours)...");
        applicationRepository.findAll().forEach(app -> {
          try {
            app.getStores().forEach(this::generateStatsForApp);
          } catch (Exception e) {
            System.err.println("Erreur lors de la génération pour " + app.getName() + ": " + e.getMessage());
          }
        });
        System.out.println("Génération terminée.");
      }
    };
  }

  private void generateStatsForApp(Store store) {
    LocalDate endDate = LocalDate.now();
    LocalDate startDate = endDate.minusDays(365);

    List<AppStats> stats = com.appdashboard.features.AppStats.StatsMockGenerator.generateStats(store, startDate,
        endDate);
    appStatsRepository.saveAll(stats);
  }
}
