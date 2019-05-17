package io.github.zeroone3010.hue2influx;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Hue2Influx implements Runnable {
  private final InfluxService influxService;
  private final HueService hueService;

  public Hue2Influx(final Hue2InfluxConfiguration configuration) {
    this.influxService = new InfluxService(configuration);
    this.hueService = new HueService(configuration);
  }

  @Override
  public void run() {
    final Map<String, Double> brightnessByRoom = hueService.getBrightnessByRoom();
    influxService.store(brightnessByRoom);
  }

  public static void main(final String... args) {
    final Hue2InfluxConfiguration configuration = configurationLoader(args[0]);
    final Hue2Influx hue2Influx = new Hue2Influx(configuration);

    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleWithFixedDelay(hue2Influx, 0L, 10L, TimeUnit.SECONDS);
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
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    return configuration;
  }
}
