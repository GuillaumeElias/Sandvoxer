package me.guillaumeelias.sandvoxer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.guillaumeelias.sandvoxer.Sandvoxer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.foregroundFPS = 60;
		config.width = 1024;
		config.height = 800;
		new LwjglApplication(new Sandvoxer(), config);
	}
}
