package io.github.zeroone3010.hue2influx;

public interface ServiceFactory {
  HueService hueService();

  InfluxService influxService();
}
