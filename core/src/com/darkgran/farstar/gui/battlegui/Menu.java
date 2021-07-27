package com.darkgran.farstar.gui.battlegui;

import com.darkgran.farstar.gui.tokens.TokenType;

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
