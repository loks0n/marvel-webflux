package com.github.loks0n.marvel.security;

import com.github.loks0n.marvel.config.MarvelClientConfig;
import com.google.common.hash.Hashing;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.time.Clock;

@Component
@RequiredArgsConstructor
@Getter
public class MarvelClientAuth {

    private final MarvelClientConfig config;

    private final Clock clock;

    private Long timestamp;
    private String hash;

    @SuppressWarnings("deprecation")
    private static String hashString(String toHash) {
        return Hashing.md5().hashString(toHash, StandardCharsets.UTF_8).toString();
    }

    @PostConstruct
    public void init() {
        timestamp = clock.millis();
        hash = hashString(getCompositeToHash());
    }

    private String getCompositeToHash() {
        return timestamp.toString() + config.getPrivateKey() + config.getPublicKey();
    }
}
