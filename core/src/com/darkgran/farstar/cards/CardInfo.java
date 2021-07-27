package com.darkgran.farstar.cards;

import java.util.ArrayList;

public class CardInfo {
    private final byte id;
    private final String name;
    private final CardCulture culture;
    private String description;
    private final CardType cardType;
    private final CardRarity cardRarity;
    private final int tier;
    private int energy; //resource-price
    private int matter; //resource-price
    private int offense;
    private int defense;
    private TechType offenseType;
    private TechType defenseType;
    private final ArrayList<AbilityInfo> abilities;
    private final int animatedShots;

    public CardInfo(byte id, String name, CardCulture culture, String description, CardType cardType, CardRarity cardRarity, int tier, int energy, int matter, int offense, int defense, TechType offenseType, TechType defenseType, ArrayList<AbilityInfo> abilities, int animatedShots) {
        this.id = id;
        this.name = name;
        this.culture = culture;
        this.description = description;
        this.cardType = cardType;
        this.cardRarity = cardRarity;
        this.energy = energy;
        this.matter = matter;
        this.offense = offense;
        this.defense = defense;
        this.offenseType = offenseType;
        this.defenseType = defenseType;
        this.abilities = instanceAbilities(abilities);
        this.tier = tier;
        this.animatedShots = animatedShots;
    }

    public CardInfo() {
        this.id = 0;
        this.name = "X";
        this.culture = CardCulture.NEUTRAL;
        this.description = "";
        this.cardType = CardType.ACTION;
        this.cardRarity = CardRarity.IRON;
        this.tier = 0;
        this.energy = 0;
        this.matter = 0;
        this.offense = 0;
        this.defense = 0;
        this.offenseType = TechType.INFERIOR;
        this.defenseType = TechType.INFERIOR;
        this.abilities = new ArrayList<>();
        this.animatedShots = 0;
    }

    private ArrayList<AbilityInfo> instanceAbilities(ArrayList<AbilityInfo> abilities) {
        return new ArrayList<>(abilities);
    }

    public byte getId() { return id; }

    public String getName() { return name; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CardType getCardType() { return cardType; }

    public CardRarity getCardRarity() { return cardRarity; }

    public int getEnergy() { return energy; }

    public int getMatter() { return matter; }

    public int getOffense() { return offense; }

    public int getDefense() { return defense; }

    public void setEnergy(int energy) { this.energy = energy; }

    public void setMatter(int matter) { this.matter = matter; }

    public void setOffense(int offense) { this.offense = offense; }

    public void changeOffense(int change) { this.offense += change; }

    public void changeDefense(int change) { this.defense += change; }

    public void setDefense(int defense) { this.defense = defense; }

    public TechType getOffenseType() { return offenseType; }

    public void setOffenseType(TechType offenseType) { this.offenseType = offenseType; }

    public TechType getDefenseType() { return defenseType; }

    public void setDefenseType(TechType defenseType) { this.defenseType = defenseType; }

    public ArrayList<AbilityInfo> getAbilities() { return abilities; }

    public void addAbility(AbilityInfo abilityInfo) { abilities.add(abilityInfo); }

    public int getTier() { return tier; }

    public CardCulture getCulture() {
        return culture;
    }

    public int getAnimatedShots() {
        return animatedShots;
    }
}
