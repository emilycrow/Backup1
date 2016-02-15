package mx.itesm.wahcamole;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by rmroman on 25/01/16.
 */
public class PantallaMenu implements Screen
{
    private final Principal principal;
    private OrthographicCamera camara;
    private Viewport vista;

    // Fondo
    private Texture texturaFondo;
    private Sprite spriteFondo;

    // Botón play
    private Texture texturaBtnPlay;
    private Sprite spriteBtnPlay;

    // Botón salir
    private Texture texturaBtnSalir;
    private Sprite spriteBtnSalir;

    // Dibujar
    private SpriteBatch batch;

    public PantallaMenu(Principal principal) {
        this.principal = principal;
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
    }

    private void cargarTexturasSprites() {
        // Fondo
        texturaFondo = new Texture(Gdx.files.internal("fondoMenu.jpg"));
        spriteFondo = new Sprite(texturaFondo);
        // Btn Play
        texturaBtnPlay = new Texture(Gdx.files.internal("playBtn.png"));
        spriteBtnPlay = new Sprite(texturaBtnPlay);
        spriteBtnPlay.setPosition(Principal.ANCHO_MUNDO / 2 - spriteBtnPlay.getWidth() / 2,
                Principal.ALTO_MUNDO / 2);
        // Btn Salir
        texturaBtnSalir = new Texture(Gdx.files.internal("exitBtn.png"));
        spriteBtnSalir = new Sprite(texturaBtnSalir);
        spriteBtnSalir.setPosition(Principal.ANCHO_MUNDO / 2 - spriteBtnSalir.getWidth() / 2,
                Principal.ALTO_MUNDO / 4);
    }

    @Override
    public void render(float delta) {
        // Borrar la pantalla
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Proyectamos la cámara sobre batch
        batch.setProjectionMatrix(camara.combined);
        // Dibujamos
        leerEntrada();

        batch.begin();
        spriteFondo.draw(batch);
        spriteBtnPlay.draw(batch);
        spriteBtnSalir.draw(batch);
        batch.end();
    }

    private void leerEntrada() {
        if (Gdx.input.justTouched()==true) {
            Vector3 coordenadas = new Vector3();
            coordenadas.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camara.unproject(coordenadas); // Transforma las coord
            float touchX = coordenadas.x;
            float touchY = coordenadas.y;
            // CAMBIAR
            if ( touchX>=spriteBtnSalir.getX() &&
                    touchX<=spriteBtnSalir.getX()+spriteBtnSalir.getWidth()
                    && touchY>=spriteBtnSalir.getY()
                    && touchY<=spriteBtnSalir.getY()+spriteBtnSalir.getHeight() ) {
                Gdx.app.exit();
            } else
            if ( touchX>=spriteBtnPlay.getX() &&
                    touchX<=spriteBtnPlay.getX()+spriteBtnPlay.getWidth()
                    && touchY>=spriteBtnPlay.getY()
                    && touchY<=spriteBtnPlay.getY()+spriteBtnPlay.getHeight() ) {
                // Lanzar la pantalla de juego
                principal.setScreen(new PantallaJuego(principal));
            }
        }
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

    }

    @Override
    public void dispose() {
        // Cuando la PantallaMenu sale de memoria.
        // LIBERAR los recursos
        texturaFondo.dispose(); // regresamos la memoria
    }
}
