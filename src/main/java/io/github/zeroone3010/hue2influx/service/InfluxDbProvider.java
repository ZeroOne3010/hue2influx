package io.github.zeroone3010.hue2influx.service;

import org.influxdb.InfluxDB;

interface InfluxDbProvider {
  InfluxDB getInfluxDb();
}
