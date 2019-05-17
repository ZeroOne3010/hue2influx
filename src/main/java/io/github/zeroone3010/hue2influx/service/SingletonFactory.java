package io.github.zeroone3010.hue2influx.service;

import io.github.zeroone3010.hue2influx.Hue2InfluxConfiguration;

public class SingletonFactory implements ServiceFactory {

  private final Hue2InfluxConfiguration configuration;

  private YetAnotherHueApiService hueService;
  private InfluxServiceImpl influxService;

  public SingletonFactory(final Hue2InfluxConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public HueService hueService() {
    if (hueService == null) {
      hueService = new YetAnotherHueApiService(configuration);
    }
    return hueService;
  }

  @Override
  public InfluxService influxService() {
    if (influxService == null) {
      influxService = new InfluxServiceImpl(configuration);
    }
    return influxService;
  }
}
