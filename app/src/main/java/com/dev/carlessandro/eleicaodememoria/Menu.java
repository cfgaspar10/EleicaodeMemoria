package com.dev.carlessandro.eleicaodememoria;

import com.dev.carlessandro.eleicaodememoria.AndGraph.AGGameManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGInputManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGScene;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGScreenManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGSoundManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGSprite;

/**
 * Created carlessandro on 26/11/2018.
 */
public class Menu extends AGScene {

    //ATRIBUTOS
    AGSprite fundo = null;
    AGSprite titulo = null;

    AGSprite btnJogo = null;
    AGSprite btnAjuda = null;
    AGSprite btnSobre = null;
    AGSprite btnSair = null;

    //Temporizador
    int tempo = 1500;

    //Scene construtor
    public Menu(AGGameManager pManager) { super(pManager); }

    @Override
    public void init() {
        AGSoundManager.vrSoundEffects.play(Efeito.nulo);
        this.setSceneBackgroundColor(0,0,0);
        //desenha o plano de fundo
        fundo = createSprite(R.drawable.avatar,1,1);
        fundo.setScreenPercent(100,100);
        fundo.vrPosition.setXY(AGScreenManager.iScreenWidth/2, AGScreenManager.iScreenHeight/2);
        fundo.bVisible = true;

        //titulo
        titulo = createSprite(R.drawable.titulo, 1, 1);
        titulo.setScreenPercent(90, 40);
        titulo.vrPosition.setXY(AGScreenManager.iScreenWidth/2, (float) (AGScreenManager.iScreenHeight*0.75));

        //Cria os elementos viauais (botoões) da esquerda pra direita da tela (15%, 5%)
        btnJogo = createSprite(R.drawable.btnjogar, 1, 1);
        btnJogo.setScreenPercent(40, 10);
        btnJogo.vrPosition.setXY((float) (AGScreenManager.iScreenWidth*0.3), (float) (AGScreenManager.iScreenHeight*0.45));
        //btnJogo.fAlpha = 0.9f;

        btnAjuda = createSprite(R.drawable.btnajuda, 1, 1);
        btnAjuda.setScreenPercent(40, 10);
        btnAjuda.vrPosition.setXY((float) (AGScreenManager.iScreenWidth*0.3), (float) (AGScreenManager.iScreenHeight*0.30));
        //btnAjuda.fAlpha = 0.9f;

        btnSobre = createSprite(R.drawable.btnsobre, 1, 1);
        btnSobre.setScreenPercent(40, 10);
        btnSobre.vrPosition.setXY((float) (AGScreenManager.iScreenWidth*0.3), (float) (AGScreenManager.iScreenHeight*0.15));
        //btnSobre.fAlpha = 0.9f;

        btnSair = createSprite(R.drawable.btnsair, 1, 1);
        btnSair.setScreenPercent(20, 5);
        btnSair.vrPosition.setXY((float) (AGScreenManager.iScreenWidth*0.3), (float) (AGScreenManager.iScreenHeight*0.05));
        //btnSair.fAlpha = 0.9f;

        //Fade-in efeito de aparecer
        btnJogo.fadeIn(tempo);
        btnAjuda.fadeIn(tempo);
        btnSobre.fadeIn(tempo);
        btnSair.fadeIn(tempo);

        //Verifica se as musicas estão rodando
        if(!AGSoundManager.vrMusic.isPlaying()){
            AGSoundManager.vrMusic.loadMusic("menu.mp3",true);
            AGSoundManager.vrMusic.play();
        }

    }

    @Override
    public void restart() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void loop() {
        //Verifica o click nos botões
        if(AGInputManager.vrTouchEvents.screenClicked()){

            if(btnJogo.collide(AGInputManager.vrTouchEvents.getLastPosition()))
            {
                AGSoundManager.vrMusic.stop();
                vrGameManager.setCurrentScene(2);
                return;
            }
            if(btnAjuda.collide(AGInputManager.vrTouchEvents.getLastPosition()))
            {
                vrGameManager.setCurrentScene(4);
                return;
            }
            if(btnSobre.collide(AGInputManager.vrTouchEvents.getLastPosition()))
            {
                vrGameManager.setCurrentScene(3);
                return;
            }
            if(btnSair.collide(AGInputManager.vrTouchEvents.getLastPosition()))
            {
                AGSoundManager.vrMusic.stop();
                vrGameManager.vrActivity.finish();
            }
        }

    }
}
