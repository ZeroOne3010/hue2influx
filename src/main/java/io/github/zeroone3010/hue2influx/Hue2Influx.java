package io.github.zeroone3010.hue2influx;

import io.github.zeroone3010.yahueapi.Hue;
import io.github.zeroone3010.yahueapi.HueBridgeProtocol;
import io.github.zeroone3010.yahueapi.Light;

import java.util.Map;
import java.util.OptionalDouble;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toMap;

public class Hue2Influx implements Runnable {
  private final Hue2InfluxConfiguration configuration;
  private final InfluxService influxService;

  public Hue2Influx(final Hue2InfluxConfiguration configuration) {
    this.configuration = configuration;
    this.influxService = new InfluxService(configuration);
  }

  @Override
  public void run() {
    final Hue hue = new Hue(HueBridgeProtocol.UNVERIFIED_HTTPS, configuration.getHueIp(), configuration.getHueApiKey());
    final Map<String, Double> brightnessByRoom = hue.getRooms().stream()
      .map(room -> new Pair(room.getName(), room.getLights().stream()
        .filter(Light::isOn)
        .filter(Light::isReachable)
        .mapToInt(light -> light.getState().getBri())
        .average()
      ))
      .collect(toMap(Pair::getRoom, p -> p.getBrightness().orElse(0d)));

    influxService.store(brightnessByRoom);
  }

  public static void main(final String... args) {
    final Hue2InfluxConfiguration configuration = new Hue2InfluxConfiguration();
    configuration.setHueIp(args[0]);
    configuration.setHueApiKey(args[1]);
    configuration.setInfluxUrl(args[2]);
    configuration.setInfluxUserName(args[3]);
    configuration.setInfluxPassword(args[4]);
    configuration.setInfluxDatabase(args[5]);

    final Hue2Influx hue2Influx = new Hue2Influx(configuration);

    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    scheduler.scheduleWithFixedDelay(hue2Influx, 0L, 10L, TimeUnit.SECONDS);
  }


  private static class Pair {
    private final String room;
    private final OptionalDouble brightness;

    public Pair(final String room, final OptionalDouble brightness) {
      this.room = room;
      this.brightness = brightness;
    }

    public String getRoom() {
      return room;
    }

    public OptionalDouble getBrightness() {
      return brightness;
    }
  }
}
