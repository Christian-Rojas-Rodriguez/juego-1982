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

## Efectos y fondo (procedurales, sin sprite)

- **Fondo:** mar del Atlántico Sur visto desde arriba, dibujado en `GameRenderer.dibujarFondo()` con olas/espuma que se desplazan hacia abajo (sensación de avance). Determinista por `frameCount`.
- **Explosión:** al destruir un enemigo, `EstadoJugando` crea una `mirage.model.efectos.Explosion` en la posición del enemigo — ráfaga de partículas cuadradas que se expande y se desvanece (~22 frames). `GameController` la posee; se descarta al terminar.

Los sprites se cargan con `mirage.view.sprites.SpriteLoader` desde `data/sprites/`
(Processing resuelve `loadImage("sprites/...")` contra la carpeta `data/`).
Renderizado pixel-perfect: `noSmooth()` (nearest-neighbor) en el primer frame.

Si un sprite falta, las entidades dibujan una forma de respaldo (triángulo/rectángulo),
lo que mantiene los tests headless funcionando sin assets.
