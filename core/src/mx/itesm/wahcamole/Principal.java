package mx.itesm.wahcamole;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Principal extends Game
{
	public static final float ANCHO_MUNDO = 1280;
	public static final float ALTO_MUNDO = 720;

	
	@Override
	public void create () {
		setScreen(new PantallaMenu(this));
	}

}
