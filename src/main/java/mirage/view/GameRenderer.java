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
import processing.core.PImage;

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
            // El pixel-art nítido (noSmooth/nearest-neighbor) se configura en
            // settings() de los runners (Juego1982/HomeRunner): noSmooth() solo
            // tiene efecto ahí.
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

    /** Fondo: imagen completa cargada desde data/sprites/fondo.png. */
    private void dibujarFondo(PApplet sk) {
        PImage fondo = SpriteLoader.get("fondo.png");
        if (fondo == null) {
            sk.background(8, 22, 40);
            return;
        }

        float escala = Math.max(sk.width / (float) fondo.width, sk.height / (float) fondo.height);
        float ancho = fondo.width * escala;
        float alto = fondo.height * escala;
        float x = (sk.width - ancho) / 2f;
        float y = (sk.height - alto) / 2f;

        sk.imageMode(PApplet.CORNER);
        sk.image(fondo, x, y, ancho, alto);
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
