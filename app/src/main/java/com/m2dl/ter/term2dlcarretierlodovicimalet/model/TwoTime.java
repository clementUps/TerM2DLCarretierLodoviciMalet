package com.m2dl.ter.term2dlcarretierlodovicimalet.model;

/**
 * Created by Thomas on 28/01/2016.
 */
public class TwoTime implements IPouvoir {

    @Override
    public int[] activer(int position, int nbCoup) {
        return new int[] {position, nbCoup+3};
    }

    @Override
    public String getText() {
        return "Jouer 2 fois de suite";
    }
}
