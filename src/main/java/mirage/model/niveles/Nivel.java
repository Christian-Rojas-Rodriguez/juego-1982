package mirage.model.niveles;

import mirage.model.entidades.enemigos.Enemigo;

import java.util.List;

public abstract class Nivel {

    public abstract void update();

    public abstract boolean isTerminado();

    /** Enemigos generados en este frame; lista vacía si no hay nuevos. */
    public abstract List<Enemigo> getEnemigosNuevos();
}
