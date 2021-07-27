package com.darkgran.farstar.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.MouseWindowQuery;
import org.lwjgl.input.Mouse;

public class DesktopLauncher {

	public static class MWQImplementation implements MouseWindowQuery {
		@Override
		public boolean isMouseInsideWindow() {
			return Mouse.isInsideWindow(); //libgdx lacks binding to this lwjgl method
		}
	}

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "FARSTAR";
		config.width = 1920;
		config.height = 960;
		config.foregroundFPS = 60;
		config.resizable = true;
		config.fullscreen = false;
		config.vSyncEnabled = false;
		config.samples = 8;
		new LwjglApplication(new Farstar(new MWQImplementation()), config);
	}

}
