package com.findyourfamily.app.models.domain;

/**
 * Type for storing biological relation between 2 individuals
 * in the form of counsinship and level of removal.
 */
public class BiologicalRelation {

    //Stores the cousinship level between the 2 individuals.
    private int cousinship;

    //Stores the level of removal between the 2 individuals.
    private int levelOfRemoval;

    public BiologicalRelation(int cousinship, int levelOfRemoval){
        this.cousinship = cousinship;
        this.levelOfRemoval = levelOfRemoval;
    }

    /**
     * Gets the cousinship level between the 2 individuals.
     * @return counsinship level
     */
    public int getCousinship() {
        return cousinship;
    }

    /**
     * Gets the level of removal between the 2 individuals.
     * @return level of removal
     */
    public int getLevelOfRemoval() {
        return levelOfRemoval;
    }
}
