# Diagrama de Clases — Módulo Mirage

> TPI Algoritmos 1 · 2026 1° Cuatrimestre  
> Vista estática del sistema — cubre p5, p6 y p7 del TPI

---

## Diagrama completo del módulo

```mermaid
classDiagram
    %% ── FACADE ─────────────────────────────────────────────
    class MirageModulo {
        <<Facade>>
        -controller GameController
        +iniciar(sketch) void
        +pausar() void
        +reanudar() void
        +getResumen() ResumenPartida
    }

    %% ── CONTROLLER ──────────────────────────────────────────
    class GameController {
        <<Controller>>
        -sketch PApplet
        -estadoActual EstadoJuego
        -mirage Mirage
        -enemigos List~Enemigo~
        -proyectiles List~Proyectil~
        -nivel NivelMirage
        -colisionDetector ColisionDetector
        -renderer GameRenderer
        -inputHandler InputHandler
        +init() void
        +update() void
        +render() void
        +setEstado(EstadoJuego) void
        +onKeyPressed(keyCode) void
        +onKeyReleased(keyCode) void
        +getMirage() Mirage
        +getEnemigos() List~Enemigo~
        +getProyectiles() List~Proyectil~
    }

    class InputHandler {
        <<Pure Fabrication>>
        -comandos Map~Integer, Comando~
        +onKeyPressed(keyCode, mirage) void
        +onKeyReleased(keyCode) void
        -registrarComandos() void
    }

    class Comando {
        <<interface>>
        +ejecutar(mirage) void
        +deshacer(mirage) void
    }
    class MoverIzquierdaCmd { +ejecutar(mirage) void }
    class MoverDerechaCmd   { +ejecutar(mirage) void }
    class MoverArribaCmd    { +ejecutar(mirage) void }
    class MoverAbajoCmd     { +ejecutar(mirage) void }
    class DispararCmd       { +ejecutar(mirage) void }

    %% ── STATE ───────────────────────────────────────────────
    class EstadoJuego {
        <<interface>>
        +update(controller) void
        +render(controller) void
        +alEntrar(controller) void
        +onKeyPressed(controller, keyCode) void
    }
    class EstadoJugando  { +update() +render() +alEntrar() +onKeyPressed() }
    class EstadoPausado  { +update() +render() +alEntrar() +onKeyPressed() }
    class EstadoGameOver {
        -puntajeFinal int
        +update() +render() +alEntrar() +onKeyPressed()
    }

    %% ── MODEL: ENTIDADES ────────────────────────────────────
    class Nave {
        <<abstract>>
        #x float
        #y float
        #velocidad float
        #vida int
        +update() void*
        +render(sketch) void*
        +getHitBox() HitBox
        +estaViva() bool
        +recibirDanio(danio) void
        +getX() float
        +getY() float
    }

    class Mirage {
        -vidas int
        -puntuacion int
        -invencible bool
        -frameInvencible int
        +update() void
        +render(sketch) void
        +disparar() Proyectil
        +sumarPuntos(puntos) void
        +getVidas() int
        +getPuntuacion() int
    }

    class Proyectil {
        -activo bool
        -danio int
        +update() void
        +render(sketch) void
        +isActivo() bool
        +desactivar() void
        +getDanio() int
        +getHitBox() HitBox
    }

    class Enemigo {
        <<abstract>>
        #puntos int
        +update() void
        +render(sketch) void*
        +getPuntos() int
        #moverIA() void*
    }

    class HarrierEnemigo {
        +render(sketch) void
        #moverIA() void
    }

    class FragataEnemiga {
        -disparaProyectiles bool
        +render(sketch) void
        #moverIA() void
        +disparar() Proyectil
    }

    class EnemyFactory {
        <<Factory>>
        +crear(Tipo, sketch, x, y) Enemigo$
    }

    %% ── MODEL: FÍSICA ───────────────────────────────────────
    class HitBox {
        -x float
        -y float
        -ancho float
        -alto float
        +colisionaCon(HitBox) bool
        +moverA(x, y) void
    }

    class ColisionDetector {
        <<Pure Fabrication>>
        +detectarProyectilEnemigo(proyectiles, enemigos) void
        +detectarEnemigoMirage(enemigos, mirage) void
    }

    %% ── MODEL: NIVELES ──────────────────────────────────────
    class Nivel {
        <<abstract>>
        +update() void*
        +isTerminado() bool*
        +getEnemigosNuevos() List~Enemigo~*
    }

    class NivelMirage {
        -spawner EnemySpawner
        -oleadaActual int
        +update() void
        +isTerminado() bool
        +getEnemigosNuevos() List~Enemigo~
    }

    class EnemySpawner {
        <<Pure Fabrication>>
        -intervaloFrames int
        -frameCounter int
        +update() List~Enemigo~
    }

    %% ── MODEL: STATS ────────────────────────────────────────
    class EstadisticasMirage {
        -enemigosDerribados int
        -tiempoJugado float
        -puntajeMaximo int
        -partidasJugadas int
        +registrarDerribo(puntos) void
        +registrarFinPartida(puntaje) void
        +guardar() void
        +cargar() void
        +exportar() ResumenPartida
    }

    class ResumenPartida {
        <<DTO>>
        -puntajeFinal int
        -enemigosDerribados int
        -vidasRestantes int
        -duracionSegundos float
        +getPuntajeFinal() int
        +getEnemigosDerribados() int
    }

    %% ── VIEW ────────────────────────────────────────────────
    class GameRenderer {
        -pantallaActual Pantalla
        -spriteLoader SpriteLoader
        +render(mirage, enemigos, proyectiles, sketch) void
        +setPantalla(Pantalla) void
    }

    class Pantalla {
        <<interface>>
        +render(sketch) void
        +update() void
    }
    class PantallaJuego   { +render(sketch) void +update() void }
    class PantallaGameOver {
        -puntaje int
        +render(sketch) void
        +update() void
    }

    class SpriteLoader {
        <<Pure Fabrication>>
        -cache Map~String, PImage~
        +cargar(nombre, sketch) void$
        +get(nombre) PImage$
    }

    class Animacion {
        -frames List~PImage~
        -frameActual int
        -velocidad int
        +update() void
        +getFrameActual() PImage
    }

    %% ── EXCEPCIONES ─────────────────────────────────────────
    class JuegoException      { <<Exception>> }
    class ColisionException   { <<Exception>> }
    class RecursoNoEncontradoException { <<Exception>> }

    %% ── RELACIONES ──────────────────────────────────────────
    MirageModulo --> GameController
    MirageModulo ..> ResumenPartida

    GameController --> EstadoJuego
    GameController --> Mirage
    GameController --> GameRenderer
    GameController --> InputHandler
    GameController --> NivelMirage
    GameController --> ColisionDetector
    GameController --> EstadisticasMirage

    InputHandler --> Comando
    Comando <|.. MoverIzquierdaCmd
    Comando <|.. MoverDerechaCmd
    Comando <|.. MoverArribaCmd
    Comando <|.. MoverAbajoCmd
    Comando <|.. DispararCmd

    EstadoJuego <|.. EstadoJugando
    EstadoJuego <|.. EstadoPausado
    EstadoJuego <|.. EstadoGameOver

    Nave <|-- Mirage
    Nave <|-- Enemigo
    Nave <|-- Proyectil
    Nave --> HitBox

    Enemigo <|-- HarrierEnemigo
    Enemigo <|-- FragataEnemiga
    EnemyFactory ..> Enemigo

    Nivel <|-- NivelMirage
    NivelMirage --> EnemySpawner
    EnemySpawner ..> EnemyFactory

    ColisionDetector --> HitBox

    GameRenderer --> Pantalla
    GameRenderer --> SpriteLoader
    Pantalla <|.. PantallaJuego
    Pantalla <|.. PantallaGameOver
    Animacion --> SpriteLoader

    EstadisticasMirage ..> ResumenPartida

    JuegoException <|-- ColisionException
    JuegoException <|-- RecursoNoEncontradoException
```

