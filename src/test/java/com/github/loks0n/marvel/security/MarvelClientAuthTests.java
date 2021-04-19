package com.github.loks0n.marvel.security;

import com.github.loks0n.marvel.config.MarvelClientConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarvelClientAuthTests {

    @Mock
    private Clock clock;

    @Mock
    private MarvelClientConfig config;

    @InjectMocks
    private MarvelClientAuth auth;

    @Test
    void whenGetHashReturnsCorrectHashGivenInit() {
        when(config.getPrivateKey()).thenReturn("private-key");
        when(config.getPublicKey()).thenReturn("public-key");
        when(clock.millis()).thenReturn(1000L);

        auth.init();
        assertThat(auth.getHash()).isEqualTo("f3199ad271cf70dffa2ddec3ea2b0b1f");
    }
}
