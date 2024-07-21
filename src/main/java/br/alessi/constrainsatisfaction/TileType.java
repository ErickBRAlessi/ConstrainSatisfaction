package br.alessi.constrainsatisfaction;

import java.util.List;
import java.util.Random;

public enum TileType {
    UNDECLARED, MOUNTAIN, HIGH_MOUNTAIN, FOREST, PLAIN, WATER, DEEPWATER, DESERT, CITY;

    private static final List<TileType> NOT_UNDECLARED = List.of(
            TileType.MOUNTAIN,
            TileType.HIGH_MOUNTAIN,
            TileType.FOREST,
            TileType.PLAIN,
            TileType.WATER,
            TileType.DEEPWATER,
            TileType.DESERT,
            TileType.CITY
            );

    public static TileType getRandomNotUndeclared(){
        return NOT_UNDECLARED.get(new Random().nextInt(NOT_UNDECLARED.size()));
    }

}
