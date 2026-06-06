# Harness de integración con el HOME

Este repo integra el módulo **Mirage** con el lobby del grupo **HOME**
(`marcosbenson/Modulo_1_Algoritmos_1`, rama `processing-ide`).

Para poder **correr el lobby y seleccionar Mirage** desde IntelliJ, se trajeron al
proyecto copias del código del HOME, adaptadas a packages de Java:

| Carpeta | Origen | Cambios respecto al upstream |
| --- | --- | --- |
| `src/main/java/contracts/` | `contracts/` del repo HOME | Solo se antepuso `package contracts;` |
| `src/main/java/lobby/` | `lobby/` del repo HOME | Solo `package lobby;` + `import contracts.*;` |
| `data/PressStart2P-Regular.ttf` | `data/` del repo HOME | Sin cambios (fuente del lobby) |

> ⚠️ **Estas copias son una herramienta local de integración, NO la fuente de verdad.**
> La lógica del HOME no se modifica (solo se le agregan `package`/imports para que
> compile en IntelliJ junto a nuestro código empaquetado). Si el grupo HOME actualiza
> sus `contracts/` o `lobby/`, hay que re-sincronizar estas carpetas.
>
> El repo HOME es **plano / sin packages a propósito** (lo exige Processing IDE), así
> que estas versiones empaquetadas **no se pushean** de vuelta a su repo.

## Cómo correr

Desde IntelliJ:

- **Lobby completo + Mirage:** ejecutar la clase **`HomeRunner`**
  (`src/main/java/HomeRunner.java`). Flujo: INICIO → START → SELECCIÓN → elegir
  *"Dassault Mirage III [Mirage]"* → JUEGO. En JUEGO: **ESC** pausa/reanuda, **Q**
  finaliza (lo intercepta el lobby).
- **Módulo Mirage solo (sin lobby):** ejecutar **`Juego1982`**
  (`src/main/java/Juego1982.java`).

Por consola (desde la raíz del repo):

```bash
# Compilar todo contra Processing
find src/main/java -name '*.java' > /tmp/sources.txt
javac -d out/prod -cp lib/core.jar @/tmp/sources.txt

# Correr el lobby con Mirage
java -cp "out/prod:lib/core.jar" HomeRunner
```

## Cómo se conecta Mirage al contrato

`mirage.ModuloMirage` implementa `contracts.ModuloJuego`:

- **Identidad:** `getNombreModulo()="Mirage"`, `getNombreAvion()="Dassault Mirage III"`.
- **Ciclo de vida** con la máquina de estados del HOME (`NoIniciado → Iniciando →
  EnEjecucion ↔ Pausado → Finalizado`); lanza `EstadoInvalidoException` en transiciones
  inválidas.
- **Observers:** notifica `INICIADO / PAUSADO / REANUDADO / FINALIZADO` al lobby.
- **Estadísticas:** `getEstadisticasGenerales()` nunca devuelve `null`.

> Estado actual: integración funcional con **gameplay placeholder**. La máquina de
> estados, observers y stats están completos y testeados; la lógica de juego real se
> cablea donde están los `TODO` de `ModuloMirage` y `GameController`.

Tests de integración: `src/test/java/mirage/ModuloMirageIntegracionTest.java`.

## Entrega al HOME (etapa siguiente)

Lo que se PR-ea al repo HOME (vía fork, contra `processing-ide`) es el módulo como
`modules/mirage/` **plano** (sin packages, clases con prefijo `Mirage`, assets
`mirage_*`, `README_MIRAGE.md`) — no este harness empaquetado.
