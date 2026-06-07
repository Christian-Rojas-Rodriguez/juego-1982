package mirage.view;

import mirage.view.sprites.SpriteLoader;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Fondo: el mar del Atlántico Sur visto desde arriba, con islas dispersas
 * (ambientación Guerra de Malvinas). Se desplaza hacia abajo para dar la
 * sensación de que el Mirage avanza hacia el norte.
 *
 * Las islas se colocan de forma DETERMINISTA (sin estado ni azar guardado):
 * el mundo se divide en bloques de BW×BH tiles y un hash de las coordenadas
 * del bloque decide si hay isla, su tamaño y su posición dentro del bloque.
 * Así el mismo tramo de mar se ve igual siempre que vuelve a pantalla, y no
 * hace falta almacenar nada.
 *
 * Las islas se dibujan con autotile de costa (Kenney): esquinas y bordes
 * superiores reales; los bordes inferiores se obtienen volteando los
 * superiores en vertical (la costa es simétrica).
 *
 * Tolerante a fallos: si falta el sprite de agua (modo headless / sin assets)
 * pinta un azul sólido y no dibuja islas.
 */
public class FondoMar {

    private static final int TS = 32;     // tamaño de tile en pantalla (px)
    private static final int BW = 5;      // ancho de bloque (tiles)
    private static final int BH = 6;      // alto de bloque (tiles)
    private static final float VEL = 0.7f; // px por frame que baja el mar

    public void render(PApplet sk) {
        PImage agua = SpriteLoader.get("agua.png");
        if (agua == null) {                // sin assets: azul de respaldo
            sk.background(20, 80, 130);
            return;
        }
        sk.background(40, 120, 170);       // azul por debajo del agua palida
        sk.imageMode(PApplet.CORNER);

        float scrollPx = sk.frameCount * VEL;
        int scrollTiles = PApplet.floor(scrollPx / TS);
        float offY = scrollPx - scrollTiles * TS;   // desfase sub-tile (0..TS)

        int cols = PApplet.ceil(sk.width / (float) TS) + 1;
        int rows = PApplet.ceil(sk.height / (float) TS) + 2;

        for (int sr = -1; sr < rows; sr++) {
            // worldRow decrece con el tiempo → el contenido entra por arriba y baja.
            int worldRow = sr - scrollTiles;
            float y = sr * TS - offY;
            for (int sc = 0; sc < cols; sc++) {
                float x = sc * TS;
                sk.image(agua, x, y, TS, TS);      // mar
                dibujarIsla(sk, sc, worldRow, x, y); // isla encima (si corresponde)
            }
        }
    }

    /** Dibuja el tile de isla correspondiente a la celda de mundo (wx,wy), o nada si es mar. */
    private void dibujarIsla(PApplet sk, int wx, int wy, float x, float y) {
        int bc = Math.floorDiv(wx, BW);
        int br = Math.floorDiv(wy, BH);
        int h  = hash(bc, br);

        if (h % 100 >= 65) return;          // ~65% de los bloques tienen isla

        int w  = 2 + (h >> 3) % 3;          // ancho 2..4 tiles
        int hh = 2 + (h >> 7) % 3;          // alto  2..4 tiles
        int ox = (h >> 11) % (BW - w + 1);  // posición dentro del bloque (cabe entero)
        int oy = (h >> 15) % (BH - hh + 1);

        int ix0 = bc * BW + ox;             // esquina sup-izq de la isla (mundo)
        int iy0 = br * BH + oy;
        int lx = wx - ix0;                  // posición local dentro de la isla
        int ly = wy - iy0;
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
        if (!flipV) {
            sk.image(img, x, y, TS, TS);
        } else {
            sk.pushMatrix();
            sk.translate(x, y + TS);
            sk.scale(1, -1);
            sk.image(img, 0, 0, TS, TS);
            sk.popMatrix();
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
