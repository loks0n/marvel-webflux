package com.github.loks0n.marvel.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.loks0n.marvel.config.MarvelClientConfig;
import com.github.loks0n.marvel.dto.CharactersDataWrapperDto;
import com.github.loks0n.marvel.exception.custom.MarvelCharacterNotFoundException;
import com.github.loks0n.marvel.exception.custom.MarvelClientConflictException;
import com.github.loks0n.marvel.security.MarvelClientAuth;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.io.IOException;

import static com.github.loks0n.marvel.util.CharacterTestUtil.mockCharacter;
import static com.github.loks0n.marvel.util.CharacterTestUtil.mockDataWrapper;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarvelCharacterClientTests {

    private static MockWebServer mockServer;
    private static ObjectMapper objectMapper;

    @Mock
    private MarvelClientConfig config;

    @Mock
    private MarvelClientAuth auth;

    @InjectMocks
    private MarvelCharacterClient client;

    @BeforeAll
    static void startMockServer() throws IOException {
        objectMapper = new ObjectMapper();
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @AfterAll
    static void stopMockServer() throws IOException {
        mockServer.shutdown();
    }

    @BeforeEach
    void setUp() {
        String mockServerUrl = mockServer.url("/characters").toString();

        when(config.getCharactersUrl()).thenReturn(mockServerUrl);
        when(config.getPublicKey()).thenReturn("xxx");
        when(auth.getHash()).thenReturn("123");
        when(auth.getTimestamp()).thenReturn(456L);

        client.init();
    }

    @Test
    void whenGetReturnsCharacterGivenCharacterExists() throws JsonProcessingException, InterruptedException {
        CharactersDataWrapperDto dataWrapper = mockDataWrapper(1, mockCharacter(1000L));

        MockResponse response = new MockResponse()
                .setResponseCode(200)
                .setHeader("content-type", "application/json")
                .setBody(objectMapper.writeValueAsString(dataWrapper));

        mockServer.enqueue(response);

        StepVerifier.create(client.get(1000L))
                .expectNext(dataWrapper)
                .verifyComplete();

        mockServer.takeRequest();
    }

    @Test
    void whenGetThrowsMarvelCharacterNotFoundExceptionGivenNotFound() throws InterruptedException {
        MockResponse response = new MockResponse()
                .setResponseCode(404);

        mockServer.enqueue(response);

        StepVerifier.create(client.get(1000L))
                .verifyError(MarvelCharacterNotFoundException.class);

        mockServer.takeRequest();
    }

    @Test
    void whenGetThrowsMarvelClientConflictExceptionGivenConflict() throws InterruptedException {
        MockResponse response = new MockResponse()
                .setResponseCode(409);

        mockServer.enqueue(response);

        StepVerifier.create(client.get(1000L))
                .verifyError(MarvelClientConflictException.class);

        mockServer.takeRequest();
    }

    @Test
    void whenGetTotalReturnsTotal() throws JsonProcessingException, InterruptedException {
        CharactersDataWrapperDto dataWrapper = mockDataWrapper(33);

        MockResponse response = new MockResponse()
                .setResponseCode(200)
                .setHeader("content-type", "application/json")
                .setBody(objectMapper.writeValueAsString(dataWrapper));

        mockServer.enqueue(response);

        StepVerifier.create(client.getTotal())
                .expectNext(33)
                .verifyComplete();

        mockServer.takeRequest();
    }

    @Test
    void whenGetAllReturnsCharacters() throws JsonProcessingException, InterruptedException {
        CharactersDataWrapperDto dataWrapper = mockDataWrapper(3,
                mockCharacter(1000L), mockCharacter(2000L), mockCharacter(3000L));

        MockResponse response = new MockResponse()
                .setResponseCode(200)
                .setHeader("content-type", "application/json")
                .setBody(objectMapper.writeValueAsString(dataWrapper));

        mockServer.enqueue(response);

        StepVerifier.create(client.getAll(3, 0))
                .expectNext(dataWrapper)
                .verifyComplete();

        mockServer.takeRequest();
    }

    @Test
    void whenGetAllCallsWithLimitAndRequestParams() throws JsonProcessingException, InterruptedException {
        CharactersDataWrapperDto dataWrapper = mockDataWrapper(3,
                mockCharacter(1000L), mockCharacter(2000L), mockCharacter(3000L));

        MockResponse response = new MockResponse()
                .setResponseCode(200)
                .setHeader("content-type", "application/json")
                .setBody(objectMapper.writeValueAsString(dataWrapper));

        mockServer.enqueue(response);
        client.getAll(3, 0).block();

        RecordedRequest request = mockServer.takeRequest();

        assertThat(request.getPath()).contains("&limit=3");
        assertThat(request.getPath()).contains("&offset=0");
    }
}
