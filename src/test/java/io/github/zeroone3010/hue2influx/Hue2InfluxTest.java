package io.github.zeroone3010.hue2influx;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class Hue2InfluxTest {
  @Test
  void testThatAllHueSamplesAreStoredToInflux() {
    final HueService hueService = mock(HueService.class);
    final InfluxService influxService = mock(InfluxService.class);
    final ServiceFactory serviceFactory = createServiceFactory(hueService, influxService);

    final Map<String, Double> sample1 = new HashMap<>();
    sample1.put("Living room", 50d);
    sample1.put("Kitchen", 100d);
    final Map<String, Double> sample2 = new HashMap<>();
    sample2.put("Living room", 50d);
    sample2.put("Kitchen", 100d);
    final Map<String, Double> sample3 = new HashMap<>();
    sample3.put("Living room", 0d);
    sample3.put("Kitchen", 100d);
    final Map<String, Double> sample4 = new HashMap<>();
    sample4.put("Living room", 0d);
    sample4.put("Kitchen", 100d);
    final Map<String, Double> sample5 = new HashMap<>();
    sample5.put("Living room", 0d);
    sample5.put("Kitchen", 0d);

    when(hueService.getBrightnessByRoom()).thenReturn(sample1, sample2, sample3, sample4, sample5);
    final InOrder influxInOrder = Mockito.inOrder(influxService);

    final Hue2Influx hue2Influx = new Hue2Influx(serviceFactory, 0L);
    hue2Influx.run();
    influxInOrder.verify(influxService).store(sample1);
    hue2Influx.run();
    influxInOrder.verify(influxService).store(sample2);
    hue2Influx.run();
    influxInOrder.verify(influxService).store(sample3);
    hue2Influx.run();
    influxInOrder.verify(influxService).store(sample4);
    hue2Influx.run();
    influxInOrder.verify(influxService).store(sample5);
    influxInOrder.verifyNoMoreInteractions();
  }

  @Test
  void testThatOnlyChangedValuesAreStoredToInflux() {
    final HueService hueService = mock(HueService.class);
    final InfluxService influxService = mock(InfluxService.class);
    final ServiceFactory serviceFactory = createServiceFactory(hueService, influxService);

    final Map<String, Double> sample1 = new HashMap<>();
    sample1.put("Living room", 50d);
    sample1.put("Kitchen", 100d);
    final Map<String, Double> sample2 = new HashMap<>();
    sample2.put("Living room", 50d);
    sample2.put("Kitchen", 100d);
    final Map<String, Double> sample3 = new HashMap<>();
    sample3.put("Living room", 0d);
    sample3.put("Kitchen", 100d);
    final Map<String, Double> sample4 = new HashMap<>();
    sample4.put("Living room", 0d);
    sample4.put("Kitchen", 100d);
    final Map<String, Double> sample5 = new HashMap<>();
    sample5.put("Living room", 0d);
    sample5.put("Kitchen", 0d);

    when(hueService.getBrightnessByRoom()).thenReturn(sample1, sample2, sample3, sample4, sample5);
    final InOrder influxInOrder = Mockito.inOrder(influxService);

    final Hue2Influx hue2Influx = new Hue2Influx(serviceFactory, 1000L);
    hue2Influx.run();
    influxInOrder.verify(influxService).store(sample1);
    hue2Influx.run();
    influxInOrder.verify(influxService, times(0)).store(sample2); // Same as the previous one
    hue2Influx.run();
    influxInOrder.verify(influxService).store(sample3);
    hue2Influx.run();
    influxInOrder.verify(influxService, times(0)).store(sample4); // Same as the previous one
    hue2Influx.run();
    influxInOrder.verify(influxService).store(sample5);
    influxInOrder.verifyNoMoreInteractions();
  }

  private ServiceFactory createServiceFactory(final HueService hueService, final InfluxService influxService) {
    return new ServiceFactory() {
      @Override
      public HueService hueService() {
        return hueService;
      }

      @Override
      public InfluxService influxService() {
        return influxService;
      }
    };
  }
}
