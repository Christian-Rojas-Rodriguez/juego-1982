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
        // controller.getRenderer().setPantalla(controller.getRenderer().getPantalla());
    }

    @Override
    public void update(GameController controller) {
        controller.getNivel().update();
        controller.getEnemigos().addAll(controller.getNivel().getEnemigosNuevos());
        controller.getMirage().update();
        for (mirage.model.entidades.enemigos.Enemigo e : controller.getEnemigos()) {
            e.update();
        }
        controller.getColisionDetector().detectarProyectilEnemigo(
                controller.getMirage().getProyectiles(),
                controller.getEnemigos(),
                controller.getMirage()
        );
        controller.getColisionDetector().detectarEnemigoMirage(
                controller.getEnemigos(),
                controller.getMirage()
        );
        controller.getEnemigos().removeIf(enemigo -> !enemigo.estaViva());
        controller.getMirage().getProyectiles().removeIf(proyectil -> !proyectil.isActivo());
        if (!controller.getMirage().estaViva()) {
            controller.setEstado(new EstadoGameOver());
        }
        // TODO: si nivel.isTerminado() → finalizar módulo
    }

    @Override
    public void render(GameController controller) {
        controller.getRenderer().render(
                controller.getMirage(),
                controller.getEnemigos(),
                controller.getMirage().getProyectiles(),
                controller.getSketch()
        );
    }

    @Override
    public void onKeyPressed(GameController controller, char key, int keyCode) {
        if (key == 'p' || key == 'P') {
            controller.setEstado(new EstadoPausado());
        }
    }
}
