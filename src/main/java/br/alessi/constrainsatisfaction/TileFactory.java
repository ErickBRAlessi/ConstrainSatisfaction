package br.alessi.constrainsatisfaction;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TileFactory {

    private static final ImagePattern CITY = new ImagePattern(new Image("file:src/main/resources/br/alessi/constrainsatisfaction/images/city.png"), 0, 0, 1, 1, true);
    private static final ImagePattern DEEPWATER = new ImagePattern(new Image("file:src/main/resources/br/alessi/constrainsatisfaction/images/deepwater.png"), 0, 0, 1, 1, true);
    private static final ImagePattern WATER = new ImagePattern(new Image("file:src/main/resources/br/alessi/constrainsatisfaction/images/water.png"), 0, 0, 1, 1, true);
    private static final ImagePattern PLAIN = new ImagePattern(new Image("file:src/main/resources/br/alessi/constrainsatisfaction/images/plain.png"), 0, 0, 1, 1, true);

    private static final Map<TileType, ImagePattern> textureMap = Map.of(
            TileType.CITY, CITY,
            TileType.WATER, WATER,
            TileType.DEEPWATER, DEEPWATER,
            TileType.PLAIN, PLAIN);

    private static final Map<TileType, Color> colorMap = Map.of(
            TileType.UNDECLARED, Color.BLACK,
            TileType.MOUNTAIN, Color.DARKGREY,
            TileType.FOREST, Color.DARKGREEN,
            TileType.PLAIN, Color.PALEGREEN,
            TileType.DESERT, Color.LIGHTYELLOW,
            TileType.WATER, Color.DEEPSKYBLUE,
            TileType.DEEPWATER, Color.BLUE,
            TileType.HIGH_MOUNTAIN, Color.WHITE,
            TileType.CITY, Color.LIGHTGOLDENRODYELLOW);

    private static final Map<TileType, Set<TileType>> constrains = Map.of(
            TileType.UNDECLARED, Set.of(TileType.UNDECLARED, TileType.MOUNTAIN, TileType.FOREST, TileType.PLAIN, TileType.WATER, TileType.DEEPWATER, TileType.HIGH_MOUNTAIN),
            TileType.MOUNTAIN, Set.of(TileType.UNDECLARED, TileType.MOUNTAIN, TileType.FOREST, TileType.HIGH_MOUNTAIN),
            TileType.FOREST, Set.of(TileType.UNDECLARED, TileType.MOUNTAIN, TileType.FOREST, TileType.PLAIN),
            TileType.PLAIN, Set.of(TileType.UNDECLARED, TileType.FOREST, TileType.PLAIN, TileType.DESERT, TileType.WATER, TileType.CITY),
            TileType.DESERT, Set.of(TileType.UNDECLARED, TileType.PLAIN, TileType.DESERT, TileType.WATER),
            TileType.WATER, Set.of(TileType.UNDECLARED, TileType.PLAIN, TileType.DESERT, TileType.WATER, TileType.DEEPWATER),
            TileType.DEEPWATER, Set.of(TileType.UNDECLARED, TileType.WATER, TileType.DEEPWATER),
            TileType.HIGH_MOUNTAIN, Set.of(TileType.UNDECLARED, TileType.MOUNTAIN, TileType.HIGH_MOUNTAIN),
            TileType.CITY, Set.of(TileType.UNDECLARED, TileType.PLAIN, TileType.CITY)
    );

    private final static Map<TileType, Tile> tiles = new HashMap<>();

    public TileFactory() {
        Arrays.stream(TileType.values()).forEach(tileType -> tiles.put(tileType, new Tile(tileType, constrains.get(tileType), colorMap.get(tileType), textureMap.get(tileType))));
    }

    public Tile get(TileType tileType) {
        return tiles.get(tileType);
    }

}