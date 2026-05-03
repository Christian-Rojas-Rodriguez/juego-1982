import processing.core.PApplet;
import game.GameManager;

public class Juego1982 extends PApplet {

    private GameManager gameManager;

    // --- Processing entry point ---
    public static void main(String[] args) {
        PApplet.main("Juego1982");
    }

    // --- Configuración inicial (tamaño, framerate, etc.) ---
    @Override
    public void settings() {
        size(600, 600);
        pixelDensity(displayDensity());
    }

    // --- Se ejecuta una sola vez al iniciar ---
    @Override
    public void setup() {
        frameRate(60);
        gameManager = new GameManager(this);
        gameManager.init();
    }

    // --- Se ejecuta 60 veces por segundo ---
    @Override
    public void draw() {
        gameManager.update();
        gameManager.render();
    }

    // --- Input ---
    @Override
    public void keyPressed() {
        gameManager.onKeyPressed(key, keyCode);
    }

    @Override
    public void keyReleased() {
        gameManager.onKeyReleased(key, keyCode);
    }
}
