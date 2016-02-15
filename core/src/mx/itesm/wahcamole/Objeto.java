package mx.itesm.wahcamole;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by rmroman on 04/02/16.
 */
public class Objeto
{
    private Sprite sprite;

    // Dimensiones
    private float ancho, alto;
    private float alturaActual;

    private float velocidad = 2;

    // Estado
    private Estado estado;

    // Oculto
    private float tiempoOculto; // total oculto (azar)
    private float tiempoMuriendo;   // EL tiempo que tarda en morir (1)


    public Objeto(Texture textura) {
        sprite = new Sprite(textura);
        ancho = sprite.getWidth();
        alto = sprite.getHeight();  // Original
        alturaActual = alto;
        estado = Estado.BAJANDO;
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void actualizar() {
        switch (estado) {
            case BAJANDO:
                alturaActual -= velocidad;
                if (alturaActual<=0) {
                    alturaActual = 0;
                    estado = Estado.OCULTO;
                    tiempoOculto = tiempoAzar();
                }
                break;
            case OCULTO:
                if (tiempoOculto<=0) {
                    estado = Estado.SUBIENDO;
                }
                tiempoOculto -= Gdx.graphics.getDeltaTime();
                break;
            case MURIENDO:
                tiempoMuriendo -= Gdx.graphics.getDeltaTime();
                sprite.setColor(Color.PURPLE);
                sprite.setRotation(sprite.getRotation()+30);
                if (tiempoMuriendo<=0) {
                    estado = Estado.OCULTO;
                    tiempoOculto = tiempoAzar();
                    alturaActual = 0;
                    sprite.setColor(Color.WHITE);
                    sprite.setRotation(0);
                }
                break;
            case SUBIENDO:
                alturaActual += velocidad;
                if (alturaActual>=alto) {
                    alturaActual = alto;
                    estado = Estado.BAJANDO;
                }
        }

        sprite.setRegion(0,0,(int)ancho,(int)alturaActual);
        sprite.setSize(ancho, alturaActual);
    }

    private float tiempoAzar() {
        return (float)(Math.random()*2+0.5);
    }

    public void setPosicion(float x, float y) {
        sprite.setPosition(x,y);
    }

    // regresa true si detecta el touch (morir)
    public boolean detectarTouch(float x, float y) {

        if (estado==Estado.MURIENDO){
            return false;
        }
        if (x>=sprite.getX() && x<=sprite.getX()+ancho
                && y>=sprite.getY() && y<=sprite.getY()+alturaActual) {
            estado = Estado.MURIENDO;
            tiempoMuriendo = 1;
            return true;
        }
        return false;
    }

    // Estados del topo
    public enum Estado {
        BAJANDO,
        SUBIENDO,
        OCULTO,
        MURIENDO
    }
}
