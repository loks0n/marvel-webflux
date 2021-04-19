package com.github.loks0n.marvel.provider;

import com.github.loks0n.marvel.dto.CharacterDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface ICharacterProvider {
    Mono<CharacterDto> get(Long id);

    Flux<CharacterDto> getAll();
}
