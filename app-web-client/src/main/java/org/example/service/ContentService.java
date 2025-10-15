package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ContentService {

    private final WebClient webClient;

    public ContentService(@Value("${content-service}") String baseURL) {
        this.webClient = WebClient.builder().baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .filter(logRequest())
                .build();
    }

    @Override
    public Mono<Post> getPost(int id) {
        return webClient.get().uri("posts/{id}", id).retrieve().bodyToMono(Post.class);
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.info("Request: {} {}" + clientRequest.method() + clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}" + name + value)));
            return next.exchange(clientRequest);
        };
    }
}
