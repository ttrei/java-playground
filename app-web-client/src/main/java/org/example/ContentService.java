package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ContentService {

    private final AtomicInteger requests = new AtomicInteger(0);
    private final AtomicInteger responses = new AtomicInteger(0);

    public void run(int countRequests) {
        WebClient client = WebClient.builder()
                .baseUrl("http://localhost:8000")
                .filter(logRequest())
                .filter(logResponse())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.newConnection())))
                .build();

        log.info("Call 1");
        callManyTimes(client, countRequests);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        log.info("Call 2");
        callManyTimes(client, countRequests);
    }

    private void callManyTimes(WebClient webClient, int count) {
        Flux<String> responses = Flux.range(1, count)
                .flatMap(i -> webClient.get().uri("/").accept(MediaType.TEXT_HTML)
                        .retrieve()
                        .bodyToMono(String.class));
        responses.collectList().block();
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.info("Request: {}", requests.incrementAndGet());
            return next.exchange(clientRequest);
        };
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response {} status={}", responses.incrementAndGet(), clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}
