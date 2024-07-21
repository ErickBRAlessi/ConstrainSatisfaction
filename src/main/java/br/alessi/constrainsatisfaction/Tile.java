package br.alessi.constrainsatisfaction;

import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Shape;

import java.util.Objects;
import java.util.Set;

public class Tile {

    private final Color color;
    private final ImagePattern texture;
    private final Set<TileType> constrains;
    private final TileType tileType;

    Tile(TileType tileType, Set<TileType> constrains, Color color, ImagePattern texture) {
        this.tileType = tileType;
        this.constrains = constrains;
        this.color = color;
        this.texture = texture;
    }

    public Color getColor() {
        return color;
    }

    public boolean isBrokeConstrain(Tile tile) {
        return !constrains.contains(tile.getTileType());
    }


    public TileType getTileType() {
        return tileType;
    }

    public void fillTextureShape(Shape shape) {
        ImagePattern imagePattern = texture;
        if (Objects.nonNull(imagePattern)) {
            shape.setFill(imagePattern);
        } else {
            shape.setFill(color);
        }
    }

    public void fillColorShape(Shape shape) {
        shape.setFill(color);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile t1) {
            return t1.getTileType() == this.getTileType();
        }
        return false;
    }

}
