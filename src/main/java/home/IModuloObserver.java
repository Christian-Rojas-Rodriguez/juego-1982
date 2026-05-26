package home;

/**
 * Interfaz del HOME team — observer que recibe eventos del módulo.
 * No modificar: definido por el grupo Lobby (repo Modulo_1_Algoritmos_1).
 *
 * HomeJuego implementa esta interfaz y se registra en MirageModulo via agregarObserver().
 * MirageModulo la llama cuando cambia de estado (INICIADO, PAUSADO, FINALIZADO, etc.).
 */
public interface IModuloObserver {

    void onEventoModulo(ModuloEvento evento);
}