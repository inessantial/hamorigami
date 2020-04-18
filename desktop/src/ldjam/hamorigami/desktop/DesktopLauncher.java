package ldjam.hamorigami.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ldjam.hamorigami.HamorigamiGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "hamorigami";
		new LwjglApplication(new HamorigamiGame(), config);
	}
}
