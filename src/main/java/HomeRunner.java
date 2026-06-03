import lobby.HomeJuego;
import mirage.ModuloMirage;
import processing.core.PApplet;

/**
 * Runner del LOBBY (HOME) con el módulo Mirage registrado.
 *
 * Réplica en Java de Game1982.pde del repo del grupo HOME
 * (marcosbenson/Modulo_1_Algoritmos_1 @ processing-ide), para correr el lobby
 * completo desde IntelliJ y seleccionar Mirage en la pantalla de selección.
 *
 * Flujo: INICIO → (START) → SELECCION → elegir "Mirage" → JUEGO.
 * En JUEGO: ESC pausa/reanuda, Q finaliza (lo intercepta el lobby).
 *
 * Para correr el módulo Mirage SOLO (sin lobby), usar Juego1982.
 */
public class HomeRunner extends PApplet {

    private HomeJuego home;

    public static void main(String[] args) {
        PApplet.main("HomeRunner");
    }

    @Override
    public void settings() {
        size(800, 600);
    }

    @Override
    public void setup() {
        frameRate(60);
        try {
            textFont(createFont("PressStart2P-Regular.ttf", 12, true));
        } catch (Exception e) {
            textFont(createFont("Monospaced", 12, true));
        }
        home = new HomeJuego(this);
        home.iniciarHome();
        home.registrarModulo(new ModuloMirage());   // ← Mirage aparece en Selección
        // Para integrar más módulos del grupo, registrarlos aquí también.
    }

    @Override
    public void draw() {
        home.dibujar();
    }

    @Override
    public void mousePressed() {
        home.manejarClick(mouseX, mouseY);
    }

    @Override
    public void keyPressed() {
        home.manejarTecla(keyCode, key);
    }
}
