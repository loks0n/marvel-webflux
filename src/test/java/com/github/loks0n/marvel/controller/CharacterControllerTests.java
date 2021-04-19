package com.github.loks0n.marvel.controller;

import com.github.loks0n.marvel.dto.CharacterDto;
import com.github.loks0n.marvel.exception.custom.MarvelCharacterNotFoundException;
import com.github.loks0n.marvel.exception.custom.MarvelClientConflictException;
import com.github.loks0n.marvel.service.CharacterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static com.github.loks0n.marvel.util.CharacterTestUtil.mockCharacter;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CharacterController.class)
public class CharacterControllerTests {

    @MockBean
    private CharacterService service;

    @Autowired
    private WebTestClient client;

    @Test
    void whenGetReturnsCorrectDtoGivenCharacterExists() {
        CharacterDto character = mockCharacter(1000L);

        when(service.get(1000L)).thenReturn(Mono.just(character));

        client.get()
                .uri("/characters/{id}", 1000L)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CharacterDto.class)
                .isEqualTo(character);
    }

    @Test
    void whenGetReturnsNotFoundGivenCharacterDoesNotExists() {
        final Long ID = 1000L;
        when(service.get(ID)).thenReturn(Mono.error(new MarvelCharacterNotFoundException(1000L)));

        client.get()
                .uri("/characters/{id}", ID)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void whenGetReturnsBadGatewayGivenMarvelClientConflict() {
        final Long ID = 1000L;
        when(service.get(ID)).thenReturn(Mono.error(new MarvelClientConflictException()));

        client.get()
                .uri("/characters/{id}", ID)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_GATEWAY);
    }

    @Test
    void whenGetAllIdsReturnsCorrectIdsGivenCharactersExist() {
        when(service.getAllIds()).thenReturn(Flux.just(1000L, 1100L, 1200L));

        client.get()
                .uri("/characters")
                .exchange()
                .expectStatus().isOk()
                .expectBody(List.class)
                .isEqualTo(Arrays.asList(1000, 1100, 1200));
    }

    @Test
    void whenGetAllIdsReturnsEmptyListGivenNoCharacters() {
        when(service.getAllIds()).thenReturn(Flux.empty());

        client.get()
                .uri("/characters")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isEmpty();
    }

    @Test
    void whenGetAllIdsReturnsBadGatewayGivenMarvelClientConflict() {
        when(service.getAllIds()).thenReturn(Flux.error(new MarvelClientConflictException()));

        client.get()
                .uri("/characters")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_GATEWAY);
    }
}
