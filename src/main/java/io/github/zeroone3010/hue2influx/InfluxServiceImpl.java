package io.github.zeroone3010.hue2influx;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

public class InfluxServiceImpl implements InfluxService {
  private Hue2InfluxConfiguration configuration;

  public InfluxServiceImpl(final Hue2InfluxConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void store(final Map<String, Double> brightnessByRoom) {
    try (final InfluxDB influxDb = InfluxDBFactory
      .connect(configuration.getInfluxUrl(), configuration.getInfluxUsername(), configuration.getInfluxPassword())
      .setDatabase(configuration.getInfluxDatabase()).setRetentionPolicy("").enableBatch()) {
      final long currentTime = System.currentTimeMillis();
      brightnessByRoom.entrySet().stream()
        .map(e -> Point.measurement("hue_measurements")
          .tag("room", e.getKey())
          .addField("brightness", e.getValue())
          .time(currentTime, TimeUnit.MILLISECONDS)
          .build())
        .forEach(influxDb::write);
    }
  }
}
