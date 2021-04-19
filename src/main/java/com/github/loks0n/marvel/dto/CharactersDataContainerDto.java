package com.github.loks0n.marvel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CharactersDataContainerDto {

    private List<CharacterDto> results;
    private Integer total;
}
