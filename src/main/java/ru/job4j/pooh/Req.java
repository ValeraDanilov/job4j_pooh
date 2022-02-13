package ru.job4j.pooh;

public record Req(String httpRequestType, String poohMode, String sourceName,
                  String param) {

    public static Req of(String content) {
        String httpRequestType, poohMode, sourceName, param;
        String[] cut = content.split("[\s\n\r/]");
        httpRequestType = cut[0];
        poohMode = cut[2];
        sourceName = cut[3];
        if (httpRequestType.equals("POST")) {
            param = cut[cut.length - 1];
        } else {
            param = poohMode.equals("queue") ? "" : cut[4];
        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }
}
