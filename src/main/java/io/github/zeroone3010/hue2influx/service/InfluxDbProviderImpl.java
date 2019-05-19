package io.github.zeroone3010.hue2influx.service;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

import io.github.zeroone3010.hue2influx.Hue2InfluxConfiguration;

class InfluxDbProviderImpl implements InfluxDbProvider {
  private final Hue2InfluxConfiguration configuration;
  private InfluxDB influxDB;

  public InfluxDbProviderImpl(final Hue2InfluxConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public InfluxDB getInfluxDb() {
    if (influxDB == null) {
      influxDB = InfluxDBFactory
        .connect(configuration.getInfluxUrl(), configuration.getInfluxUsername(), configuration.getInfluxPassword())
        .setDatabase(configuration.getInfluxDatabase()).setRetentionPolicy("").enableBatch();
    }
    return influxDB;
  }
}
