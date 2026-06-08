package mirage.model.niveles;

import mirage.model.entidades.enemigos.Enemigo;
import mirage.model.entidades.enemigos.EnemyFactory;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class EnemySpawner {

    private final PApplet sketch;
    private int intervaloFrames;
    private final int tamanoOleada;
    private int frameCounter;

    /** Buffer de enemigos generados en el frame actual. Se limpia tras cada consulta. */
    private final List<Enemigo> nuevosEsteFrame = new ArrayList<>();

    public EnemySpawner(PApplet sketch, int intervaloFrames, int tamanoOleada) {
        this.sketch          = sketch;
        this.intervaloFrames = intervaloFrames;
        this.tamanoOleada    = tamanoOleada;
        this.frameCounter    = 0;
    }

    /** Avanza el contador y genera una oleada cuando se alcanza el intervalo. */
    public void update() {
        frameCounter++;
        if (frameCounter >= intervaloFrames) {
            for (int i = 0; i < tamanoOleada; i++) {
                float x = sketch.random(20, sketch.width - 20);
                nuevosEsteFrame.add(EnemyFactory.crear(EnemyFactory.Tipo.HARRIER, sketch, x, -20));
            }
            frameCounter = 0;
        }

    }

    /**
     * Retorna los enemigos generados en este frame y limpia el buffer.
     * Llamado por NivelMirage.getEnemigosNuevos() cada frame.
     */
    public List<Enemigo> getEnemigosNuevos() {
        List<Enemigo> resultado = new ArrayList<>(nuevosEsteFrame);
        nuevosEsteFrame.clear();
        // System.out.println(resultado.size());
        return resultado;
    }
}
