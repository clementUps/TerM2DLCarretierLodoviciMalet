package com.m2dl.ter.term2dlcarretierlodovicimalet.model;

/**
 * Created by Romain on 28/01/2016.
 */
public class Joueur {

    private String name;
    private IPouvoir pouvoir;

    public Joueur(String name) {
        this(name, null);
    }

    public Joueur(String name, IPouvoir pouvoir) {
        this.name = name;
        this.pouvoir = pouvoir;
    }

    public String getName() {
        return name;
    }

    public IPouvoir getPouvoir() {
        return pouvoir;
    }
}
