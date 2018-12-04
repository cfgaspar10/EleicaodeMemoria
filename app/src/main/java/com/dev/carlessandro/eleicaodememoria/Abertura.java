package com.dev.carlessandro.eleicaodememoria;

import com.dev.carlessandro.eleicaodememoria.AndGraph.AGGameManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGInputManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGScene;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGScreenManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGSoundManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGSprite;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGTimer;

/**
 * Created carlessandro on 26/11/2018.
 */
public class Abertura extends AGScene {

    //varivaveis de controle
    AGTimer temporizador = null;
    AGSprite logoSi = null;
    int tempo;

    //Construtor da cena
    public Abertura(AGGameManager pManager) { super(pManager);  }

    //Chama a cena e desenha os logos
    @Override
    public void init() {
        //cor de fundo da cena vermelha
        this.setSceneBackgroundColor(1,1,1);
        AGSoundManager.vrMusic.loadMusic("menu.mp3",true);
        AGSoundManager.vrMusic.play();

        //maximo X e Y da tela
        int x = AGScreenManager.iScreenWidth;
        int y = AGScreenManager.iScreenHeight;

        //cria temporizador de 8 segundos
        tempo = 4000;
        temporizador = new AGTimer(tempo);

        //logo SI
        logoSi = createSprite(R.drawable.logosi,1,1);
        logoSi.setScreenPercent(50,30);
        logoSi.vrPosition.setXY(x/2,y/2);
        logoSi.fadeIn(tempo/2);

        //AGSoundManager.vrMusic.loadMusic("menuTheme.mp3",true);
        //AGSoundManager.vrMusic.play();
    }

    @Override
    public void restart() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void loop() {
        temporizador.update();

        if( logoSi.fadeEnded()){
            logoSi.fadeOut((tempo/2)-200);
        }

        if (temporizador.isTimeEnded()){
            vrGameManager.setCurrentScene(1);
            return;
        }

        if (AGInputManager.vrTouchEvents.screenClicked()){
            vrGameManager.setCurrentScene(1);
            return;
        }
    }
}
