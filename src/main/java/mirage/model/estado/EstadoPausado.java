package mirage.model.estado;

import mirage.controller.GameController;

public class EstadoPausado implements EstadoJuego {

    @Override
    public void alEntrar(GameController controller) {
        // Overlay de pausa como Pantalla (Strategy), igual que EstadoGameOver.
        controller.getRenderer()
                  .setPantalla(new mirage.view.pantallas.PantallaPausa());
    }

    @Override
    public void update(GameController controller) {
        // El juego está pausado: no se actualiza ninguna entidad.
    }

    @Override
    public void render(GameController controller) {
        // El juego se dibuja "congelado" (no se actualizó en update()) y el
        // GameRenderer superpone la PantallaPausa seteada en alEntrar().
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
            controller.setEstado(new EstadoJugando());
        }
    }
}
