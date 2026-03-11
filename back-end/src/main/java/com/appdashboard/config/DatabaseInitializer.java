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
import java.util.Random;
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
        app1.setIcon("🎮");

        Store store1 = new Store();
        store1.setPackageName("com.example.superrunner");
        store1.setPlatform(Store.Platform.ANDROID);
        store1.setApplication(app1);

        Store store2 = new Store();
        store2.setBundleId("com.example.superrunner.ios");
        store2.setAppStoreId("123456789");
        store2.setPlatform(Store.Platform.IOS);
        store2.setApplication(app1);

        app1.setStores(Arrays.asList(store1, store2));
        applicationRepository.save(app1);

        // Application 2: Puzzle Master (Android only)
        Application app2 = new Application();
        app2.setName("Puzzle Master");
        app2.setIcon("🎮");

        Store store3 = new Store();
        store3.setPackageName("com.example.puzzlemaster");
        store3.setPlatform(Store.Platform.ANDROID);
        store3.setApplication(app2);

        app2.setStores(Arrays.asList(store3));
        applicationRepository.save(app2);

        // 3. Stats Data (last 30 days)
        generateStatsForApp(store1);
        generateStatsForApp(store2);
        generateStatsForApp(store3);
      }
    };
  }

  private void generateStatsForApp(Store store) {
    Random random = new Random();
    LocalDate today = LocalDate.now();

    for (int i = 0; i < 30; i++) {
      LocalDate date = today.minusDays(i);

      // Downloads
      createStat(store, date, AppStats.MetricType.DOWNLOADS, (long) (random.nextInt(100) + 50));

      // Revenue
      double revenue = random.nextDouble() * 50;
      createStatRevenue(store, date, AppStats.MetricType.REVENUE, revenue, "EUR");

      // Active Users
      createStat(store, date, AppStats.MetricType.ACTIVE_USERS, (long) (random.nextInt(1000) + 500));

      // Ratings (just a few per day)
      createStat(store, date, AppStats.MetricType.RATINGS, (long) (random.nextInt(5)));
    }
  }

  private void createStat(Store store, LocalDate date, AppStats.MetricType type, Long value) {
    AppStats stat = new AppStats();
    stat.setStore(store);
    stat.setDate(date);
    stat.setMetricType(type);
    stat.setValue(value);
    appStatsRepository.save(stat);
  }

  private void createStatRevenue(Store store, LocalDate date, AppStats.MetricType type, Double amount,
      String currency) {
    AppStats stat = new AppStats();
    stat.setStore(store);
    stat.setDate(date);
    stat.setMetricType(type);
    stat.setValue(0L);
    stat.setRevenueAmount(amount);
    stat.setCurrency(currency);
    appStatsRepository.save(stat);
  }
}
