package com.github.loks0n.marvel.service;

import com.github.loks0n.marvel.dto.CharacterDto;
import com.github.loks0n.marvel.provider.ICharacterProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.github.loks0n.marvel.util.CharacterTestUtil.mockCharacter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CharacterServiceTests {

    @Mock
    private ICharacterProvider provider;

    @InjectMocks
    private CharacterService service;

    @Test
    void whenGetReturnsClientGet() {
        Mono<CharacterDto> character = Mono.just(mockCharacter(1000L));
        when(provider.get(1000L)).thenReturn(character);
        assertThat(service.get(1000L)).isEqualTo(character);
    }

    @Test
    void whenGetAllReturnsClientGetMappedToIds() {
        Flux<CharacterDto> characters =
                Flux.just(mockCharacter(1000L), mockCharacter(1100L), mockCharacter(1200L));

        when(provider.getAll()).thenReturn(characters);

        StepVerifier.create(service.getAllIds())
                .expectNext(1000L)
                .expectNext(1100L)
                .expectNext(1200L)
                .expectComplete()
                .verify();
    }
}
