package com.darkgran.farstar.gui.battlegui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.tokens.SupportToken;
import com.darkgran.farstar.gui.tokens.TokenType;
import com.darkgran.farstar.battle.players.CardList;
import com.darkgran.farstar.battle.players.BattlePlayer;
import com.darkgran.farstar.battle.players.Support;
import com.darkgran.farstar.util.SimpleBox2;
import com.darkgran.farstar.util.SimpleVector2;

public class SupportMenu extends CardListMenu implements DropTarget {
    private final SimpleBox2 simpleBox2 = new SimpleBox2();
    private Texture netSpot = Farstar.ASSET_LIBRARY.get("images/tokens/netspot_S.png");
    private SimpleVector2[] netSpotPositions = new SimpleVector2[6];

    public SupportMenu(CardList cardList, float x, float y, float tokensX, float tokensY, float width, float height, boolean negativeOffset, BattleStage battleStage, BattlePlayer battlePlayer) {
        super(cardList, x, y, tokensX, tokensY, negativeOffset, battleStage, battlePlayer);
        setWidth(width);
        setHeight(height);
        setupSimpleBox2(x, y, width, height);
        prepareNetSpots();
    }

    private void prepareNetSpots() {
        for (int i = 0; i < netSpotPositions.length; i++) {
            int pos = translatePosition(i);
            netSpotPositions[i] = new SimpleVector2(getTokensX() + x + getOffset()*pos + ((pos >= 3) ? TokenType.MS.getWidth()+11f : 0), getTokensY() + y);
        }
    }

    public void drawNetSpots(Batch batch) {
        for (int i = 0; i < netSpotPositions.length; i++) {
            if (getTokens().size() <= i || getTokens().get(i) == null) {
                batch.draw(netSpot, netSpotPositions[i].x, netSpotPositions[i].y);
            }
        }
    }

    @Override
    public void setupOffset() {
        setOffset(TokenType.SUPPORT.getWidth()*1.02f);
    }

    @Override
    protected void generateTokens() {
        getTokens().clear();
        for (int i = 0; i < getCardList().size(); i++) {
            int pos = translatePosition(i);
            getTokens().add(new SupportToken(getCardList().get(i), getTokensX() + x + getOffset()*pos + ((pos >= 3) ? TokenType.MS.getWidth()+11f : 0), getTokensY() + y, getBattleStage(), this));
        }
    }

    @Override
    public void generateNewToken(BattleCard battleCard) {
        if (battleCard instanceof Support) {
            Support support = (Support) battleCard;
            int pos = translatePosition(getTokens().size());
            SupportToken supportToken = new SupportToken(support, getTokensX() + x + getOffset()*pos + ((pos >= 3) ? TokenType.MS.getWidth()+11f : 0), getTokensY() + y, getBattleStage(), this);
            getTokens().add(supportToken);
            battleCard.setToken(supportToken);
        }
    }

    public static int translatePosition(int position) {
        switch (position) {
            case 0:
                return 2;
            case 1:
                return 3;
            case 2:
                return 1;
            case 3:
                return 4;
            case 4:
                return 0;
            case 5:
                return 5;
            default:
                return position;
        }
    }

    public static int unTranslatePosition(int position) {
        switch (position) {
            case 0:
                return 4;
            case 1:
                return 2;
            case 2:
                return 0;
            case 3:
                return 1;
            case 4:
                return 3;
            case 5:
                return 5;
            default:
                return position;
        }
    }

    @Override
    public SimpleBox2 getSimpleBox2() { return simpleBox2; }
}
