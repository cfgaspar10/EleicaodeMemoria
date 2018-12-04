package com.dev.carlessandro.eleicaodememoria;

import com.dev.carlessandro.eleicaodememoria.AndGraph.AGGameManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGInputManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGScene;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGScreenManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGSprite;

/**
 * Created carlessandro on 26/11/2018.
 */
public class Sobre extends AGScene {

    AGSprite titulo = null;
    AGSprite texto = null;

    //Scene construtor
    public Sobre(AGGameManager pManager) { super(pManager); }

    @Override
    public void init() {
        //titulo
        titulo = createSprite(R.drawable.sobrea,1,1);
        titulo.setScreenPercent(75,15);
        titulo.vrPosition.setXY(AGScreenManager.iScreenWidth/2, (float) (AGScreenManager.iScreenHeight*0.9));

        //texto
        texto = createSprite(R.drawable.sobreb,1,1);
        texto.setScreenPercent(90,75);
        texto.vrPosition.setXY(AGScreenManager.iScreenWidth/2, (float) (AGScreenManager.iScreenHeight*0.4));
    }

    @Override
    public void restart() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void loop() {
        if(AGInputManager.vrTouchEvents.screenClicked() || AGInputManager.vrTouchEvents.backButtonClicked()){
            vrGameManager.setCurrentScene(1);
            return;
        }

    }
}
