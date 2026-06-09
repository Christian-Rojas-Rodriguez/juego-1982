package mirage.view;

import processing.core.PApplet;
import mirage.model.efectos.Explosion;
import mirage.model.entidades.Mirage;
import mirage.model.entidades.Proyectil;
import mirage.model.entidades.enemigos.Enemigo;
import mirage.view.pantallas.Pantalla;
import mirage.view.pantallas.PantallaJuego;
import mirage.view.sprites.SpriteLoader;

import java.util.List;

public class GameRenderer {

    /** Fondo estático — se dibuja primero. */
    private final Pantalla pantallaFondo = new PantallaJuego();

    /** Overlay activo (Game Over, etc.) — se dibuja al final. */
    private Pantalla pantallaActual;

    /** Precarga de sprites diferida al primer render (necesita el PApplet vivo). */
    private boolean spritesListos;

    public void render(Mirage mirage, List<Enemigo> enemigos, List<Proyectil> proyectiles,
                       List<Explosion> efectos, PApplet sketch) {
        if (!spritesListos) {
            SpriteLoader.precargarTodos(sketch);
            spritesListos = true;
        }
        pantallaFondo.render(sketch);
        for (Proyectil p : proyectiles) p.render(sketch);
        for (Enemigo e : enemigos) e.render(sketch);
        mirage.render(sketch);
        for (Explosion ex : efectos) ex.render(sketch);
        dibujarHUD(sketch, mirage);
        if (pantallaActual != null) pantallaActual.render(sketch);
    }

    private void dibujarHUD(PApplet sketch, Mirage mirage) {
        sketch.fill(255);
        sketch.textSize(12);
        sketch.textAlign(PApplet.LEFT, PApplet.TOP);
        sketch.text("Puntos: " + mirage.getPuntuacion(), 10, 10);
        sketch.textAlign(PApplet.RIGHT, PApplet.TOP);
        sketch.text("Vidas: " + mirage.getVidas(), sketch.width - 10, 10);
    }

    public void setPantalla(Pantalla pantalla) {
        this.pantallaActual = pantalla;
    }
}
