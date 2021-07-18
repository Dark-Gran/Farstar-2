package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.TechType;
import com.darkgran.farstar.battle.players.cards.CardType;

public class TokenDefense extends TokenPart {
    public TokenDefense(String fontPath, Token token) {
        super(fontPath, token);
    }

    @Override
    public boolean isEnabled() {
        if (getToken().getTokenType() != TokenType.JUNK) {
            if (!getContent().equals("0") || !TechType.isInferior(getToken().getCard().getCardInfo().getDefenseType())) {
                return true;
            } else {
                return CardType.needsDefense(getToken().getCard().getCardInfo().getCardType());
            }
        } else {
            return false;
        }
    }

    @Override
    public String getContent() {
        return Integer.toString(getToken().getCard().getHealth());
    }

    @Override
    public void setPad(TokenType tokenType) {
        setPad((Texture) Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getTypePad(getToken().getCard().getCardInfo().getDefenseType(), tokenType)));
    }

    @Override
    public void resetContentState() {
        if (getToken().getCard().isDamaged()) {
            setCurrentContentState(ContentState.DAMAGED);
        } else if (getToken().getCard().hasUpgradedShields()) {
            setCurrentContentState(ContentState.UPGRADED);
        } else {
            setCurrentContentState(ContentState.NORMAL);
        }
    }

}
