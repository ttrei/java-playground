package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class ContentService {

    public void run(int countRequests) {
        log.info("ContentService run");

        WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8000")
                .filter(logRequest())
                .build();

        Flux<String> responses = Flux.range(1, countRequests)
                .flatMap(i -> client.get().uri("/").accept(MediaType.TEXT_HTML)
                        .retrieve()
                        .bodyToMono(String.class));

        responses.collectList().block();
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
