# Diagramas de Estado — MVP 1

> Vista de comportamiento dinámico del sistema en la primera versión entregable

---

## 1. Estado del Juego

```mermaid
stateDiagram-v2
    [*] --> JUGANDO : ModuloJuego.iniciar()

    JUGANDO --> PAUSADO : tecla P
    PAUSADO --> JUGANDO : tecla P

    JUGANDO --> GAME_OVER : vidasMirage == 0
    GAME_OVER --> JUGANDO : tecla R (reiniciar)
    GAME_OVER --> [*] : tecla ESC → notificar(FINALIZADO)

    note right of JUGANDO
        actualizar(): mover entidades, colisiones,
        spawner, registrar heatmap
    end note
    note right of PAUSADO
        Solo renderiza. No actualiza física.
    end note
    note right of GAME_OVER
        Muestra puntaje. notificar(FINALIZADO).
        getEstadisticasGenerales() disponible.
    end note
```

| Estado | Clase Java |
|--------|-----------|
| JUGANDO | `EstadoJugando` |
| PAUSADO | `EstadoPausado` |
| GAME_OVER | `EstadoGameOver` |
| Transiciones | `GameController.setEstado()` |

---

## 2. Estado del Mirage

```mermaid
stateDiagram-v2
    [*] --> NORMAL : Mirage creado (vidas = 3)

    NORMAL --> INVENCIBLE : recibirDanio() && vidas > 0
    INVENCIBLE --> NORMAL : frameInvencible >= DURACION_INV

    NORMAL --> MUERTO : recibirDanio() && vidas == 0
    MUERTO --> [*] : GameController detecta !estaViva()

    note right of INVENCIBLE
        Ignora colisiones con enemigos.
        Parpadea (blink) en render.
        Cuenta frameInvencible.
    end note
```

---

## 3. Estado de un Proyectil

```mermaid
stateDiagram-v2
    [*] --> ACTIVO : Mirage.disparar()

    ACTIVO --> IMPACTADO : ColisionDetector detecta colisión con HarrierEnemigo
    ACTIVO --> FUERA_DE_PANTALLA : getY() < 0

    IMPACTADO --> [*] : desactivar()
    FUERA_DE_PANTALLA --> [*] : GameController limpia lista
```

---

## 4. Estado de un HarrierEnemigo

```mermaid
stateDiagram-v2
    [*] --> ACTIVO : EnemySpawner crea HarrierEnemigo

    ACTIVO --> DESTRUIDO : vida <= 0
    ACTIVO --> FUERA_DE_PANTALLA : getY() > altura + margen

    DESTRUIDO --> [*] : GameController suma puntos
    FUERA_DE_PANTALLA --> [*] : GameController elimina (sin puntos)

    note right of ACTIVO
        moverIA() cada frame.
        HitBox activa.
    end note
```
