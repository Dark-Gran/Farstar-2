package com.darkgran.farstar;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.IntSet;
import com.darkgran.farstar.battle.BattleScreen;

public class KeyboardProcessor extends InputAdapter {
    private final IntSet keysDown = new IntSet(20);

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
        if (keycode == Input.Keys.ESCAPE || (keycode == Input.Keys.SPACE && Farstar.getSuperScreen() instanceof IntroScreen)) {
            if (Farstar.getSuperScreen() != null) { Farstar.getSuperScreen().userEscape(); }
        } else { //if (keysDown.size == 1)
            switch (keycode) {
                case Input.Keys.F1:
                    if (Farstar.getSuperScreen() instanceof BattleScreen) {
                        ((BattleScreen) Farstar.getSuperScreen()).getBattleStage().toggleBattleHelp();
                    }
                    break;
                case Input.Keys.F2:
                    ScreenSettings.getInstance().setTableStageEnabled(!ScreenSettings.getInstance().isTableStageEnabled(), Farstar.getSuperScreen().getTableMenu());
                    break;
                case Input.Keys.F3:
                    if (Farstar.getSuperScreen() instanceof BattleScreen) {
                        ScreenSettings.getInstance().setTokenFramesEnabled(!ScreenSettings.getInstance().areTokenFramesEnabled());
                    }
                    break;
                case Input.Keys.F4:
                    if (Farstar.getSuperScreen() instanceof BattleScreen) {
                        ScreenSettings.getInstance().setNetEnabled(!ScreenSettings.getInstance().isNetEnabled());
                    }
                    break;
                case Input.Keys.F5:
                    if (Farstar.getSuperScreen() instanceof BattleScreen) {
                        ScreenSettings.getInstance().setF1buttonEnabled(!ScreenSettings.getInstance().isF1buttonEnabled());
                        ((BattleScreen) Farstar.getSuperScreen()).getBattleStage().setF1ButtonVisibility(ScreenSettings.getInstance().isF1buttonEnabled());
                    }
                    break;
                case Input.Keys.F6:
                    ScreenSettings.getInstance().setPerfMeterEnabled(!ScreenSettings.getInstance().isPerfMeterEnabled());
                    break;
                case Input.Keys.F8:
                    Farstar.getSuperScreen().toggleFPSthrottle();
                    break;
            }
        }
        keysDown.remove(keycode);
        return true;
    }

    public IntSet getKeysDown() {
        return keysDown;
    }
}
