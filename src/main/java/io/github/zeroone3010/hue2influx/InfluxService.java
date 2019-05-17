package io.github.zeroone3010.hue2influx;

import java.util.Map;

public interface InfluxService {
  void store(Map<String, Double> brightnessByRoom);
}
