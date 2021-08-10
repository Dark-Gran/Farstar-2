package com.darkgran.farstar.gui.tokens;

import com.darkgran.farstar.cards.TechType;
import com.darkgran.farstar.cards.CardType;
import com.darkgran.farstar.gui.AssetLibrary;

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
    public void setupOffset() { //in-future: some fonts seem to be highly imperfect (either in origin or by import)... fix (= probably font edit)
        super.setupOffset();
        if (getToken().getTokenType() == TokenType.PRINT || getToken().getTokenType() == TokenType.SUPPORT || getToken().getTokenType() == TokenType.HAND) {
            if (getContent().equals("0")) {
                setTextOffsetX(1f);
            }
        }
    }

    @Override
    public String getContent() {
        return Integer.toString(getToken().getCard().getHealth());
    }

    @Override
    public void setPad(TokenType tokenType) {
        setPad(AssetLibrary.getInstance().getAtlasRegion(AssetLibrary.getInstance().getTypePad(getToken().getCard().getCardInfo().getDefenseType(), tokenType)));
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
