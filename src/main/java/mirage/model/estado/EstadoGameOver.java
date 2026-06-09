package mirage.model.estado;

import mirage.controller.GameController;

public class EstadoGameOver implements EstadoJuego {

    @Override
    public void alEntrar(GameController controller) {
        int puntaje    = controller.getMirage().getPuntuacion();
        int derribados = controller.getEstadisticas().getEnemigosDerribados();

        controller.getEstadisticas().registrarFinPartida(puntaje);

        controller.getRenderer()
                  .setPantalla(new mirage.view.pantallas.PantallaGameOver(puntaje, derribados));
        // finalizar() lo llama el HOME cuando el jugador presiona ESC.
        // No lo llamamos aquí para que la pantalla de Game Over sea visible.
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
                controller.getEfectos(),
                controller.getSketch()
        );
    }

    @Override
    public void onKeyPressed(GameController controller, char key, int keyCode) {
        if (key == 'r' || key == 'R') {
            controller.init(); // reinicia gameplay sin tocar el ciclo de vida HOME
        } else if (keyCode == 27) { // ESC → volver al HOME
            try {
                controller.getMirageModulo().finalizar();
            } catch (contracts.EstadoInvalidoException e) { /* ignorar */ }
        }
    }
}
