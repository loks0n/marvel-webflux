package com.github.loks0n.marvel.exception.custom;


public class MarvelClientConflictException extends Exception {

    public MarvelClientConflictException() {
        super("Request to marvel API failed with conflict status.");
    }
}
