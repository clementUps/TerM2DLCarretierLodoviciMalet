package com.m2dl.ter.term2dlcarretierlodovicimalet.model;

/**
 * Created by Romain on 28/01/2016.
 */
public interface IPlay {

    void start();

    void joueurJoue(Joueur joueur);

    void joueurActivePouvoir(Joueur joueur);

    void end();
}
