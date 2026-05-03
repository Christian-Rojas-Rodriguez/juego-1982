# Diagramas de Estado — Módulo Mirage

> TPI Algoritmos 1 · 2026 1° Cuatrimestre  
> Vista de comportamiento dinámico del sistema

---

## 1. Estado del Juego (máquina principal)

Gestiona el flujo de pantallas y controla qué se ejecuta en cada frame.
Implementado con el **patrón State** en `GameController`.

```mermaid
stateDiagram-v2
    [*] --> JUGANDO : iniciar()

    JUGANDO --> PAUSADO : tecla P
    PAUSADO --> JUGANDO : tecla P

    JUGANDO --> GAME_OVER : vidasMirage == 0
    GAME_OVER --> JUGANDO : tecla R (reiniciar)
    GAME_OVER --> [*] : tecla ESC (volver al HOME)

    JUGANDO --> NIVEL_COMPLETO : nivel.isTerminado()
    NIVEL_COMPLETO --> [*] : guardar stats y volver al HOME

    note right of JUGANDO
        update: mover entidades,
        detectar colisiones,
        actualizar spawner
    end note

    note right of PAUSADO
        Solo renderiza.
        No actualiza física ni lógica.
    end note

    note right of GAME_OVER
        Muestra puntaje final.
        Persiste estadísticas en archivo.
    end note
```

**Clases involucradas:**

| Estado | Clase Java |
|--------|-----------|
| JUGANDO | `EstadoJugando` |
| PAUSADO | `EstadoPausado` |
| GAME_OVER | `EstadoGameOver` |
| Transiciones | `GameController.setEstado(EstadoJuego)` |

---

## 2. Estado del Mirage (jugador)

Después de recibir daño, el Mirage entra en **invencibilidad temporal**
para evitar pérdidas de vida en cadena.

```mermaid
stateDiagram-v2
    [*] --> NORMAL : Mirage creado

    NORMAL --> INVENCIBLE : recibirDanio() && vidas > 0
    INVENCIBLE --> NORMAL : frameInvencible >= DURACION_INV

    NORMAL --> MUERTO : recibirDanio() && vidas == 0

    MUERTO --> [*] : GameController detecta !estaViva()

    note right of NORMAL
        Colisiones activas.
        Se renderiza normalmente.
        Puede disparar.
    end note

    note right of INVENCIBLE
        Ignora colisiones con enemigos.
        Renderiza parpadeando (blink).
        Cuenta frames de invencibilidad.
    end note

    note right of MUERTO
        vida <= 0.
        GameController transiciona
        a EstadoGameOver.
    end note
```

**Implementación sugerida en `Mirage.java`:**
- Campo `boolean invencible`
- Campo `int frameInvencible`
- Constante `int DURACION_INVENCIBILIDAD` (ej: 120 frames = 2 segundos)

---

## 3. Estado de un Enemigo

Cada enemigo tiene su propio ciclo de vida gestionado por `GameController`.

```mermaid
stateDiagram-v2
    [*] --> ACTIVO : EnemySpawner genera enemigo

    ACTIVO --> DESTRUIDO : vida <= 0
    ACTIVO --> FUERA_DE_PANTALLA : getY() > altura + margen

    DESTRUIDO --> [*] : GameController lo elimina\n(suma puntos al Mirage)
    FUERA_DE_PANTALLA --> [*] : GameController lo elimina\n(sin puntos)

    note right of ACTIVO
        Ejecuta moverIA() cada frame.
        HitBox activa.
        FragataEnemiga puede disparar.
    end note

    note right of DESTRUIDO
        estaViva() == false.
        Puede reproducir animación
        de explosión antes de eliminarse.
    end note
```

---

## 4. Estado de un Proyectil

Los proyectiles los crea `Mirage.disparar()` y los gestiona `GameController`.

```mermaid
stateDiagram-v2
    [*] --> ACTIVO : Mirage.disparar()

    ACTIVO --> IMPACTADO : ColisionDetector detecta colisión
    ACTIVO --> FUERA_DE_PANTALLA : getY() < 0

    IMPACTADO --> [*] : desactivar() — GameController limpia lista
    FUERA_DE_PANTALLA --> [*] : GameController limpia lista

    note right of ACTIVO
        Se mueve hacia arriba cada frame.
        HitBox activa.
        danio aplicado al impactar.
    end note
```

---

## Resumen: quién gestiona cada estado

| Ciclo de vida | Gestionado por |
|---------------|---------------|
| Estado del juego (JUGANDO/PAUSADO/GAME_OVER) | `GameController` con patrón **State** |
| Estado del Mirage (NORMAL/INVENCIBLE/MUERTO) | Campo interno en `Mirage.update()` |
| Estado del Enemigo (ACTIVO/DESTRUIDO) | `estaViva()` + `GameController` limpia la lista |
| Estado del Proyectil (ACTIVO/IMPACTADO) | `isActivo()` + `GameController` limpia la lista |
