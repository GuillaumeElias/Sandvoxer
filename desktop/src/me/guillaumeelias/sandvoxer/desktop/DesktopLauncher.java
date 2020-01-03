package me.guillaumeelias.sandvoxer.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.guillaumeelias.sandvoxer.Sandvoxer;

public class DesktopLauncher {
	public static void main (String[] arg) {
		new LwjglApplication(new Sandvoxer(),  "Sandvoxer", 1064, 800);
	}
}
