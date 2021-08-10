package com.darkgran.farstar;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.IntSet;
import com.darkgran.farstar.battle.BattleScreen;

public class KeyboardProcessor extends InputAdapter { //in-future: check all singletons for thread-safety (see PlayerFactory)
    private final IntSet keysDown = new IntSet(20);
    private Farstar game;

    private static KeyboardProcessor keyboardProcessor = null;
    private KeyboardProcessor() {}
    public static KeyboardProcessor getInstance() {
        if (keyboardProcessor == null) { keyboardProcessor = new KeyboardProcessor(); }
        return keyboardProcessor;
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
        if (game != null) {
            if (keycode == Input.Keys.ESCAPE || (keycode == Input.Keys.SPACE && getGame().getSuperScreen() instanceof IntroScreen)) {
                if (getGame().getSuperScreen() != null) {
                    getGame().getSuperScreen().userEscape();
                }
            } else { //if (keysDown.size == 1)
                switch (keycode) {
                    case Input.Keys.F1:
                        if (getGame().getSuperScreen() instanceof BattleScreen) {
                            ((BattleScreen) getGame().getSuperScreen()).getBattleStage().toggleBattleHelp();
                        }
                        break;
                    case Input.Keys.F2:
                        getGame().getSuperScreen().getScreenSettings().setTableStageEnabled(!getGame().getSuperScreen().getScreenSettings().isTableStageEnabled(), getGame().getSuperScreen().getTableMenu());
                        break;
                    case Input.Keys.F3:
                        if (getGame().getSuperScreen() instanceof BattleScreen) {
                            getGame().getSuperScreen().getScreenSettings().setTokenFramesEnabled(!getGame().getSuperScreen().getScreenSettings().areTokenFramesEnabled());
                        }
                        break;
                    case Input.Keys.F4:
                        if (getGame().getSuperScreen() instanceof BattleScreen) {
                            getGame().getSuperScreen().getScreenSettings().setNetEnabled(!getGame().getSuperScreen().getScreenSettings().isNetEnabled());
                        }
                        break;
                    case Input.Keys.F5:
                        if (getGame().getSuperScreen() instanceof BattleScreen) {
                            getGame().getSuperScreen().getScreenSettings().setF1buttonEnabled(!getGame().getSuperScreen().getScreenSettings().isF1buttonEnabled());
                            ((BattleScreen) getGame().getSuperScreen()).getBattleStage().setF1ButtonVisibility(getGame().getSuperScreen().getScreenSettings().isF1buttonEnabled());
                        }
                        break;
                    case Input.Keys.F6:
                        getGame().getSuperScreen().getScreenSettings().setPerfMeterEnabled(!getGame().getSuperScreen().getScreenSettings().isPerfMeterEnabled());
                        break;
                    case Input.Keys.F8:
                        getGame().getSuperScreen().toggleFPSthrottle();
                        break;
                }
            }
        }
        keysDown.remove(keycode);
        return true;
    }

    public IntSet getKeysDown() {
        return keysDown;
    }

    public Farstar getGame() {
        return game;
    }

    public void setGame(Farstar game) {
        this.game = game;
    }
}
