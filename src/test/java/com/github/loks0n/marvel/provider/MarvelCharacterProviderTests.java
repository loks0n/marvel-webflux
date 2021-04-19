package com.github.loks0n.marvel.provider;

import com.github.loks0n.marvel.client.MarvelCharacterClient;
import com.github.loks0n.marvel.dto.CharacterDto;
import com.github.loks0n.marvel.dto.CharactersDataWrapperDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.github.loks0n.marvel.util.CharacterTestUtil.mockCharacter;
import static com.github.loks0n.marvel.util.CharacterTestUtil.mockCharacterList;
import static com.github.loks0n.marvel.util.CharacterTestUtil.mockDataWrapper;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarvelCharacterProviderTests {

    @Mock
    private MarvelCharacterClient client;

    @InjectMocks
    private MarvelCharacterProvider provider;

    @Test
    void whenGetReturnsCharacterFromWrapper() {
        CharacterDto character = mockCharacter(1000L);
        CharactersDataWrapperDto dataWrapper = mockDataWrapper(1, character);

        when(client.get(1000L)).thenReturn(Mono.just(dataWrapper));

        StepVerifier.create(provider.get(1000L))
                .expectNext(character)
                .verifyComplete();
    }

    @Test
    void whenGetAllCallsCharactersGivenOneOffset() {
        CharacterDto characterOne = mockCharacter(1000L);
        CharacterDto characterTwo = mockCharacter(2000L);
        CharactersDataWrapperDto dataWrapper = mockDataWrapper(2, characterOne, characterTwo);

        when(client.getTotal()).thenReturn(Mono.just(2));
        when(client.getAll(100, 0)).thenReturn(Mono.just(dataWrapper));

        StepVerifier.create(provider.getAll())
                .expectNext(characterOne)
                .expectNext(characterTwo)
                .verifyComplete();
    }

    @Test
    void whenGetAllReturnsCharactersGivenTwoOffset() {
        List<CharacterDto> charactersOne = mockCharacterList(100);
        CharacterDto characterTwo = mockCharacter(100L);

        CharactersDataWrapperDto dataWrapperOne = mockDataWrapper(charactersOne);
        CharactersDataWrapperDto dataWrapperTwo = mockDataWrapper(characterTwo);

        when(client.getTotal()).thenReturn(Mono.just(101));
        when(client.getAll(100, 0)).thenReturn(Mono.just(dataWrapperOne));
        when(client.getAll(100, 100)).thenReturn(Mono.just(dataWrapperTwo));

        StepVerifier.create(provider.getAll())
                .expectNextSequence(charactersOne)
                .expectNext(characterTwo)
                .verifyComplete();
    }
}
