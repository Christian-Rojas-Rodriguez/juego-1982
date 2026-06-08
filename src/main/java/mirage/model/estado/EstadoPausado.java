package mirage.model.estado;

import mirage.controller.GameController;

public class EstadoPausado implements EstadoJuego {

    @Override
    public void alEntrar(GameController controller) {
    }

    @Override
    public void update(GameController controller) {
        // El juego está pausado: no se actualiza ninguna entidad.
    }

    @Override
    public void render(GameController controller) {
        // El juego se dibuja "congelado": las entidades no se actualizaron en update().
        controller.getRenderer().render(
                controller.getMirage(),
                controller.getEnemigos(),
                controller.getMirage().getProyectiles(),
                controller.getEfectos(),
                controller.getSketch()
        );
        // Overlay "PAUSA" centrado en pantalla.
        processing.core.PApplet sk = controller.getSketch();
        sk.fill(255, 220, 0);
        sk.textAlign(processing.core.PApplet.CENTER, processing.core.PApplet.CENTER);
        sk.textSize(28);
        sk.text("PAUSA", sk.width / 2f, sk.height / 2f);
    }

    @Override
    public void onKeyPressed(GameController controller, char key, int keyCode) {
        if (key == 'p' || key == 'P') {
            controller.setEstado(new EstadoJugando());
        }
    }
}
