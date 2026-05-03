package game.levels;

import game.entities.enemies.Enemy;
import game.entities.enemies.EnemyFactory;
import processing.core.PApplet;

import java.util.ArrayList;

public class EnemySpawner {

    private final PApplet sketch;
    private final ArrayList<Enemy> enemies;
    private final int intervaloFrames;
    private int frameCounter;

    public EnemySpawner(PApplet sketch, ArrayList<Enemy> enemies, int intervaloFrames) {
        this.sketch          = sketch;
        this.enemies         = enemies;
        this.intervaloFrames = intervaloFrames;
        this.frameCounter    = 0;
    }

    public void update() {
        frameCounter++;
        if (frameCounter >= intervaloFrames) {
            spawnEnemigo();
            frameCounter = 0;
        }
    }

    private void spawnEnemigo() {
        float x = sketch.random(15, sketch.width - 15);
        enemies.add(EnemyFactory.crear(EnemyFactory.Tipo.BASICO, sketch, x, -20));
    }
}