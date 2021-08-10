package com.darkgran.farstar;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.IntSet;
import com.darkgran.farstar.battle.BattleScreen;

public class KeyboardProcessor extends InputAdapter { //in-future: check all singletons for thread-safety (see PlayerFactory)
    private final IntSet keysDown = new IntSet(20);
    private Farstar game;

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
        if (game != null) {
            if (keycode == Input.Keys.ESCAPE || (keycode == Input.Keys.SPACE && game.getSuperScreen() instanceof IntroScreen)) {
                if (game.getSuperScreen() != null) {
                    game.getSuperScreen().userEscape();
                }
            } else { //if (keysDown.size == 1)
                switch (keycode) {
                    case Input.Keys.F1:
                        if (game.getSuperScreen() instanceof BattleScreen) {
                            ((BattleScreen) game.getSuperScreen()).getBattleStage().toggleBattleHelp();
                        }
                        break;
                    case Input.Keys.F2:
                        game.getSuperScreen().getScreenSettings().setTableStageEnabled(!game.getSuperScreen().getScreenSettings().isTableStageEnabled(), game.getSuperScreen().getTableMenu());
                        break;
                    case Input.Keys.F3:
                        if (game.getSuperScreen() instanceof BattleScreen) {
                            game.getSuperScreen().getScreenSettings().setTokenFramesEnabled(!game.getSuperScreen().getScreenSettings().areTokenFramesEnabled());
                        }
                        break;
                    case Input.Keys.F4:
                        if (game.getSuperScreen() instanceof BattleScreen) {
                            game.getSuperScreen().getScreenSettings().setNetEnabled(!game.getSuperScreen().getScreenSettings().isNetEnabled());
                        }
                        break;
                    case Input.Keys.F5:
                        if (game.getSuperScreen() instanceof BattleScreen) {
                            game.getSuperScreen().getScreenSettings().setF1buttonEnabled(!game.getSuperScreen().getScreenSettings().isF1buttonEnabled());
                            ((BattleScreen) game.getSuperScreen()).getBattleStage().setF1ButtonVisibility(game.getSuperScreen().getScreenSettings().isF1buttonEnabled());
                        }
                        break;
                    case Input.Keys.F6:
                        game.getSuperScreen().getScreenSettings().setPerfMeterEnabled(!game.getSuperScreen().getScreenSettings().isPerfMeterEnabled());
                        break;
                    case Input.Keys.F8:
                        game.getSuperScreen().toggleFPSthrottle();
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

}
