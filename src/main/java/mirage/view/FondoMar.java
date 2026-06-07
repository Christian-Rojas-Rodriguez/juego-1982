package mirage.view;

import mirage.view.sprites.SpriteLoader;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Fondo: las Islas Malvinas vistas desde arriba (ambientación de la guerra).
 *
 * Dibuja una aproximación reconocible del archipiélago — las dos islas
 * principales, Gran Malvina (oeste) e Isla Soledad (este), separadas por el
 * Estrecho de San Carlos — a partir de un "land mask" (mapa de tierra/mar) que
 * se autotilea con las piezas de costa del pack Kenney.
 *
 * Es ESTÁTICO: se construye una sola vez en un buffer y cada frame solo se copia
 * (en vez de re-tilear cientos de tiles por frame). El mapa se escala
 * preservando su proporción y se centra, con mar alrededor, así se ve bien tanto
 * en la ventana vertical (Juego1982, 600×800) como en la del lobby (800×600).
 *
 * Sobre el mar se dibuja una capa oscura semitransparente para dar ambiente y
 * para que el HUD (puntaje / vidas, en blanco) se lea con contraste.
 *
 * Autotile de costa: a cada celda de tierra se le elige el tile según qué lados
 * son mar — bordes, esquinas convexas (volteando las superiores para las
 * inferiores) y esquinas cóncavas para las entradas de la costa.
 *
 * Tolerante a fallos: si falta el sprite de agua (headless / sin assets) pinta
 * un azul oscuro sólido.
 */
public class FondoMar {

    private static final int TS = 32;     // tamaño de tile en pantalla (px)

    /**
     * Mapa de tierra de las Malvinas (landscape ~1.4:1). '#' = tierra, '.' = mar.
     * Izquierda: Gran Malvina · Derecha: Isla Soledad · medio: Estrecho de San Carlos.
     */
    private static final String[] MASK = {
        "......................",
        "...###......#####.....",
        "..#####....########...",
        ".######...##########..",
        ".#######..##########..",
        ".#######..##########..",
        "..######..##########..",
        "..######...#########..",
        "..#####....#########..",
        "...####....#########..",
        "..#####....########...",
        "..######...#######....",
        "...####....######.....",
        "...###.....#####......",
        "....##.....####.......",
        "......................"
    };
    private static final int MW = MASK[0].length();
    private static final int MH = MASK.length;

    /** Buffer con las islas ya dibujadas (se construye una sola vez). */
    private PImage buffer;

    // Parámetros del mapeo mask→pantalla (calculados en construir()).
    private float scale, offC, offR;

    public void render(PApplet sk) {
        if (SpriteLoader.get("agua.png") == null) {  // sin assets: azul de respaldo
            sk.background(8, 22, 40);
            return;
        }
        if (buffer == null) buffer = construir(sk);

        sk.imageMode(PApplet.CORNER);
        sk.image(buffer, 0, 0);

        // Capa oscura para ambiente + contraste del HUD (puntaje/vidas en blanco).
        sk.noStroke();
        sk.fill(6, 18, 38, 120);
        sk.rect(0, 0, sk.width, sk.height);
    }

    /** Dibuja el mar con las islas una sola vez en un buffer del tamaño de la pantalla. */
    private PImage construir(PApplet sk) {
        int cols = PApplet.ceil(sk.width  / (float) TS);
        int rows = PApplet.ceil(sk.height / (float) TS);

        // Escala el mapa para que entre preservando proporción, y lo centra.
        scale = Math.min(cols / (float) MW, rows / (float) MH) * 0.92f;
        offC  = (cols - MW * scale) / 2f;
        offR  = (rows - MH * scale) / 2f;

        PGraphics g = sk.createGraphics(sk.width, sk.height);
        g.noSmooth();
        g.beginDraw();
        g.imageMode(PApplet.CORNER);
        g.background(40, 120, 170);   // azul por debajo del agua pálida

        PImage agua = SpriteLoader.get("agua.png");
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                g.image(agua, c * TS, r * TS, TS, TS);   // mar
                if (esTierra(c, r)) dibujarTileTierra(g, c, r);
            }
        }
        g.endDraw();
        return g;
    }

    /** ¿La celda de pantalla (c,r) cae sobre tierra del mapa de las Malvinas? */
    private boolean esTierra(int c, int r) {
        int mc = (int) Math.floor((c - offC) / scale);
        int mr = (int) Math.floor((r - offR) / scale);
        if (mr < 0 || mr >= MH || mc < 0 || mc >= MW) return false;
        return MASK[mr].charAt(mc) == '#';
    }

    /** Elige y dibuja el tile de costa correcto para una celda de tierra. */
    private void dibujarTileTierra(PGraphics g, int c, int r) {
        boolean n = !esTierra(c, r - 1);
        boolean s = !esTierra(c, r + 1);
        boolean e = !esTierra(c + 1, r);
        boolean w = !esTierra(c - 1, r);

        if (n && w) { dibujar(g, "isla-esq-sup-izq.png", c, r, false); return; }
        if (n && e) { dibujar(g, "isla-esq-sup-der.png", c, r, false); return; }
        if (s && w) { dibujar(g, "isla-esq-sup-izq.png", c, r, true);  return; } // inf-izq = flip
        if (s && e) { dibujar(g, "isla-esq-sup-der.png", c, r, true);  return; } // inf-der = flip
        if (n) { dibujar(g, "isla-borde-sup.png", c, r, false); return; }
        if (s) { dibujar(g, "isla-borde-sup.png", c, r, true);  return; } // borde inf = flip
        if (w) { dibujar(g, "isla-borde-izq.png", c, r, false); return; }
        if (e) { dibujar(g, "isla-borde-der.png", c, r, false); return; }

        // Interior: si una diagonal es mar, va una esquina cóncava (entrada de costa).
        if (!esTierra(c - 1, r - 1)) { dibujar(g, "isla-concava-sup-izq.png", c, r, false); return; }
        if (!esTierra(c + 1, r - 1)) { dibujar(g, "isla-concava-sup-der.png", c, r, false); return; }
        if (!esTierra(c - 1, r + 1)) { dibujar(g, "isla-concava-inf-izq.png", c, r, false); return; }
        if (!esTierra(c + 1, r + 1)) { dibujar(g, "isla-concava-inf-der.png", c, r, false); return; }

        dibujar(g, "isla-centro.png", c, r, false);
    }

    /** Dibuja un tile en la celda (c,r), opcionalmente volteado en vertical. */
    private void dibujar(PGraphics g, String nombre, int c, int r, boolean flipV) {
        PImage img = SpriteLoader.get(nombre);
        if (img == null) return;
        float x = c * TS, y = r * TS;
        if (!flipV) {
            g.image(img, x, y, TS, TS);
        } else {
            g.pushMatrix();
            g.translate(x, y + TS);
            g.scale(1, -1);
            g.image(img, 0, 0, TS, TS);
            g.popMatrix();
        }
    }
}
