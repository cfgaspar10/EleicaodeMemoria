package com.dev.carlessandro.eleicaodememoria;

import android.os.Bundle;

import com.dev.carlessandro.eleicaodememoria.AndGraph.AGActivityGame;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGGameManager;

/**
 * Created carlessandro on 26/11/2018.
 */
public class Principal extends AGActivityGame {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Objeto responsavel pelo gerencimento de cenas
        vrManager = new AGGameManager(this,false);

        //inicializando as telas das cenas e add o gerenciador
        Abertura abertura = new Abertura(vrManager);
        Menu menu = new Menu(vrManager);
        Jogo jogo = new Jogo(vrManager);
        Sobre sobre = new Sobre(vrManager);
        Ajuda ajuda = new Ajuda(vrManager);

        //registrando as cenas
        vrManager.addScene(abertura);//0
        vrManager.addScene(menu);//1
        vrManager.addScene(jogo);//2
        vrManager.addScene(sobre);//3
        vrManager.addScene(ajuda);//4

    }

}
