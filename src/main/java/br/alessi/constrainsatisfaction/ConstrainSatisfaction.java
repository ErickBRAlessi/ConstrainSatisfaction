package br.alessi.constrainsatisfaction;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.*;


/**
 * JavaFX App
 */
public class ConstrainSatisfaction extends Application {
    private static final int SCENE_SIZE_X = 1000;
    private static final int SCENE_SIZE_Y = 1000;
    private static final int TILE_SIZE = 15;
    private static final double BORDER_SIZE = 0.0D;
    private static final int NUMBER_OF_TILES_Y = SCENE_SIZE_Y / TILE_SIZE;
    private static final int NUMBER_OF_TILES_X = SCENE_SIZE_X / TILE_SIZE;
    private static final int NUMBER_OF_TILES = NUMBER_OF_TILES_Y * NUMBER_OF_TILES_X;
    private static final int RANGE = 1;
    private static final Random RANDOM = new Random();
    private static final boolean RECTANGLE = true;
    private static final int INITIAL_RANDOM_CYCLES = 5;
    private static int randomCycles = INITIAL_RANDOM_CYCLES;
    private static final int INITIAL_SYSTEMATIC_CYCLES = 5;
    private static int systematicCycles = INITIAL_SYSTEMATIC_CYCLES;
    private AnimationTimer frameTimer;
    private final TileFactory tileFactory = new TileFactory();
    private final Tile[][] map = new Tile[NUMBER_OF_TILES_X][NUMBER_OF_TILES_Y];

    @Override
    public void start(Stage stage) throws InterruptedException {
        Group root = new Group();
        Scene scene = new Scene(root, SCENE_SIZE_X, SCENE_SIZE_Y, Color.BLACK);
        stage.setTitle("Constrain Satisfaction");
        stage.setScene(scene);
        stage.show();
        addEventHandler(root, scene);
        fillMapWithUndeclared();
        addMapRefresh(root);
    }

    private void addEventHandler(Group root, Scene scene) {
        EventHandler<KeyEvent> restart = event -> {
            if (event.getCode().isWhitespaceKey()) {
                fillMapWithUndeclared();
                randomCycles = INITIAL_RANDOM_CYCLES;
                systematicCycles = INITIAL_SYSTEMATIC_CYCLES;
                frameTimer.start();

            }
        };
        EventHandler<MouseEvent> destroy = event -> {
            destroyMapRegion((int) event.getX() / TILE_SIZE, (int) event.getY() / TILE_SIZE);
            drawMap(root);
            randomCycles = INITIAL_RANDOM_CYCLES;
            systematicCycles = INITIAL_SYSTEMATIC_CYCLES;
            frameTimer.start();
        };

        scene.addEventHandler(KeyEvent.ANY, restart);
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, destroy);
    }

    private void fillMapWithUndeclared() {
        for (int y = 0; y < NUMBER_OF_TILES_Y; y++) {
            for (int x = 0; x < NUMBER_OF_TILES_X; x++) {
                Tile tile = tileFactory.get(TileType.UNDECLARED);
                map[x][y] = tile;
            }
        }
    }

    private void destroyMapRegion(int x, int y) {
        int range = (int) Math.sqrt(NUMBER_OF_TILES) / 10;
        for (int distanceX = -range; distanceX <= range; distanceX++) {
            for (int distanceY = -range; distanceY <= range; distanceY++) {
                var tx = (distanceX + x + NUMBER_OF_TILES_X) % NUMBER_OF_TILES_X;
                var ty = (distanceY + y + NUMBER_OF_TILES_Y) % NUMBER_OF_TILES_Y;
                map[tx][ty] = tileFactory.get(TileType.UNDECLARED);
            }
        }
        randomCycles = INITIAL_RANDOM_CYCLES;
        systematicCycles = INITIAL_SYSTEMATIC_CYCLES;
    }

    private void addMapRefresh(Group root) {
        frameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long start = System.currentTimeMillis();
                process();
                drawMap(root);
                long time = System.currentTimeMillis() - start;
                System.out.println(time * 1000);
                if (randomCycles + systematicCycles < 0) {
                    this.stop();
                }
            }
        };
        frameTimer.start();
    }


    private void process() {
        leastConflicts();
    }

    private void leastConflicts() {
        while (randomCycles >= 0) {
            runRandomCycle();
            randomCycles--;
        }
        while (systematicCycles >= 0) {
            runSystematicCycle();
            systematicCycles--;
        }
    }

    private void runRandomCycle() {
        int x, y;
        for (int j = 0; j < NUMBER_OF_TILES; j++) {
            x = RANDOM.nextInt(0, NUMBER_OF_TILES_X);
            y = RANDOM.nextInt(0, NUMBER_OF_TILES_Y);
            tryFindBestMatch(x, y);
        }
    }

    private void runSystematicCycle() {
        for (int y = 0; y < NUMBER_OF_TILES_Y; y++) {
            for (int x = 0; x < NUMBER_OF_TILES_X; x++)
                tryFindBestMatch(x, y);
        }
    }


    private void tryFindBestMatch(int x, int y) {
        int tries = TileType.values().length;
        int conflicts;
        conflicts = checkConflicts(x, y);
        if (conflicts > 0 || map[x][y].getTileType() == TileType.UNDECLARED) {
            TileType bestTerrain = map[x][y].getTileType();
            int leastConflicts = Integer.MAX_VALUE;
            int tempConflicts;
            for (int j = 0; j < tries; j++) {
                map[x][y] = tileFactory.get(TileType.getRandomNotUndeclared());
                tempConflicts = checkConflicts(x, y);
                if (tempConflicts < leastConflicts) {
                    bestTerrain = map[x][y].getTileType();
                    leastConflicts = tempConflicts;
                }
            }
            map[x][y] = tileFactory.get(bestTerrain);
        }
    }

    private int checkConflicts(int x, int y) {
        Tile tile = map[x][y];
        int conflicts = 0;
        int range = RANGE;
        for (int distanceX = -range; distanceX <= range; distanceX++) {
            for (int distanceY = -range; distanceY <= range; distanceY++) {
                var tx = (distanceX + x + NUMBER_OF_TILES_X) % NUMBER_OF_TILES_X;
                var ty = (distanceY + y + NUMBER_OF_TILES_Y) % NUMBER_OF_TILES_Y;
                conflicts += tile.isBrokeConstrain(map[tx][ty]) ? 1 : 0;
            }
        }
        return conflicts;
    }

    private void drawMap(Group root) {
        for (int y = 0; y < NUMBER_OF_TILES_Y; y++) {
            for (int x = 0; x < NUMBER_OF_TILES_X; x++) {
                Shape shape = null;
                if (RECTANGLE) {
                    shape = new Rectangle(TILE_SIZE * x, TILE_SIZE * y, (TILE_SIZE - BORDER_SIZE), (TILE_SIZE - BORDER_SIZE));
                } else {
                    shape = new Circle(TILE_SIZE * x, TILE_SIZE * y, (TILE_SIZE - BORDER_SIZE));
                }
                root.getChildren().removeAll();
                map[x][y].fillTextureShape(shape);
                root.getChildren().add(shape);
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }

}