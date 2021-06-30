package com.darkgran.farstar;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.IntSet;

public class KeyboardProcessor extends InputAdapter {
    private IntSet keysDown = new IntSet(20);
    private final Farstar game;

    public KeyboardProcessor(Farstar game) {
        this.game = game;
    }

    private void multipleKeysDown() {
        if (keysDown.size == 2) {
            //ALT+ENTER
            if (keysDown.contains(Input.Keys.ENTER) && (keysDown.contains(Input.Keys.ALT_LEFT) || keysDown.contains(Input.Keys.ALT_RIGHT))) {
                SuperScreen.switchFullscreen();
                keysDown.clear();
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        keysDown.add(keycode);
        if (keysDown.size > 1) {
            multipleKeysDown();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keysDown.remove(keycode);
        if (keycode == Input.Keys.ESCAPE || (keycode == Input.Keys.SPACE && game.getSuperScreen() instanceof IntroScreen)) {
            if (game.getSuperScreen() != null) { game.getSuperScreen().userEscape(); }
        }
        return true;
    }

    public IntSet getKeysDown() {
        return keysDown;
    }
}
