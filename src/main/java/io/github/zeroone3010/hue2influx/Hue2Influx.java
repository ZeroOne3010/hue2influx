package io.github.zeroone3010.hue2influx;

import io.github.zeroone3010.hue2influx.service.HueService;
import io.github.zeroone3010.hue2influx.service.InfluxService;
import io.github.zeroone3010.hue2influx.service.ServiceFactory;
import io.github.zeroone3010.hue2influx.service.SingletonFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Clock;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.github.zeroone3010.hue2influx.service.HueService;
import io.github.zeroone3010.hue2influx.service.InfluxService;
import io.github.zeroone3010.hue2influx.service.ServiceFactory;
import io.github.zeroone3010.hue2influx.service.SingletonFactory;

public class Hue2Influx implements Runnable {
  private final InfluxService influxService;
  private final HueService hueService;

  private final long forceUpdateSeconds;

  private long previousSampleTimestamp = 0L;
  private Map<String, Double> previousBrightnessByRoom = new HashMap<>();

  public Hue2Influx(final ServiceFactory serviceFactory, final long forceUpdateSeconds) {
    this.influxService = serviceFactory.influxService();
    this.hueService = serviceFactory.hueService();
    this.forceUpdateSeconds = forceUpdateSeconds;
  }

  @Override
  public void run() {
    final Map<String, Double> brightnessByRoom = hueService.getBrightnessByRoom();
    final long currentTime = Clock.systemDefaultZone().millis();

    final long secondsSincePrevious = Duration.ofMillis(currentTime - previousSampleTimestamp).get(ChronoUnit.SECONDS);
    if (!Objects.equals(brightnessByRoom, previousBrightnessByRoom)
      || secondsSincePrevious >= forceUpdateSeconds) {
      influxService.store(brightnessByRoom);
      previousSampleTimestamp = currentTime;
    }
    previousBrightnessByRoom = brightnessByRoom;
  }

  public static void main(final String... args) {
    final Hue2InfluxConfiguration configuration = configurationLoader(args[0]);
    final Hue2Influx hue2Influx = new Hue2Influx(new SingletonFactory(configuration),
      configuration.getForceUpdateIntervalSeconds());

    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleWithFixedDelay(hue2Influx, 0L, configuration.getUpdateIntervalSeconds(), TimeUnit.SECONDS);
  }

  private static Hue2InfluxConfiguration configurationLoader(final String file) {
    final Hue2InfluxConfiguration configuration = new Hue2InfluxConfiguration();
    try (final InputStream inputStream = new FileInputStream(file)) {
      final Properties properties = new Properties();
      properties.load(inputStream);
      configuration.setHueIp(properties.getProperty("hue.ip"));
      configuration.setHueApiKey(properties.getProperty("hue.apiKey"));
      configuration.setInfluxUrl(properties.getProperty("influx.url"));
      configuration.setInfluxUsername(properties.getProperty("influx.username"));
      configuration.setInfluxPassword(properties.getProperty("influx.password"));
      configuration.setInfluxDatabase(properties.getProperty("influx.database"));
      configuration.setUpdateIntervalSeconds(Long.valueOf(properties.getProperty("updateIntervalSeconds",
        String.valueOf(Hue2InfluxConfiguration.DEFAULT_UPDATE_INTERVAL_SECONDS))));
      configuration.setForceUpdateIntervalSeconds(Long.valueOf(properties.getProperty("forceUpdateIntervalSeconds",
        String.valueOf(Hue2InfluxConfiguration.DEFAULT_FORCE_UPDATE_INTERVAL_SECONDS))));
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return configuration;
  }
}
