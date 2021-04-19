package com.github.loks0n.marvel.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "marvel-client")
@Getter
@Setter
public class MarvelClientConfig {

    private String publicKey;
    private String privateKey;
    private String baseUrl;
    private String charactersUrl;
}
