package com.github.loks0n.marvel.util;

import com.github.loks0n.marvel.dto.CharacterDto;
import com.github.loks0n.marvel.dto.CharacterThumbnailDto;
import com.github.loks0n.marvel.dto.CharactersDataContainerDto;
import com.github.loks0n.marvel.dto.CharactersDataWrapperDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CharacterTestUtil {

    public static CharactersDataWrapperDto mockDataWrapper(Integer total, CharacterDto... characters) {
        return mockDataWrapper(total, Arrays.asList(characters));
    }

    public static CharactersDataWrapperDto mockDataWrapper(CharacterDto... characters) {
        return mockDataWrapper(Arrays.asList(characters));
    }

    public static CharactersDataWrapperDto mockDataWrapper(List<CharacterDto> characterList) {
        CharactersDataContainerDto dataContainer = new CharactersDataContainerDto();
        dataContainer.setResults(characterList);
        dataContainer.setTotal(characterList.size());

        CharactersDataWrapperDto dataWrapper = new CharactersDataWrapperDto();
        dataWrapper.setData(dataContainer);

        return dataWrapper;
    }

    public static CharactersDataWrapperDto mockDataWrapper(Integer total, List<CharacterDto> characterList) {
        CharactersDataContainerDto dataContainer = new CharactersDataContainerDto();
        dataContainer.setResults(characterList);
        dataContainer.setTotal(total);

        CharactersDataWrapperDto dataWrapper = new CharactersDataWrapperDto();
        dataWrapper.setData(dataContainer);

        return dataWrapper;
    }

    public static CharacterDto mockCharacter(Long id) {
        CharacterThumbnailDto thumbnail = new CharacterThumbnailDto();
        thumbnail.setPath("http://images.com/" + id.toString());
        thumbnail.setExtension("jpg");

        CharacterDto character = new CharacterDto();
        character.setId(id);
        character.setName("Character" + id.toString());
        character.setDescription("This is character with id" + id.toString());
        character.setThumbnail(thumbnail);

        return character;
    }

    public static List<CharacterDto> mockCharacterList(Integer size) {
        List<CharacterDto> characterList = new ArrayList<>();

        for (int i = 0; i < size; i = i + 1) {
            characterList.add(mockCharacter(Integer.toUnsignedLong(i)));
        }
        return characterList;
    }
}
