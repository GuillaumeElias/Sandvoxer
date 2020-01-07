package me.guillaumeelias.sandvoxer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import me.guillaumeelias.sandvoxer.view.VoxelModelFactory;
import me.guillaumeelias.sandvoxer.view.screen.GameScreen;
import me.guillaumeelias.sandvoxer.view.screen.MenuScreen;

public class Sandvoxer extends Game {

	public static final Color BACKGROUND_COLOR = new Color(.1f, .12f, .16f, 1f);

	GameScreen gameScreen;
	MenuScreen menuScreen;

	@Override
	public void create () {
		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);

		this.setScreen(menuScreen);
	}

	public void switchToGameScreen(){
		this.setScreen(gameScreen);
	}

	public void switchToMenuScreen(){
		this.setScreen(menuScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		VoxelModelFactory.disposeAll();
	}
}
