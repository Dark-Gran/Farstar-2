package com.darkgran.farstar.gui;

import com.badlogic.gdx.graphics.Color;
import com.darkgran.farstar.Farstar;

public class YXQuestionBox extends TextInTheBox { //TODO
    private final ListeningStage stage;
    private final ActorButton yBut = new ActorButton(Farstar.ASSET_LIBRARY.getAssetManager().get("images/y.png"), Farstar.ASSET_LIBRARY.getAssetManager().get("images/yO.png"), Farstar.ASSET_LIBRARY.getAssetManager().get("images/yO.png")){
        @Override
        public void clicked() {

        }
    };
    private final ActorButton xBut = new ActorButton(Farstar.ASSET_LIBRARY.getAssetManager().get("images/x.png"), Farstar.ASSET_LIBRARY.getAssetManager().get("images/xO.png"), Farstar.ASSET_LIBRARY.getAssetManager().get("images/xO.png")){
        @Override
        public void clicked() {

        }
    };

    public YXQuestionBox(Color fontColor, Color boxColor, String fontPath, String message, float x, float y, float width, float height, boolean noBox, ListeningStage stage) {
        super(fontColor, boxColor, fontPath, message, x, y, width, height, noBox);
        yBut.setPosition(x, y);
        xBut.setPosition(x+xBut.getWidth(), y);
        this.stage = stage;
        stage.addActor(yBut);
        stage.addActor(xBut);
    }

    public void dispose() {
        yBut.remove();
        xBut.remove();
    }

}
