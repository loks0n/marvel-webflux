package com.github.loks0n.marvel.client;

import com.github.loks0n.marvel.config.MarvelClientConfig;
import com.github.loks0n.marvel.dto.CharactersDataWrapperDto;
import com.github.loks0n.marvel.exception.custom.MarvelCharacterNotFoundException;
import com.github.loks0n.marvel.exception.custom.MarvelClientConflictException;
import com.github.loks0n.marvel.security.MarvelClientAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Repository
@RequiredArgsConstructor
public class MarvelCharacterClient {

    private final MarvelClientConfig config;
    private final MarvelClientAuth auth;

    private WebClient client;

    @PostConstruct
    public void init() {
        // Build webclient with 2MB buffer else getAll() queries will run out of memory.
        client =
                WebClient.builder()
                        .baseUrl(config.getCharactersUrl())
                        .exchangeStrategies(
                                ExchangeStrategies.builder()
                                        .codecs(
                                                clientCodecConfigurer ->
                                                        clientCodecConfigurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                                        .build())
                        .build();
    }

    public Mono<CharactersDataWrapperDto> get(Long id) {
        return client
                .get()
                .uri(uriBuilder -> authenticate(uriBuilder).path("/" + id.toString()).build())
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.CONFLICT,
                        result -> Mono.error(new MarvelClientConflictException()))
                .onStatus(
                        status -> status == HttpStatus.NOT_FOUND,
                        response -> Mono.error(new MarvelCharacterNotFoundException(id)))
                .bodyToMono(CharactersDataWrapperDto.class);
    }

    public Mono<Integer> getTotal() {
        return getAll(1, 0).map(data -> data.getData().getTotal());
    }

    public Mono<CharactersDataWrapperDto> getAll(Integer limit, Integer offset) {
        return client
                .get()
                .uri(
                        uriBuilder ->
                                authenticate(uriBuilder)
                                        .queryParam("limit", limit)
                                        .queryParam("offset", offset)
                                        .build())
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.CONFLICT,
                        result -> Mono.error(new MarvelClientConflictException()))
                .bodyToMono(CharactersDataWrapperDto.class);
    }

    private UriBuilder authenticate(UriBuilder uriBuilder) {
        return uriBuilder
                .queryParam("ts", auth.getTimestamp())
                .queryParam("apikey", config.getPublicKey())
                .queryParam("hash", auth.getHash());
    }
}