---

## Vista p6 — Model (entidades y jerarquía)

```mermaid
classDiagram
    class Nave {
        <<abstract>>
        #x, y, velocidad, vida
        +update()*
        +render(sketch)*
        +estaViva() bool
        +recibirDanio(int)
        +getHitBox() HitBox
    }
    class Mirage   { +disparar() Proyectil }
    class Proyectil{ +isActivo() bool +desactivar() }
    class Enemigo  { <<abstract>> #puntos int +moverIA()* }
    class HarrierEnemigo
    class FragataEnemiga { +disparar() Proyectil }

    Nave <|-- Mirage
    Nave <|-- Proyectil
    Nave <|-- Enemigo
    Enemigo <|-- HarrierEnemigo
    Enemigo <|-- FragataEnemiga
```

---

## Vista p7 — Vista y Controlador

```mermaid
classDiagram
    class GameController {
        <<Controller>>
        -estadoActual EstadoJuego
        +update()
        +render()
        +setEstado(EstadoJuego)
    }
    class EstadoJuego { <<interface>> }
    class EstadoJugando
    class EstadoPausado
    class EstadoGameOver

    class GameRenderer { +render(...) void }
    class Pantalla     { <<interface>> +render(sketch) }
    class PantallaJuego
    class PantallaGameOver
    class InputHandler { <<Pure Fabrication>> }
    class Comando      { <<interface>> }

    GameController --> EstadoJuego
    GameController --> GameRenderer
    GameController --> InputHandler
    EstadoJuego <|.. EstadoJugando
    EstadoJuego <|.. EstadoPausado
    EstadoJuego <|.. EstadoGameOver
    GameRenderer --> Pantalla
    Pantalla <|.. PantallaJuego
    Pantalla <|.. PantallaGameOver
    InputHandler --> Comando
```
