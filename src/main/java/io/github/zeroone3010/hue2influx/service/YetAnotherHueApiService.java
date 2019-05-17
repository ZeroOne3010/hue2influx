package io.github.zeroone3010.hue2influx.service;

import static java.util.stream.Collectors.toMap;
import java.util.Map;
import java.util.OptionalDouble;

import io.github.zeroone3010.hue2influx.Hue2InfluxConfiguration;
import io.github.zeroone3010.yahueapi.Hue;
import io.github.zeroone3010.yahueapi.HueBridgeProtocol;
import io.github.zeroone3010.yahueapi.Light;

class YetAnotherHueApiService implements HueService {
  private final Hue hue;

  public YetAnotherHueApiService(final Hue2InfluxConfiguration configuration) {
    this(new Hue(HueBridgeProtocol.UNVERIFIED_HTTPS, configuration.getHueIp(), configuration.getHueApiKey()));
  }

  public YetAnotherHueApiService(final Hue hue) {
    this.hue = hue;
  }

  @Override
  public Map<String, Double> getBrightnessByRoom() {
    return hue.getRooms().stream()
      .map(room -> new Pair(room.getName(), room.getLights().stream()
        .filter(Light::isReachable)
        .mapToInt(light -> light.isOn() ? light.getState().getBri() : 0)
        .average()
      ))
      .collect(toMap(Pair::getRoom, p -> p.getBrightness().orElse(0d)));
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
