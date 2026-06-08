import contracts.ContextoJuego;
import contracts.EstadoInvalidoException;
import mirage.ModuloMirage;
import processing.core.PApplet;

/**
 * Runner STANDALONE del módulo Mirage (sin lobby), para desarrollo.
 *
 * En producción el HOME instancia ModuloMirage y lo controla vía
 * contracts.ModuloJuego. Esta clase emula al lobby para correr el módulo solo.
 *
 * Para correr el LOBBY completo y seleccionar Mirage, usar HomeRunner.
 *
 * Emulación de teclas del lobby (que en producción intercepta el HOME):
 *   ESC → pausar / reanudar (toggle)
 *   Q   → finalizar
 * El resto del input se reenvía al módulo (movimiento, disparo).
 */
public class Juego1982 extends PApplet {

    private static final int ANCHO = 600;
    private static final int ALTO  = 800;

    private ModuloMirage modulo;
    private boolean pausado = false;

    public static void main(String[] args) {
        PApplet.main("Juego1982");
    }

    @Override
    public void settings() {
        size(ANCHO, ALTO);
        pixelDensity(displayDensity());
        noSmooth();   // pixel-art nitido (nearest-neighbor); solo aplica en settings()
    }

    @Override
    public void setup() {
        frameRate(60);
        modulo = new ModuloMirage();
        modulo.inicializarContexto(new ContextoJuego("Jugador", width, height));
        try {
            modulo.iniciar();
        } catch (EstadoInvalidoException e) {
            System.err.println("No se pudo iniciar el modulo: " + e.getMessage());
        }
    }

    @Override
    public void draw() {
        modulo.actualizar(this);
        modulo.dibujar(this);
    }

    @Override
    public void keyPressed() {
        try {
            if (keyCode == ESC) {
                key = 0; // evita que Processing cierre la ventana con ESC
                if (pausado) { modulo.reanudar(); } else { modulo.pausar(); }
                pausado = !pausado;
                return;
            }
            if (key == 'q' || key == 'Q') {
                modulo.finalizar();
                return;
            }
        } catch (EstadoInvalidoException e) {
            System.err.println("Transicion invalida: " + e.getMessage());
            return;
        }
        modulo.onKeyPressed(key, keyCode);
    }

    @Override
    public void keyReleased() {
        modulo.onKeyReleased(key, keyCode);
    }
}
