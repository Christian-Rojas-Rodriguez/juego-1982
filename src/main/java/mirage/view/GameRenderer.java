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
        // TODO: dibujarFondo(sketch)
        // TODO: for proyectil : proyectiles → proyectil.render(sketch)
        // TODO: for enemigo   : enemigos   → enemigo.render(sketch)
        // TODO: mirage.render(sketch)
        // TODO: dibujarHUD(sketch, mirage)
        // TODO: si pantallaActual != null → pantallaActual.render(sketch)
    }

    private void dibujarFondo(PApplet sketch) {
        // TODO: sketch.background(10, 10, 30)  // azul oscuro de noche
        // TODO: dibujar estrellas estáticas o con efecto de movimiento
    }

    private void dibujarHUD(PApplet sketch, Mirage mirage) {
        // TODO: mostrar puntuación arriba a la izquierda
        // TODO: mostrar vidas (íconos o número) arriba a la derecha
    }

    // --- Cambiar pantalla superpuesta ---
    public void setPantalla(Pantalla pantalla) {
        // TODO: this.pantallaActual = pantalla
    }
}
