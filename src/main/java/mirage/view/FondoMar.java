package mirage.view;

import mirage.view.sprites.SpriteLoader;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Fondo: el mar del Atlántico Sur con islas dispersas (ambientación Guerra de
 * Malvinas). Es ESTÁTICO: se construye una sola vez en un buffer y cada frame
 * solo se copia ese buffer (una operación), en vez de re-tilear ~200 tiles por
 * frame. Eso mantiene el costo del fondo parejo con el del resto del juego.
 *
 * Sobre el mar se dibuja una capa oscura semitransparente para dar ambiente y,
 * sobre todo, para que el HUD (puntaje / vidas, en blanco) se lea con contraste.
 *
 * Las islas se colocan de forma DETERMINISTA: el mundo se divide en bloques de
 * BW×BH tiles y un hash de las coordenadas del bloque decide si hay isla, su
 * tamaño y su posición. Autotile de costa (Kenney): esquinas y bordes superiores
 * reales; los inferiores se obtienen volteando los superiores en vertical.
 *
 * Tolerante a fallos: si falta el sprite de agua (modo headless / sin assets)
 * pinta un azul oscuro sólido y no dibuja islas.
 */
public class FondoMar {

    private static final int TS = 32;     // tamaño de tile en pantalla (px)
    private static final int BW = 5;      // ancho de bloque (tiles)
    private static final int BH = 6;      // alto de bloque (tiles)

    /** Buffer con el mar+islas ya dibujado (se construye una sola vez). */
    private PImage buffer;

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

    /** Dibuja el mar con islas una sola vez en un buffer del tamaño de la pantalla. */
    private PImage construir(PApplet sk) {
        PGraphics g = sk.createGraphics(sk.width, sk.height);
        g.noSmooth();
        g.beginDraw();
        g.imageMode(PApplet.CORNER);
        g.background(40, 120, 170);   // azul por debajo del agua pálida

        PImage agua = SpriteLoader.get("agua.png");
        int cols = PApplet.ceil(sk.width / (float) TS);
        int rows = PApplet.ceil(sk.height / (float) TS);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                g.image(agua, c * TS, r * TS, TS, TS);   // mar
                dibujarIsla(g, c, r);                    // isla encima (si corresponde)
            }
        }
        g.endDraw();
        return g;
    }

    /** Dibuja el tile de isla correspondiente a la celda (cx,cy), o nada si es mar. */
    private void dibujarIsla(PGraphics g, int cx, int cy) {
        int bc = Math.floorDiv(cx, BW);
        int br = Math.floorDiv(cy, BH);
        int h  = hash(bc, br);

        if (h % 100 >= 65) return;          // ~65% de los bloques tienen isla

        int w  = 2 + (h >> 3) % 3;          // ancho 2..4 tiles
        int hh = 2 + (h >> 7) % 3;          // alto  2..4 tiles
        int ox = (h >> 11) % (BW - w + 1);  // posición dentro del bloque (cabe entero)
        int oy = (h >> 15) % (BH - hh + 1);

        int ix0 = bc * BW + ox;             // esquina sup-izq de la isla
        int iy0 = br * BH + oy;
        int lx = cx - ix0;                  // posición local dentro de la isla
        int ly = cy - iy0;
        if (lx < 0 || lx >= w || ly < 0 || ly >= hh) return; // fuera de la isla → mar

        boolean top = ly == 0, bot = ly == hh - 1, lft = lx == 0, rgt = lx == w - 1;
        String tile;
        boolean flipV = false;
        if (top && lft)      tile = "isla-esq-sup-izq.png";
        else if (top && rgt) tile = "isla-esq-sup-der.png";
        else if (bot && lft) { tile = "isla-esq-sup-izq.png"; flipV = true; }
        else if (bot && rgt) { tile = "isla-esq-sup-der.png"; flipV = true; }
        else if (top)        tile = "isla-borde-sup.png";
        else if (bot)        { tile = "isla-borde-sup.png"; flipV = true; }
        else if (lft)        tile = "isla-borde-izq.png";
        else if (rgt)        tile = "isla-borde-der.png";
        else                 tile = "isla-centro.png";

        PImage img = SpriteLoader.get(tile);
        if (img == null) return;
        float x = cx * TS, y = cy * TS;
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

    /** Hash entero determinista de coordenadas de bloque (mezcla tipo xorshift). */
    private int hash(int x, int y) {
        int h = x * 73856093 ^ y * 19349663;
        h ^= (h >>> 13);
        h *= 1274126177;
        h ^= (h >>> 16);
        return h & 0x7fffffff;
    }
}
