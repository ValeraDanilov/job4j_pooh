package ru.job4j.pooh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReqTest {

    @Before
    public void setUp() {
        System.out.println("Before method");
    }

    @After
    public void tearDown() {
        System.out.println("After method");
    }

    @Test
    public void whenQueueModePostMethod() {
        String ls = System.lineSeparator();
        String content = "POST /queue/weather HTTP/1.1" + ls + "Host: localhost:9000" + ls + "User-Agent: curl/7.72.0" + ls + "Accept: */*" + ls + "Content-Length: 14" + ls + "Content-Type: application/x-www-form-urlencoded" + ls + "" + ls + "temperature=18" + ls;
        Req req = Req.of(content);
        assertEquals(req.httpRequestType(), "POST");
        assertEquals(req.poohMode(), "queue");
        assertEquals(req.sourceName(), "weather");
        assertEquals(req.param(), "temperature=18");
    }

    @Test
    public void whenQueueModeGetMethod() {
        String ls = System.lineSeparator();
        String content = "GET /queue/weather HTTP/1.1" + ls + "Host: localhost:9000" + ls + "User-Agent: curl/7.72.0" + ls + "Accept: */*" + ls + ls + ls;
        Req req = Req.of(content);
        assertEquals(req.httpRequestType(), "GET");
        assertEquals(req.poohMode(), "queue");
        assertEquals(req.sourceName(), "weather");
        assertEquals(req.param(), "");
    }

    @Test
    public void whenTopicModePostMethod() {
        String ls = System.lineSeparator();
        String content = "POST /topic/weather HTTP/1.1" + ls + "Host: localhost:9000" + ls + "User-Agent: curl/7.72.0" + ls + "Accept: */*" + ls + "Content-Length: 14" + ls + "Content-Type: application/x-www-form-urlencoded" + ls + "" + ls + "temperature=18" + ls;
        Req req = Req.of(content);
        assertEquals(req.httpRequestType(), "POST");
        assertEquals(req.poohMode(), "topic");
        assertEquals(req.sourceName(), "weather");
        assertEquals(req.param(), "temperature=18");
    }

    @Test
    public void whenTopicModeGetMethod() {
        String ls = System.lineSeparator();
        String content = "GET /topic/weather/client407 HTTP/1.1" + ls + "Host: localhost:9000" + ls + "User-Agent: curl/7.72.0" + ls + "Accept: */*" + ls + ls + ls;
        Req req = Req.of(content);
        assertEquals(req.httpRequestType(), "GET");
        assertEquals(req.poohMode(), "topic");
        assertEquals(req.sourceName(), "weather");
        assertEquals(req.param(), "client407");
    }
}
