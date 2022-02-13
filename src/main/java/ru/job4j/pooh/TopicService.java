package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> queue = new ConcurrentHashMap<>();
    private static final String POST = "POST";

    @Override
    public Resp process(Req req) {
        if ((POST.equals(req.httpRequestType())) && (this.queue.get(req.sourceName()) == null)) {
            return new Resp("", "204");
        }
        if (POST.equals(req.httpRequestType())) {
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> result = this.queue.get(req.sourceName());
            for (String value : result.keySet()) {
                result.get(value).add(req.param());
            }
            return new Resp("", "200");
        } else {
            this.queue.putIfAbsent(req.sourceName(), new ConcurrentHashMap<>());
            String result = this.queue.get(req.sourceName()).putIfAbsent(req.param(), new ConcurrentLinkedQueue<>()) == null ? "200" : "204";
            String value = this.queue.get(req.sourceName()).get(req.param()).poll();
            if (value != null && result.equals("204")) {
                return new Resp(value, "200");
            }
            return new Resp("", result);
        }
    }
}
