package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class ContentService {

    public void run() {
        log.info("ContentService run");

        WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8000")
                .filter(logRequest())
                .build();

        ResponseEntity<String> result =
                client.get().accept(MediaType.TEXT_HTML).retrieve().toEntity(String.class).block();

        log.info("Result:\n{}", result);

    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return next.exchange(clientRequest);
        };
    }
}
