package com.m2dl.ter.term2dlcarretierlodovicimalet.model;

/**
 * Created by Thomas on 28/01/2016.
 */
public class AddAllumette implements IPouvoir {

    @Override
    public int[] activer(int position, int nbCoup) {
        return new int[] {position--, nbCoup};
    }

    @Override
    public String getText() {
        return "Ajouter une allumette !";
    }
}
