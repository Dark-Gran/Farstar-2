package com.darkgran.farstar;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.IntSet;
import com.darkgran.farstar.battle.BattleScreen;

public class KeyboardProcessor extends InputAdapter {
    private final IntSet keysDown = new IntSet(20);
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
        if (keycode == Input.Keys.ESCAPE || (keycode == Input.Keys.SPACE && game.getSuperScreen() instanceof IntroScreen)) {
            if (game.getSuperScreen() != null) { game.getSuperScreen().userEscape(); }
        } else { //if (keysDown.size == 1)
            switch (keycode) {
                case Input.Keys.F1:
                    if (game.getSuperScreen() instanceof BattleScreen) {
                        ((BattleScreen) game.getSuperScreen()).getBattleStage().toggleBattleHelp();
                    }
                    break;
                case Input.Keys.F2:
                    game.getSuperScreen().setTableStageEnabled(!game.getSuperScreen().isTableStageEnabled());
                    break;
                case Input.Keys.F3:
                    if (game.getSuperScreen() instanceof BattleScreen) {
                        game.getSuperScreen().setTokenFramesEnabled(!game.getSuperScreen().isTokenFramesEnabled());
                    }
                    break;
                case Input.Keys.F4:
                    if (game.getSuperScreen() instanceof BattleScreen) {
                        game.getSuperScreen().setNetEnabled(!game.getSuperScreen().isNetEnabled());
                        //((BattleScreen) game.getSuperScreen()).getBattleStage().f1ButtonToNet();
                    }
                    break;
                case Input.Keys.F5:
                    if (game.getSuperScreen() instanceof BattleScreen) {
                        ((BattleScreen) game.getSuperScreen()).getBattleStage().toggleF1Button();
                    }
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
