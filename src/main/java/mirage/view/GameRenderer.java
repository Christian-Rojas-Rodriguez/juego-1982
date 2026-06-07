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
import mirage.model.entidades.Mirage;
import mirage.model.entidades.Proyectil;
import mirage.model.entidades.enemigos.Enemigo;
import mirage.view.pantallas.Pantalla;

import java.util.List;

public class GameRenderer {

    // --- Atributos ---
    private Pantalla pantallaActual;

    // --- Renderizado principal ---
    public void render(Mirage mirage, List<Enemigo> enemigos, List<Proyectil> proyectiles, PApplet sketch) {
        dibujarFondo(sketch);
        for (Proyectil p : proyectiles) p.render(sketch);
        for (Enemigo e : enemigos) e.render(sketch);
        mirage.render(sketch);
        dibujarHUD(sketch, mirage);
        if (pantallaActual != null) pantallaActual.render(sketch);
    }

    private void dibujarFondo(PApplet sketch) {
        sketch.background(10, 10, 30);  // azul oscuro de noche
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

    // public Pantalla getPantalla(){
    //     return this.pantallaActual;
    // }
}
