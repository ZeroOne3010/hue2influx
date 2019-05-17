package io.github.zeroone3010.hue2influx;

import java.util.Map;

public interface HueService {
  Map<String, Double> getBrightnessByRoom();
}
