package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();
    private static final String POST = "POST";

    @Override
    public Resp process(Req req) {
        if (POST.equals(req.httpRequestType())) {
            addValue(req);
            return new Resp("", "200");
        }
        return getValue(req);
    }

    private void addValue(Req req) {
        this.queue.putIfAbsent(req.sourceName(), new ConcurrentLinkedQueue<>());
        this.queue.get(req.sourceName()).add(req.param());
    }

    private Resp getValue(Req req) {
        if (this.queue.size() == 0) {
            return new Resp("", "204");
        }
        String value = this.queue.get(req.sourceName()).poll();
        return value == null ? new Resp("", "204") : new Resp(value, "200");
    }
}
