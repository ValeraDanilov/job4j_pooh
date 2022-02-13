package ru.job4j.pooh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueueServiceTest {

    @Before
    public void setUp() {
        System.out.println("Before method");
    }

    @After
    public void tearDown() {
        System.out.println("After method");
    }

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String weatherSourceName = "weather";
        String trafficSourceName = "traffic";
        String paramForWeatherSourceName = "temperature=18";
        String paramForTrafficSourceName = "cars=20188";

        queueService.process(
                new Req("POST", "queue", weatherSourceName, paramForWeatherSourceName)
        );
        queueService.process(
                new Req("POST", "queue", trafficSourceName, paramForTrafficSourceName)
        );
        Resp resultFromWeatherSource1 = queueService.process(
                new Req("GET", "queue", weatherSourceName, null)
        );
        Resp resultFromWeatherSource2 = queueService.process(
                new Req("GET", "queue", weatherSourceName, null)
        );
        Resp resultFromTrafficSource1 = queueService.process(
                new Req("GET", "queue", trafficSourceName, null)
        );
        Resp resultFromTrafficSource2 = queueService.process(
                new Req("GET", "queue", trafficSourceName, null)
        );
        assertEquals(resultFromWeatherSource1.text(), "temperature=18");
        assertEquals(resultFromWeatherSource2.text(), "");
        assertEquals(resultFromTrafficSource1.text(), "cars=20188");
        assertEquals(resultFromTrafficSource2.text(), "");
    }

    @Test
    public void whenPostThenGetReturnNull() {
        QueueService queueService = new QueueService();
        Resp result = queueService.process(new Req("GET", "queue", "weather", null));
        assertEquals(result.text(), "");
    }
}
