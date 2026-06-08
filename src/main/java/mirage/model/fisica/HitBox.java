package mirage.model.fisica;

/** Caja de colisión rectangular (AABB). */
public class HitBox {

    private float x;
    private float y;
    private final float ancho;
    private final float alto;

    public HitBox(float x, float y, float ancho, float alto) {
        this.x     = x;
        this.y     = y;
        this.ancho = ancho;
        this.alto  = alto;
    }

    /** Dos rectángulos colisionan salvo que uno esté del todo a un lado del otro. */
    public boolean colisionaCon(HitBox otro) {
        return !(this.x + this.ancho < otro.x ||
                  otro.x  + otro.ancho < this.x  ||
                  this.y + this.alto  < otro.y ||
                  otro.y  + otro.alto  < this.y);
    }

    public float getX()     { return x; }
    public float getY()     { return y; }
    public float getAncho() { return ancho; }
    public float getAlto()  { return alto; }
}
