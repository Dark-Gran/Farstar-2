package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.darkgran.farstar.battle.BattleScreen;
import com.darkgran.farstar.gui.AssetLibrary;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.gui.battlegui.DropTarget;
import com.darkgran.farstar.gui.battlegui.SupportMenu;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.SimpleBox2;

import static com.darkgran.farstar.battle.BattleScreen.DEBUG_RENDER;

public class MothershipToken extends ClickToken implements DropTarget, DisableMark {
    private final SimpleBox2 simpleBox2 = new SimpleBox2();
    private final SupportMenu supportMenu;
    private TextureRegion disableMark;

    public MothershipToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, SupportMenu supportMenu) {
        super(battleCard, x, y, battleStage, cardListMenu, TokenType.MS, false, true);
        this.supportMenu = supportMenu;
        setupSimpleBox2(x, y, getWidth(), getHeight());
        getCard().getBattlePlayer().getMs().setToken(this);
        setMark(AssetLibrary.getInstance().getAtlasRegion("disable-MS"));
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        drawMark(batch, getX(), getY());
        //Draws DropTarget SimpleBox2
        if (DEBUG_RENDER) { BattleScreen.drawDebugSimpleBox2(getSimpleBox2(), getBattleStage().getBattleScreen().getShapeRenderer(), batch); }
    }

    @Override
    void click(int button) {
        super.click(button);
        if (button == 0) {
            getBattleStage().getBattleScreen().getBattle().getRoundManager().processClick(this, getCard().getBattlePlayer());
        }
    }

    @Override
    public SimpleBox2 getSimpleBox2() { return simpleBox2; }

    public SupportMenu getSupportMenu() {
        return supportMenu;
    }

    @Override
    public TextureRegion getMark() {
        return disableMark;
    }

    @Override
    public void setMark(TextureRegion texture) {
        disableMark = texture;
    }

    @Override
    public boolean isDisabled() {
        return getCard().isUsed();
    }
}
