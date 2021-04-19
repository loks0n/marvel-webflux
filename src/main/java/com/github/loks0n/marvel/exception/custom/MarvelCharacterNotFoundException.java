package com.github.loks0n.marvel.exception.custom;


import lombok.Getter;

public class MarvelCharacterNotFoundException extends Exception {

    @Getter
    private final Long characterId;

    public MarvelCharacterNotFoundException(Long characterId) {
        super("Could not find Marvel character with id=" + characterId.toString());

        this.characterId = characterId;
    }
}
