package com.m2dl.ter.term2dlcarretierlodovicimalet.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Romain on 28/01/2016.
 */
public class Game {

    private int nbAllumette;
    private Map<Integer, IPiege> pieges;
    private Joueur joueurUn;
    private Joueur joueurDeux;

    public Game(int nb, Joueur un, Joueur deux) {
        this(nb, un, deux, new ArrayList<IPiege>());
    }

    public Game(int nb, Joueur un, Joueur deux, List<IPiege> pieges) {
        this.nbAllumette = nb;
        this.joueurUn = un;
        this.joueurDeux = deux;
        randomPositionPiege(pieges);
    }

    private void randomPositionPiege(List<IPiege> pieges) {
        this.pieges = new HashMap<Integer, IPiege>();
        Random rn = new Random();
        for (IPiege piege : pieges) {
            this.pieges.put(rn.nextInt(nbAllumette), piege);
        }
    }

    public int getNbAllumette() {
        return nbAllumette;
    }

    public Joueur getJoueurUn() {
        return joueurUn;
    }

    public Joueur getJoueurDeux() {
        return joueurDeux;
    }

    public boolean isPiege(int pos) {
        return pieges.containsKey(pos);
    }

    public IPiege getPiege(int pos) {
        return pieges.get(pos);
    }
}
