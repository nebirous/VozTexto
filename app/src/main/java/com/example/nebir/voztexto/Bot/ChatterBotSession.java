package com.example.nebir.voztexto.Bot;

/**
 * Created by nebir on 05/02/2016.
 */
public interface ChatterBotSession {

    ChatterBotThought think(ChatterBotThought thought) throws Exception;

    String think(String text) throws Exception;
}
