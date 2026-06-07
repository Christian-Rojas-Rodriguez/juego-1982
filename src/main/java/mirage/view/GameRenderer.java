// ============================================================
// GameRenderer — Renderiza el estado del Model en pantalla
// ============================================================
// GRASP : High Cohesion (solo dibuja, nunca modifica estado)
// ============================================================
// Qué implementar:
//   - render(): dibujar fondo, luego entidades, luego HUD
//               Orden: fondo → proyectiles → enemigos → mirage → HUD
//   - dibujarFondo(sketch): fondo negro + estrellas (efecto parallax opcional)
//   - dibujarHUD(sketch, mirage): mostrar vidas y puntuación
//   - setPantalla(): cambiar la pantalla activa (para Game Over, etc.)
// REGLA: este archivo NO modifica ningún campo del Model.
//        Solo llama métodos de lectura (get...) y render(sketch).
// ============================================================

package mirage.view;

import processing.core.PApplet;
import mirage.model.efectos.Explosion;
import mirage.model.entidades.Mirage;
import mirage.model.entidades.Proyectil;
import mirage.model.entidades.enemigos.Enemigo;
import mirage.view.pantallas.Pantalla;
import mirage.view.sprites.SpriteLoader;

import java.util.List;

public class GameRenderer {

    // --- Atributos ---
    private Pantalla pantallaActual;

    /** Precarga de sprites diferida al primer render (necesita el PApplet vivo). */
    private boolean spritesListos;

    // --- Renderizado principal ---
    public void render(Mirage mirage, List<Enemigo> enemigos, List<Proyectil> proyectiles,
                       List<Explosion> efectos, PApplet sketch) {
        if (!spritesListos) {
            sketch.noSmooth();                    // nearest-neighbor → pixel art nítido
            SpriteLoader.precargarTodos(sketch);
            spritesListos = true;
        }
        dibujarFondo(sketch);
        for (Proyectil p : proyectiles) p.render(sketch);
        for (Enemigo e : enemigos) e.render(sketch);
        mirage.render(sketch);
        for (Explosion ex : efectos) ex.render(sketch);   // efectos por encima de las naves
        dibujarHUD(sketch, mirage);
        if (pantallaActual != null) pantallaActual.render(sketch);
    }

    /**
     * Fondo procedural: mar del Atlántico Sur visto desde arriba.
     * Olas y espuma que se desplazan hacia abajo dan la sensación de avance.
     * Determinista por frameCount (mismo patrón en cada frame, sin azar).
     */
    private void dibujarFondo(PApplet sk) {
        sk.background(10, 30, 58);   // mar profundo (navy)
        sk.noStroke();

        float fc = sk.frameCount;

        // Capa 1 (parallax lento): franjas anchas de azul medio.
        sk.fill(18, 44, 78);
        int pasoF = 70;
        float despF = (fc * 0.6f) % pasoF;
        for (float y = -pasoF + despF; y < sk.height; y += pasoF) {
            sk.rect(0, y, sk.width, 26);
        }

        // Capa 2 (parallax rápido): espuma — guiones claros dispersos por fila.
        int pasoE = 38;
        float despE = (fc * 1.6f) % pasoE;
        int fila = 0;
        for (float y = -pasoE + despE; y < sk.height; y += pasoE, fila++) {
            sk.fill(150, 185, 210, 150);
            // Posiciones pseudo-aleatorias pero deterministas (hash de la fila).
            for (int k = 0; k < 5; k++) {
                int h = (fila * 131 + k * 977) & 0x7fffffff;
                float fx = (h % 100) / 100f * sk.width;
                float fw = 6 + (h % 3) * 4;
                sk.rect(fx, y, fw, 2);
            }
        }
    }

    private void dibujarHUD(PApplet sketch, Mirage mirage) {
        sketch.fill(255);
        sketch.textSize(12);
        sketch.textAlign(PApplet.LEFT, PApplet.TOP);
        sketch.text("Puntos: " + mirage.getPuntuacion(), 10, 10);
        sketch.textAlign(PApplet.RIGHT, PApplet.TOP);
        sketch.text("Vidas: " + mirage.getVidas(), sketch.width - 10, 10);
    }

    // --- Cambiar pantalla superpuesta ---
    public void setPantalla(Pantalla pantalla) {
        this.pantallaActual = pantalla;
    }
}
