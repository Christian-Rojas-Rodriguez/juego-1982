# Sprites — Kenney "Pixel Shmup"

Gráficos del módulo Mirage tomados del pack **Kenney — Pixel Shmup**.

- **Fuente:** https://kenney.nl/assets/pixel-shmup
- **Licencia:** CC0 1.0 Universal (dominio público — uso libre, atribución no requerida pero agradecida).
- **Licencia completa:** ver `LICENSE-kenney.txt` en la raíz del repo.

## Mapeo entidad → sprite del pack

| Archivo en el juego | Origen en el pack | Tamaño original | Entidad |
|---------------------|-------------------|-----------------|---------|
| `player.png`        | `Ships/ship_0012.png` | 32×32 | Nave del jugador (Mirage) — **gris**, apunta arriba. Se eligió gris a propósito para reservar los colores a los enemigos. |
| `enemy-harrier.png` | `Ships/ship_0001.png` | 32×32 | Enemigo Harrier (se voltea en vertical al renderizar para apuntar hacia el jugador) |
| `bullet.png`        | `Tiles/tile_0000.png` | 16×16 | Proyectil del Mirage |

### Fondo: las Islas Malvinas (Guerra de Malvinas)

`mirage.view.FondoMar` dibuja una aproximación reconocible del archipiélago — las
dos islas principales (**Gran Malvina** al oeste, **Isla Soledad** al este) separadas
por el **Estrecho de San Carlos** — a partir de un *land mask* (mapa de tierra/mar
embebido en el código) que se autotilea con las piezas de costa del pack. El mapa se
escala preservando su proporción y se centra, así se ve bien tanto en la ventana
vertical (Juego1982, 600×800) como en la del lobby (800×600).

El fondo es **estático**: se construye una sola vez en un buffer y cada frame solo se
copia (en vez de re-tilear cientos de tiles por frame). Lleva una capa oscura
semitransparente encima para ambiente y para que el HUD (puntaje/vidas, en blanco) se
lea con contraste.

Autotile de costa — a cada celda de tierra se le elige el tile según qué lados son mar.
Los bordes/esquinas inferiores se obtienen volteando en vertical los superiores (la
costa es simétrica), por eso no hacen falta tiles propios para ellos.

| Archivo | Origen | Rol en el autotile |
|---------|--------|--------------------|
| `agua.png`                 | `Tiles/tile_0042.png` | Mar (relleno) |
| `isla-centro.png`          | `Tiles/tile_0050.png` | Interior de la isla |
| `isla-borde-sup.png`       | `Tiles/tile_0038.png` | Borde superior (y, volteado, el inferior) |
| `isla-borde-izq.png`       | `Tiles/tile_0049.png` | Borde izquierdo |
| `isla-borde-der.png`       | `Tiles/tile_0051.png` | Borde derecho |
| `isla-esq-sup-izq.png`     | `Tiles/tile_0037.png` | Esquina sup-izq (y, volteada, la inf-izq) |
| `isla-esq-sup-der.png`     | `Tiles/tile_0039.png` | Esquina sup-der (y, volteada, la inf-der) |
| `isla-concava-sup-izq.png` | `Tiles/tile_0053.png` | Esquina cóncava (entrada de costa) sup-izq |
| `isla-concava-sup-der.png` | `Tiles/tile_0052.png` | Esquina cóncava sup-der |
| `isla-concava-inf-izq.png` | `Tiles/tile_0041.png` | Esquina cóncava inf-izq |
| `isla-concava-inf-der.png` | `Tiles/tile_0040.png` | Esquina cóncava inf-der |

## Explosión (procedural, sin sprite)

Al destruir un enemigo, `EstadoJugando` crea una `mirage.model.efectos.Explosion`
en la posición del enemigo — ráfaga de partículas cuadradas que se expande y se
desvanece (~22 frames). `GameController` la posee; se descarta al terminar.

Los sprites se cargan con `mirage.view.sprites.SpriteLoader` desde `data/sprites/`
(Processing resuelve `loadImage("sprites/...")` contra la carpeta `data/`).
Renderizado pixel-perfect: `noSmooth()` (nearest-neighbor) en el primer frame.

Si un sprite falta, las entidades dibujan una forma de respaldo (triángulo/rectángulo),
lo que mantiene los tests headless funcionando sin assets.
