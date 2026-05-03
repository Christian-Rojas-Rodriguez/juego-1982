// ============================================================
// EstadoJugando — Estado activo: el juego corre normalmente
// ============================================================
// GRASP : Polymorphism, High Cohesion
// Patrón: State (estado concreto)
// ============================================================
// Qué implementar:
//   - update(): pedir al nivel nuevos enemigos y agregarlos,
//               actualizar mirage / enemigos / proyectiles,
//               llamar colisionDetector, limpiar entidades inactivas
//   - render(): delegar al GameRenderer
//   - alEntrar(): inicializar lo necesario al entrar en este estado
//   - onKeyPressed(): tecla P → setEstado(new EstadoPausado())
// ============================================================

package mirage.model.estado;

import mirage.controller.GameController;

public class EstadoJugando implements EstadoJuego {

    @Override
    public void alEntrar(GameController controller) {
        // TODO: inicializar pantalla de juego si es necesario
    }

    @Override
    public void update(GameController controller) {
        // TODO: controller.getNivel().update()
        // TODO: controller.getEnemigos().addAll(nivel.getEnemigosNuevos())
        // TODO: controller.getMirage().update()
        // TODO: actualizar cada enemigo y proyectil
        // TODO: controller.getColisionDetector().detectarProyectilEnemigo(...)
        // TODO: controller.getColisionDetector().detectarEnemigoMirage(...)
        // TODO: limpiar listas con removeIf
        // TODO: si !mirage.estaViva() → controller.setEstado(new EstadoGameOver())
        // TODO: si nivel.isTerminado() → finalizar módulo
    }

    @Override
    public void render(GameController controller) {
        // TODO: controller.getRenderer().render(
        //           controller.getMirage(),
        //           controller.getEnemigos(),
        //           controller.getProyectiles(),
        //           controller.getSketch()
        //       )
    }

    @Override
    public void onKeyPressed(GameController controller, int keyCode) {
        // TODO: si keyCode == 'p' o 'P' → controller.setEstado(new EstadoPausado())
    }
}
