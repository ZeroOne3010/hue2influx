package io.github.zeroone3010.hue2influx;

import com.github.zeroone3010.yahueapi.Hue;
import com.github.zeroone3010.yahueapi.HueBridgeProtocol;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import java.util.Map;
import java.util.OptionalDouble;

import static java.util.stream.Collectors.toMap;

public class Hue2Influx {
  public static void main(final String... args) {
    final String ip = args[0];
    final String apiKey = args[1];
    final String influxUrl = args[2];
    final String influxUserName = args[3];
    final String influxPassword = args[4];
    final String influxDatabase = args[5];

    final Hue hue = new Hue(HueBridgeProtocol.UNVERIFIED_HTTPS, ip, apiKey);
    final Map<String, Double> brightnessByRoom = hue.getRooms().stream()
      .map(room -> new Pair(room.getName(), room.getLights().stream()
        .filter(light -> light.isOn())
        .mapToInt(light -> light.getState().getBri())
        .average()
      ))
      .collect(toMap(Pair::getRoom, p -> p.getBrightness().orElse(0d)));

    try (final InfluxDB influxDb = InfluxDBFactory.connect(influxUrl, influxUserName, influxPassword)
      .setDatabase(influxDatabase).setRetentionPolicy("")) {
      brightnessByRoom.entrySet().stream()
        .map(e -> Point.measurement("hue_measurements")
          .tag("room", e.getKey())
          .addField("brightness", e.getValue()).build())
        .forEach(point -> {
          System.out.println(point);
          influxDb.write(point);
        });
    }
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
