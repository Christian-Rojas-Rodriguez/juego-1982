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
        // Explosión al destruir un enemigo: spawnear efecto antes de removerlo.
        for (mirage.model.entidades.enemigos.Enemigo e : controller.getEnemigos()) {
            if (!e.estaViva()) {
                controller.getEfectos().add(
                        new mirage.model.efectos.Explosion(e.getX(), e.getY()));
            }
        }
        controller.getEnemigos().removeIf(enemigo -> !enemigo.estaViva());
        // Enemigos que salieron por debajo de la pantalla: se descartan (sin sumar
        // puntos). Evita la acumulacion indefinida de entidades fuera de vista.
        float limiteY = controller.getSketch().height + 40;
        controller.getEnemigos().removeIf(enemigo -> enemigo.getY() > limiteY);
        controller.getMirage().getProyectiles().removeIf(proyectil -> !proyectil.isActivo());
        controller.getEfectos().forEach(mirage.model.efectos.Explosion::update);
        controller.getEfectos().removeIf(mirage.model.efectos.Explosion::terminada);
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
                controller.getEfectos(),
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
