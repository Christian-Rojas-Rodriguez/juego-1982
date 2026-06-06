package mirage.model.estado;

import mirage.controller.GameController;

/**
 * Estado: partida terminada (vidas del Mirage = 0).
 *
 * Al entrar: registra fin de partida en estadísticas, notifica al HOME
 * que el módulo finalizó, y configura la pantalla de Game Over en el renderer.
 *
 * Durante update(): no hace nada — espera input del jugador.
 * onKeyPressed(): R reinicia, ESC vuelve al HOME.
 *
 * Patrón: State
 */
public class EstadoGameOver implements EstadoJuego {

    @Override
    public void alEntrar(GameController controller) {
        int puntaje    = controller.getMirage().getPuntuacion();
        int derribados = controller.getEstadisticas().getEnemigosDerribados();

        controller.getEstadisticas().registrarFinPartida(puntaje);
        // TODO(UC-08): controller.getEstadisticas().guardar() — persistencia a CSV fuera de v1.

        controller.getRenderer()
                  .setPantalla(new mirage.view.pantallas.PantallaGameOver(puntaje, derribados));

        try {
            controller.getMirageModulo().finalizar();   // notifica FINALIZADO al HOME
        } catch (contracts.EstadoInvalidoException e) {
            // Ya finalizado o estado no válido: el HOME maneja el ciclo de vida.
        }
    }

    @Override
    public void update(GameController controller) {
        // No actualizar — espera input del jugador para reiniciar o salir
    }

    @Override
    public void render(GameController controller) {
        // Juego de fondo + la pantalla de Game Over (ya seteada en alEntrar()).
        controller.getRenderer().render(
                controller.getMirage(),
                controller.getEnemigos(),
                controller.getMirage().getProyectiles(),
                controller.getSketch()
        );
    }

    @Override
    public void onKeyPressed(GameController controller, char key, int keyCode) {
        // R reinicia. ESC lo intercepta el HOME; no se maneja acá.
        if (key == 'r' || key == 'R') {
            controller.getMirageModulo().reset();
        }
    }
}
