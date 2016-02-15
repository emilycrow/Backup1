package mx.itesm.wahcamole;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by rmroman on 04/02/16.
 */
public class PantallaJuego implements Screen
{
    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;

    // Fondo
    private Texture texturaFondo;
    private Sprite spriteFondo;

    // Dibujar
    private SpriteBatch batch;

    // Hoyos 9, 3x3
    private Array<Objeto> hoyos;
    private Texture texturaHoyo;

    // Topos 9, 3x3
    private Array<Objeto> topos;
    private Texture texturaTopo;

    // Marcador (contador)
    private int marcador;
    private Texto texto;

    //Sonidos
    private Sound efectoGolpe; //Efecto
    private Music musicaFondo;  //Musica de fondo



    public PantallaJuego(Principal principal) {
        this.principal = principal;
        marcador = 0;
        texto = new Texto();
    }


    @Override
    public void show() {
        // Se ejecuta cuando se muestra la pantalla
        camara = new OrthographicCamera(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO);
        camara.position.set(Principal.ANCHO_MUNDO/2, Principal.ALTO_MUNDO/2, 0);
        camara.update();
        vista = new StretchViewport(Principal.ANCHO_MUNDO, Principal.ALTO_MUNDO,camara);

        batch = new SpriteBatch();

        cargarTexturasSprites();
        crearHoyos();
        crearTopos();
        efectoGolpe= Gdx.audio.newSound(Gdx.files.internal("The Boo! You Suck! Sound Effect.mp3"));
        musicaFondo= Gdx.audio.newMusic(Gdx.files.internal("04 Ageispolis.mp3"));
        musicaFondo.setLooping(true);   ///Infinito
        musicaFondo.play();


    }

    private void crearTopos() {
        topos = new Array<Objeto>(9);
        for(int i=0; i<9; i++) {
            Objeto nuevo = new Objeto(texturaTopo);
            topos.add(nuevo);
            // Cambiar la posición del topo
            float x = (i%3)*280 + 250;
            float y = (i/3)*150 + 230;
            nuevo.setPosicion(x, y);
        }
    }

    private void crearHoyos() {
        hoyos = new Array<Objeto>(9);
        for (int i=0; i<9; i++) {
            Objeto nuevo = new Objeto(texturaHoyo);
            hoyos.add(nuevo);
            // Cambiar la posición del hoyo
            float x = (i%3)*280 + 200;
            float y = (i/3)*150 + 200;
            nuevo.setPosicion(x,y);
        }
    }

    private void cargarTexturasSprites() {
        // Fondo
        texturaFondo = new Texture(Gdx.files.internal("fondoPasto.jpg"));
        spriteFondo = new Sprite(texturaFondo);
        // Hoyo
        texturaHoyo = new Texture(Gdx.files.internal("hole.png"));
        // Topo
        texturaTopo = new Texture(Gdx.files.internal("mole.png"));
    }

    @Override
    public void render(float delta) {
        // Leer
        leerEntrada();

        // Actualizar objetos
        for (Objeto topo :
                topos) {
            topo.actualizar();
        }
        // Borrar la pantalla
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Proyectamos la cámara sobre batch
        batch.setProjectionMatrix(camara.combined);
        // Dibujamos

        batch.begin();

        spriteFondo.draw(batch);
        // Dibujar los hoyos
        for (Objeto hoyo :
                hoyos) {
            hoyo.render(batch);
        }
        // Dibujar los TOPOS
        for (Objeto topo :
                topos) {
            topo.render(batch);
        }
        // Marcador
        texto.mostrarMensaje("Puntos: " + marcador, Principal.ANCHO_MUNDO/2,
                Principal.ALTO_MUNDO*0.95f, batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        vista.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        //apagar musica
        if (musicaFondo.isPlaying()){
            musicaFondo.stop();
        }

    }

    private void leerEntrada() {
        if (Gdx.input.justTouched()==true) {
            Vector3 coordenadas = new Vector3();
            coordenadas.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camara.unproject(coordenadas); // Transforma las coord
            float touchX = coordenadas.x;
            float touchY = coordenadas.y;
            // CAMBIAR
            // Recorrer los 9 topos
            for (Objeto topo :
                    topos) {
                if ( topo.detectarTouch(touchX, touchY)==true ) {
                    marcador += 1;
                    efectoGolpe.play();
                }
            }
        }
    }

    @Override
    public void dispose() {
        // Cuando la PantallaMenu sale de memoria.
        // LIBERAR los recursos
        //para apple liberar memoria es indispensable!!!
        texturaFondo.dispose(); // regresamos la memoria
        texturaTopo.dispose();
        texturaHoyo.dispose();
    }
}
