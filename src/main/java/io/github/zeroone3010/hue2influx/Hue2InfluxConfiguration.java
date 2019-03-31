package io.github.zeroone3010.hue2influx;

public class Hue2InfluxConfiguration {
  private String hueIp;
  private String hueApiKey;

  private String influxUrl;
  private String influxUserName;
  private String influxPassword;
  private String influxDatabase;

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

  public String getInfluxUserName() {
    return influxUserName;
  }

  public void setInfluxUsername(String influxUserName) {
    this.influxUserName = influxUserName;
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
}
