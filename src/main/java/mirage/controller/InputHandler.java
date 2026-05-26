package mirage.controller;

import mirage.controller.commands.Comando;
import mirage.model.entidades.Mirage;

import java.util.HashMap;
import java.util.Map;

/**
 * Traduce códigos de teclado a Commands y los ejecuta sobre el Mirage.
 *
 * Mantiene un mapa keyCode → Comando. GameController registra los comandos
 * en init() via registrarComando() y luego propaga los eventos de Processing.
 *
 * Patrón: GRASP Pure Fabrication + Command
 */
public class InputHandler {

    /** Mapa keyCode → Comando. Se configura en GameController.init(). */
    private final Map<Integer, Comando> comandos = new HashMap<>();

    /**
     * Asocia una tecla con un comando.
     * Llamado desde GameController.init() para configurar todos los controles.
     */
    public void registrarComando(int keyCode, Comando comando) {
        // TODO: comandos.put(keyCode, comando)
    }

    /**
     * Ejecutar el comando asociado a la tecla presionada.
     * Para teclas de movimiento: activa el flag (moverDerecha = true).
     * Para SPACE: llama mirage.disparar().
     */
    public void onKeyPressed(int keyCode, char key, Mirage mirage) {
        // TODO: Comando cmd = comandos.get(keyCode)
        // TODO: if (cmd != null) cmd.ejecutar(mirage)
    }

    /**
     * Deshacer el comando asociado a la tecla soltada.
     * Para teclas de movimiento: desactiva el flag (moverDerecha = false).
     * Para SPACE: no hace nada (el disparo no se deshace).
     */
    public void onKeyReleased(int keyCode, char key, Mirage mirage) {
        // TODO: Comando cmd = comandos.get(keyCode)
        // TODO: if (cmd != null) cmd.deshacer(mirage)
    }
}
