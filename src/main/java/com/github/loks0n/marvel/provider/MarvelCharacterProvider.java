package com.github.loks0n.marvel.provider;

import com.github.loks0n.marvel.client.MarvelCharacterClient;
import com.github.loks0n.marvel.dto.CharacterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MarvelCharacterProvider implements ICharacterProvider {

    private static final Integer LIMIT = 100;

    private final MarvelCharacterClient client;

    @Override
    public Mono<CharacterDto> get(Long id) {
        return client
                .get(id).map(data -> data.getData().getResults().get(0));
    }

    @Override
    public Flux<CharacterDto> getAll() {
        Mono<Integer> total = client.getTotal();
        return generateOffsets(total)
                .flatMap(offset -> client.getAll(LIMIT, offset))
                .map(data -> data.getData().getResults().stream())
                .flatMap(Flux::fromStream);
    }

    private Flux<Integer> generateOffsets(Mono<Integer> total) {
        return total
                .map(t -> IntStream.rangeClosed(0, t).filter(x -> x % LIMIT == 0).boxed())
                .flatMapMany(Flux::fromStream);
    }
}
