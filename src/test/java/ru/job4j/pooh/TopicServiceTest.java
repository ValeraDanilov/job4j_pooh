package ru.job4j.pooh;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TopicServiceTest {

    private String statusSuccess;
    private String statusFail;

    @Before
    public void setUp() {
        this.statusSuccess = "200";
        this.statusFail = "204";
        System.out.println("Before method");
    }

    @After
    public void tearDown() {
        this.statusSuccess = null;
        this.statusFail = null;
        System.out.println("After method");
    }

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        /* Режим topic. Подписываемся на топик weather. client407. */
        topicService.process(new Req("GET", "topic", "weather", paramForSubscriber1));
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(new Req("POST", "topic", "weather", paramForPublisher));
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicService.process(new Req("GET", "topic", "weather", paramForSubscriber1));
        /* Режим topic.  Подписываемся на топик weather. client6565. */
        Resp result2 = topicService.process(new Req("GET", "topic", "weather", paramForSubscriber2));
		/* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565.
        Очередь отсутствует, т.к. еще не был подписан - получит пустую строку */
        Resp result3 = topicService.process(new Req("GET", "topic", "weather", paramForSubscriber2));
        assertEquals(result1.text(), paramForPublisher);
        assertEquals(result1.status(), this.statusSuccess);
        assertEquals(result2.text(), "");
        assertEquals(result2.status(), this.statusSuccess);
        assertEquals(result3.text(), "");
        assertEquals(result3.status(), this.statusFail);

    }

    @Test
    public void whenPostWithoutSubscriberTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=100";
        String paramForSubscriber1 = "client407";
        Resp result1 = topicService.process(new Req("POST", "topic", "weather", paramForPublisher));
        Resp result2 = topicService.process(new Req("GET", "topic", "weather", paramForSubscriber1));
        Resp result3 = topicService.process(new Req("GET", "topic", "weather", paramForSubscriber1));
        assertEquals(result1.text(), "");
        assertEquals(result1.status(), this.statusFail);
        assertEquals(result2.text(), "");
        assertEquals(result2.status(), this.statusSuccess);
        assertEquals(result3.text(), "");
        assertEquals(result3.status(), this.statusFail);
    }

    @Test
    public void when2SubscribersTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=45";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        /* Режим topic. Подписываемся на топик weather. client407. */
        Resp processSub1 = topicService.process(new Req("GET", "topic", "weather", paramForSubscriber1));
        /* Режим topic. Подписываемся на топик weather. client6565. */
        Resp processSub2 = topicService.process(new Req("GET", "topic", "weather", paramForSubscriber2));
        /* Режим topic. Добавляем данные в топик weather. */
        Resp processPublish = topicService.process(new Req("POST", "topic", "weather", paramForPublisher));
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicService.process(new Req("GET", "topic", "weather", paramForSubscriber1));
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565. */
        Resp result2 = topicService.process(new Req("GET", "topic", "weather", paramForSubscriber2));
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565.
         * там пусто */
        Resp result3 = topicService.process(new Req("GET", "topic", "weather", paramForSubscriber2));
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather2. Очередь client6565.
         * там пусто нет такого топика
         * подписались */
        Resp result4 = topicService.process(new Req("GET", "topic", "weather2", paramForSubscriber2));
        assertEquals(processSub1.status(), this.statusSuccess);
        assertEquals(processSub2.status(), this.statusSuccess);
        assertEquals(processPublish.status(), this.statusSuccess);
        assertEquals(result1.text(), paramForPublisher);
        assertEquals(result1.status(), this.statusSuccess);
        assertEquals(result2.text(), paramForPublisher);
        assertEquals(result2.status(), this.statusSuccess);
        assertEquals(result3.text(), "");
        assertEquals(result3.status(), this.statusFail);
        assertEquals(result4.text(), "");
        assertEquals(result4.status(), this.statusSuccess);
    }
}
