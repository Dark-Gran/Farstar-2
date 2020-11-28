package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.ListeningStage;
import com.darkgran.farstar.battle.BattleScreen;

public abstract class BattleStage extends ListeningStage {
    private final BattleScreen battleScreen;
    private final Texture turn = new Texture("images/turn.png");
    public final ImageButton turnButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(turn)));
    private final Texture yardPic = new Texture("images/yard.png");
    private FakeToken fakeToken;

    public BattleStage(final Farstar game, Viewport viewport, BattleScreen battleScreen) {
        super(game, viewport);
        this.battleScreen = battleScreen;
    }

    public void drawBattleStage(float delta, Batch batch) {
        if (fakeToken != null) { fakeToken.draw(batch); }
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

    public void drawTokenMenu(TokenMenu tokenMenu, Batch batch) {
        for (int i = 0; i < tokenMenu.getTokens().size(); i++) {
            tokenMenu.getTokens().get(i).draw(batch);
        }
    }

    public Texture getYardPic() { return yardPic; }

    public BattleScreen getBattleScreen() { return battleScreen; }

    public FakeToken getFakeToken() { return fakeToken; }

    public void setFakeToken(FakeToken fakeToken) {
        if (this.fakeToken != null) { this.fakeToken.remove(); }
        this.fakeToken = fakeToken;
        if (fakeToken != null) { this.addActor(fakeToken); }
    }

}
