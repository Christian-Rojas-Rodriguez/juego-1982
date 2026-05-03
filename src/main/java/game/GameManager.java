package game;

import game.entities.enemies.Enemy;
import game.entities.player.Player;
import game.levels.EnemySpawner;
import processing.core.PApplet;

import java.util.ArrayList;

public class GameManager {

    private final PApplet sketch;
    private Player player;
    private ArrayList<Enemy> enemies;
    private EnemySpawner spawner;

    public GameManager(PApplet sketch) {
        this.sketch = sketch;
    }

    public void init() {
        player  = new Player(sketch);
        enemies = new ArrayList<>();
        spawner = new EnemySpawner(sketch, enemies, 10); // un enemigo cada 90 frames (1.5s)
    }

    public void update() {
        player.update();
        spawner.update();

        for (Enemy e : enemies) {
            e.update();
        }

        enemies.removeIf(e -> !e.estaViva() || e.getY() > sketch.height + 20);
    }

    public void render() {
        drawBackground();
        player.render();

        for (Enemy e : enemies) {
            e.render();
        }
    }

    private void drawBackground() {
        sketch.background(0);
        sketch.fill(255, 255, 255, 200);
        sketch.noStroke();
    }

    public void onKeyPressed(char key, int keyCode) {
        player.onKeyPressed(keyCode);
    }

    public void onKeyReleased(char key, int keyCode) {
        player.onKeyReleased(keyCode);
    }
}