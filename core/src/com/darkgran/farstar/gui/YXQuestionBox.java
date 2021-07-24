package com.darkgran.farstar.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.util.SimpleVector2;

public class YXQuestionBox extends TextInTheBox {
    private final ListeningStage stage;
    private final Runnable runY;
    private final Runnable runX;
    private final ActorButton yBut = new ActorButton(Farstar.ASSET_LIBRARY.getAtlasRegion("y"), Farstar.ASSET_LIBRARY.getAtlasRegion("yO"), Farstar.ASSET_LIBRARY.getAtlasRegion("yO")){
        @Override
        public void clicked() {
            Gdx.app.postRunnable(runY);
        }
    };
    private final ActorButton xBut = new ActorButton(Farstar.ASSET_LIBRARY.getAtlasRegion("x"), Farstar.ASSET_LIBRARY.getAtlasRegion("xO"), Farstar.ASSET_LIBRARY.getAtlasRegion("xO")){
        @Override
        public void clicked() {
            Gdx.app.postRunnable(runX);
        }
    };

    public YXQuestionBox(Color fontColor, Color boxColor, String fontPath, String message, float x, float y, float width, float height, boolean noBox, ListeningStage stage, Runnable runX, Runnable runY) {
        super(fontColor, boxColor, fontPath, message, x, y, width, height, noBox);
        this.stage = stage;
        this.runX = runX;
        this.runY = runY;
        SimpleVector2 textWH = TextDrawer.getTextWH(getFont(), message);
        yBut.setPosition(x+textWH.x/2-yBut.getWidth(), y-textWH.y*4);
        xBut.setPosition(x+textWH.x/2, y-textWH.y*4);
        stage.addActor(yBut);
        stage.addActor(xBut);
    }

    @Override
    public void centralizeBox() {
        SimpleVector2 boxOrigin = new SimpleVector2(
                (this.x + TextDrawer.getTextWH(getFont(), getText()).x/2) - getSimpleBox().getWidth()/2,
                (this.y - TextDrawer.getTextWH(getFont(), getText()).y*5/3) - getSimpleBox().getHeight()/2);
        setupBox(boxOrigin.x, boxOrigin.y, getSimpleBox().getWidth(), getSimpleBox().getHeight());
    }

    public void dispose() {
        yBut.remove();
        xBut.remove();
    }

}
