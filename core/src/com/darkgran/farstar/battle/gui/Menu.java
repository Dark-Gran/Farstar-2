package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.gui.tokens.TokenType;

public interface Menu {
    default void setupOffset() {
        setOffset(TokenType.FLEET.getWidth());
        if (isNegativeOffset()) { setOffset(getOffset()*-1); }
    }

    default boolean isEmpty() {
        return true;
    }

    boolean isNegativeOffset();
    void setNegativeOffset(boolean negativeOffset);
    float getOffset();
    void setOffset(float offset);

}
