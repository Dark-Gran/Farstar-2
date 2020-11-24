package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.ListeningMenu;
import com.darkgran.farstar.battle.BattleScreen;

public abstract class BattleMenu extends ListeningMenu {
    private final BattleScreen battleScreen;
    private final Texture turn = new Texture("images/turn.png");
    public final ImageButton turnButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(turn)));
    private final Texture yardPic = new Texture("images/yard.png");

    public BattleMenu(final Farstar game, Viewport viewport, BattleScreen battleScreen) {
        super(game, viewport);
        this.battleScreen = battleScreen;
    }

    @Override
    public void setupListeners() {
        turnButton.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
               battleScreen.getBattle().getRoundManager().endTurn();
            }
        });
    }

    @Override
    public void dispose() {
        turnButton.removeListener(turnButton.getClickListener());
        turn.dispose();
        super.dispose();
    }

    public Texture getYardPic() { return yardPic; }

    public BattleScreen getBattleScreen() { return battleScreen; }

}
