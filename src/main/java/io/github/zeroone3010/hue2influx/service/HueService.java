package io.github.zeroone3010.hue2influx.service;

import java.util.Map;

public interface HueService {
  Map<String, Double> getBrightnessByRoom();
}
