package com.dev.carlessandro.eleicaodememoria;

import com.dev.carlessandro.eleicaodememoria.AndGraph.AGSprite;

/**
 * Created carlessandro on 26/11/2018.
 */
public class Carta {

    //Atributos
    private AGSprite corpo;
    private AGSprite verso;
    private int id;
    private boolean click;
    private boolean combine;

    //Construtor
    public Carta(){

    }

    public AGSprite getCorpo() {
        return corpo;
    }

    public void setCorpo(AGSprite corpo) {
        this.corpo = corpo;
    }

    public AGSprite getVerso() {
        return verso;
    }

    public void setVerso(AGSprite verso) {
        this.verso = verso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public boolean isCombine() {
        return combine;
    }

    public void setCombine(boolean combine) {
        this.combine = combine;
    }

}
