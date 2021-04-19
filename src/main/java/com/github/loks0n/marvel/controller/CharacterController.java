package com.github.loks0n.marvel.controller;

import com.github.loks0n.marvel.dto.CharacterDto;
import com.github.loks0n.marvel.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService service;

    @GetMapping("/{id}")
    public Mono<CharacterDto> get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping
    public Flux<Long> getAllIds() {
        return service.getAllIds();
    }
}
