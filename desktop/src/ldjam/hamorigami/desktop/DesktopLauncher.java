package ldjam.hamorigami.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ldjam.hamorigami.HamorigamiGame;

public class DesktopLauncher {
	public static void main (String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "hamorigami";
		config.audioDeviceBufferSize = 1024;
		config.audioDeviceSimultaneousSources = 32;
		config.forceExit = false;
		config.pauseWhenBackground = true;
		config.resizable = false;
		new LwjglApplication(new HamorigamiGame(args), config);
	}
}
