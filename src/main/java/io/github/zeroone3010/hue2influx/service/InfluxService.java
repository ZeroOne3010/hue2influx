package io.github.zeroone3010.hue2influx.service;

import java.util.Map;

public interface InfluxService {
  void store(Map<String, Double> brightnessByRoom);
}
