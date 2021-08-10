package com.darkgran.farstar.cards;

import com.darkgran.farstar.gui.tokens.Token;

public abstract class Card {
    private Token token;
    private final CardInfo cardInfo;
    private final CardInfo originalInfo;
    private boolean possible; //for PossibilityAdvisor

    public Card(CardInfo cardInfo) {
        originalInfo = cardInfo;
        this.cardInfo = instanceCardInfo(originalInfo);
    }

    public Card() {
        originalInfo = CardLibrary.getInstance().getCard(0);
        cardInfo = instanceCardInfo(originalInfo);
    }

    public Card(int id) {
        originalInfo = CardLibrary.getInstance().getCard(id);
        cardInfo = instanceCardInfo(originalInfo);
    }

    public void refreshToken(boolean def, boolean off, boolean abi) { }

    private CardInfo instanceCardInfo(CardInfo cardInfo) {
        return new CardInfo(cardInfo.getId(), cardInfo.getName(), cardInfo.getCulture(), cardInfo.getDescription(), cardInfo.getCardType(), cardInfo.getCardRarity(), cardInfo.getTier(), cardInfo.getEnergy(), cardInfo.getMatter(), cardInfo.getOffense(), cardInfo.getDefense(), cardInfo.getOffenseType(), cardInfo.getDefenseType(), cardInfo.getAbilities(), cardInfo.getAnimatedShots());
    }

    public boolean isPurelyOffensiveChange() {
        boolean foundOffense = false;
        for (AbilityInfo ability : cardInfo.getAbilities()) {
            if (ability.isPurelyOffensiveChange()) {
                foundOffense = true;
            } else {
                return false;
            }
        }
        return foundOffense;
    }

    public boolean isTactic() { return cardInfo.getCardType() == CardType.TACTIC; }

    public boolean isMS() { return cardInfo.getCardType() == CardType.MS; }

    public void setPossible(boolean possible) {
        this.possible = possible;
    }

    public boolean isPossible() { return possible; }

    public CardInfo getCardInfo() { return cardInfo; }

    public CardInfo getOriginalInfo() { return originalInfo; }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

}
