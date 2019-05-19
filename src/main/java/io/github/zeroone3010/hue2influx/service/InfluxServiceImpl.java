package io.github.zeroone3010.hue2influx.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;

class InfluxServiceImpl implements InfluxService {
  private InfluxDbProvider influxDbProvider;

  public InfluxServiceImpl(final InfluxDbProvider influxDbProvider) {
    this.influxDbProvider = influxDbProvider;
  }

  @Override
  public void store(final Map<String, Double> brightnessByRoom) {
    try (final InfluxDB influxDb = influxDbProvider.getInfluxDb()) {
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
