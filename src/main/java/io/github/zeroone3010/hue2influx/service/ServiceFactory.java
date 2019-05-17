package io.github.zeroone3010.hue2influx.service;

public interface ServiceFactory {
  HueService hueService();

  InfluxService influxService();
}
