package com.github.loks0n.marvel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CharacterDto {

    private Long id;
    private String name;
    private String description;
    private CharacterThumbnailDto thumbnail;
}
