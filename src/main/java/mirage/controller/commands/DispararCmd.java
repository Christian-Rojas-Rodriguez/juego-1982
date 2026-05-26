package mirage.controller.commands;

import mirage.model.entidades.Mirage;

/**
 * Comando: el Mirage dispara un proyectil.
 *
 * Llama mirage.disparar() — Mirage gestiona su propia lista de proyectiles
 * y el contador de disparosTotales internamente.
 * Este comando no necesita conocer GameController ni ninguna lista externa.
 *
 * deshacer() no hace nada: el disparo ya ocurrió y no es reversible.
 *
 * Patrón: Command
 */
public class DispararCmd implements Comando {

    @Override
    public void ejecutar(Mirage mirage) {
        // TODO: mirage.disparar()
    }

    @Override
    public void deshacer(Mirage mirage) {
        // El disparo no se deshace
    }
}
