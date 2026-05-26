import processing.core.PApplet;
import mirage.MirageModulo;

/**
 * Punto de entrada de Processing y del módulo Mirage en modo standalone.
 *
 * En producción el HOME team instancia MirageModulo y lo controla via ModuloJuego.
 * Esta clase existe para correr el módulo de forma independiente durante desarrollo.
 *
 * Responsabilidades:
 *   - Configurar el sketch (tamaño, framerate)
 *   - Delegar setup/draw/input a MirageModulo
 */
public class Juego1982 extends PApplet {

    private MirageModulo mirageModulo;

    public static void main(String[] args) {
        PApplet.main("Juego1982");
    }

    @Override
    public void settings() {
        size(600, 800);
        pixelDensity(displayDensity());
    }

    @Override
    public void setup() {
        frameRate(60);
        mirageModulo = new MirageModulo();
        mirageModulo.inicializarContexto(this);
        mirageModulo.iniciar();
    }

    @Override
    public void draw() {
        mirageModulo.actualizar();
        mirageModulo.dibujar();
    }

    @Override
    public void keyPressed() {
        mirageModulo.onKeyPressed(key, keyCode);
    }

    @Override
    public void keyReleased() {
        mirageModulo.onKeyReleased(key, keyCode);
    }
}