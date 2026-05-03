// ============================================================
// InputHandler — Traduce códigos de teclado a Commands
// ============================================================
// GRASP : Pure Fabrication, Low Coupling
// Patrón: Command (registra un Comando por tecla)
// ============================================================
// Qué implementar:
//   - registrarComandos(): mapear keyCode → instancia de Comando
//     Ej: comandos.put(PApplet.LEFT, new MoverIzquierdaCmd())
//   - onKeyPressed(): buscar comando en el mapa y llamar ejecutar(mirage)
//   - onKeyReleased(): llamar deshacer(mirage) si el comando lo requiere
// ============================================================

package mirage.controller;

import processing.core.PApplet;
import mirage.controller.commands.Comando;
import mirage.model.entidades.Mirage;

import java.util.HashMap;
import java.util.Map;

public class InputHandler {

    // --- Atributos ---
    private final Map<Integer, Comando> comandos = new HashMap<>();

    // --- Constructor ---
    public InputHandler() {
        registrarComandos();
    }

    // --- Registrar mapeo tecla → comando ---
    private void registrarComandos() {
        // TODO: comandos.put(PApplet.LEFT,  new MoverIzquierdaCmd())
        // TODO: comandos.put(PApplet.RIGHT, new MoverDerechaCmd())
        // TODO: comandos.put(PApplet.UP,    new MoverArribaCmd())
        // TODO: comandos.put(PApplet.DOWN,  new MoverAbajoCmd())
        // TODO: comandos.put(32,            new DispararCmd())  // 32 = SPACE
    }

    // --- Ejecutar comando al presionar tecla ---
    public void onKeyPressed(int keyCode, Mirage mirage) {
        // TODO: Comando cmd = comandos.get(keyCode)
        // TODO: if (cmd != null) cmd.ejecutar(mirage)
    }

    // --- Deshacer comando al soltar tecla ---
    public void onKeyReleased(int keyCode) {
        // TODO: Comando cmd = comandos.get(keyCode)
        // TODO: if (cmd != null) cmd.deshacer(mirage)
        // Nota: necesitarás pasar el mirage como parámetro también
    }
}
