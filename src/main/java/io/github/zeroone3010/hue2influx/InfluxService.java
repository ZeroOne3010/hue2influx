package io.github.zeroone3010.hue2influx;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import java.util.Map;

public class InfluxService {
  private Hue2InfluxConfiguration configuration;

  public InfluxService(final Hue2InfluxConfiguration configuration) {
    this.configuration = configuration;
  }

  void store(final Map<String, Double> brightnessByRoom) {
    try (final InfluxDB influxDb = InfluxDBFactory
      .connect(configuration.getInfluxUrl(), configuration.getInfluxUserName(), configuration.getInfluxPassword())
      .setDatabase(configuration.getInfluxDatabase()).setRetentionPolicy("")) {
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
}
