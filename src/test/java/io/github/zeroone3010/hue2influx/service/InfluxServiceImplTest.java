package io.github.zeroone3010.hue2influx.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.junit.jupiter.api.Test;

class InfluxServiceImplTest {
  @Test
  void testThatDataIsWrittenToGivenInfluxDb() {
    final List<Point> storedData = new ArrayList<>();
    final InfluxDB influxDB = mock(InfluxDB.class);
    doAnswer(invocation -> {
      storedData.add(invocation.getArgument(0));
      return null;
    }).when(influxDB).write(any(Point.class));

    final InfluxDbProvider dbProvider = () -> influxDB;

    final InfluxServiceImpl service = new InfluxServiceImpl(dbProvider);

    service.store(Collections.singletonMap("Living room", 33d));
    assertEquals(1, storedData.size());

    service.store(Collections.singletonMap("Living room", 33d));
    service.store(Collections.singletonMap("Living room", 33d));
    assertEquals(3, storedData.size());

    service.store(Collections.singletonMap("Hallway", 50d));
    assertEquals(4, storedData.size());
  }
}
