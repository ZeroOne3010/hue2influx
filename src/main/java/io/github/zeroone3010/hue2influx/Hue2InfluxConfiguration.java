package io.github.zeroone3010.hue2influx;

public class Hue2InfluxConfiguration {
  static final long DEFAULT_UPDATE_INTERVAL_SECONDS = 10L;

  private String hueIp;
  private String hueApiKey;

  private String influxUrl;
  private String influxUsername;
  private String influxPassword;
  private String influxDatabase;

  private Long updateIntervalSeconds;

  public String getHueIp() {
    return hueIp;
  }

  public void setHueIp(String hueIp) {
    this.hueIp = hueIp;
  }

  public String getHueApiKey() {
    return hueApiKey;
  }

  public void setHueApiKey(String hueApiKey) {
    this.hueApiKey = hueApiKey;
  }

  public String getInfluxUrl() {
    return influxUrl;
  }

  public void setInfluxUrl(String influxUrl) {
    this.influxUrl = influxUrl;
  }

  public String getInfluxUsername() {
    return influxUsername;
  }

  public void setInfluxUsername(String influxUsername) {
    this.influxUsername = influxUsername;
  }

  public String getInfluxPassword() {
    return influxPassword;
  }

  public void setInfluxPassword(String influxPassword) {
    this.influxPassword = influxPassword;
  }

  public String getInfluxDatabase() {
    return influxDatabase;
  }

  public void setInfluxDatabase(String influxDatabase) {
    this.influxDatabase = influxDatabase;
  }

  public Long getUpdateIntervalSeconds() {
    return updateIntervalSeconds;
  }

  public void setUpdateIntervalSeconds(Long updateIntervalSeconds) {
    this.updateIntervalSeconds = updateIntervalSeconds;
  }
}
