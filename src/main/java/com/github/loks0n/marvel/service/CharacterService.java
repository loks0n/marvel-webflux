package com.github.loks0n.marvel.service;

import com.github.loks0n.marvel.dto.CharacterDto;
import com.github.loks0n.marvel.provider.ICharacterProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final ICharacterProvider provider;

    public Mono<CharacterDto> get(Long id) {
        return provider.get(id);
    }

    @Cacheable(value = "character-ids")
    public Flux<Long> getAllIds() {
        return provider.getAll().map(CharacterDto::getId).cache();
    }
}
