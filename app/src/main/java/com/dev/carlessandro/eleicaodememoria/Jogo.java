package com.dev.carlessandro.eleicaodememoria;

import android.util.Log;

import com.dev.carlessandro.eleicaodememoria.AndGraph.AGGameManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGInputManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGScene;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGScreenManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGSoundEffect;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGSoundManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGSprite;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGTimeManager;
import com.dev.carlessandro.eleicaodememoria.AndGraph.AGTimer;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created carlessandro on 26/11/2018.
 */
public class Jogo extends AGScene {

    //temporaziadores
    AGTimer temporizador;
    AGTimer cSom;
    AGTimer cFinal;

    //Objetos das cartas
    Carta carta;
    ArrayList<Carta> cartasLista;
    AGSprite auxVerso;
    AGSprite auxFrente;
    int cartaSizeX;
    int cartaSizeY;

    //botões
    AGSprite btnRecomeco;
    AGSprite btnVoltar;
    AGSprite fundo;

    //largura e altura
    int maxSizeX;
    int maxSizeY;

    //Define a quantidade de linhas e colunas
    int linha = 4;
    int coluna = 0;
    //Pega a posição inicial de x e y, vai colocar as posições de acorod com o conjunto de coordenadas
    int desHY = 0;
    int desWX = 0;
    int posHY = 0;
    int posWX = 0;

    //variaveis de controle
    boolean jogando;
    boolean saiu;
    boolean tocamSom;
    int auxIdentificador;
    boolean clickBackbutton;

    //variaveis de controle do click
    ArrayList<Integer> paramArray;
    ArrayList<Integer> idFigura;
    int cont;
    int acertos;


    //Scene construtor
    public Jogo(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {
        //cor de fundo preta
        this.setSceneBackgroundColor(0, 0, 0);

        //temporizadores
        temporizador = new AGTimer(800);
        cFinal = new AGTimer(2000);
        cSom = new AGTimer(1);

        //tamanho das cartas
        cartaSizeX = 25;
        cartaSizeY = 20;

        //tamanho da tela
        maxSizeX = AGScreenManager.iScreenWidth;
        maxSizeY = AGScreenManager.iScreenHeight;

        //Define a quantidade de linhas e colunas
        linha = 4;
        coluna = 0;

        //Pega a posição inicial de x e y, vai colocar as posições de acorod com o conjunto de coordenadas
        //Usa um exemplo unico antes de criar as cartas
        AGSprite cartaSample = createSprite(R.drawable.verso, 1, 1);
        cartaSample.setScreenPercent(cartaSizeX, cartaSizeY);
        cartaSample.bVisible = false;

        //centro da renderização
        desHY = (int) (cartaSample.getSpriteHeight() / 2);
        desWX = (int) (cartaSample.getSpriteWidth() / 2);

        iniciaJogo(false);

        //fundo do final
        fundo = createSprite(R.drawable.fdfpreto, 1, 1);
        fundo.setScreenPercent(100, 100);
        fundo.fAlpha = 0.4f;
        fundo.vrPosition.setXY(maxSizeX / 2, maxSizeY / 2);
        fundo.bVisible = false;

        //botoes
        btnRecomeco = createSprite(R.drawable.btnreiniciar,1,1);
        btnRecomeco.setScreenPercent(50,10);
        btnRecomeco.vrPosition.setXY((float)(AGScreenManager.iScreenWidth*0.5),(float)(AGScreenManager.iScreenHeight*0.6));
        btnRecomeco.bVisible = false;

        btnVoltar = createSprite(R.drawable.btnvoltar,1,1);
        btnVoltar.setScreenPercent(50,10);
        btnVoltar.vrPosition.setXY((float)(AGScreenManager.iScreenWidth*0.5),(float)(AGScreenManager.iScreenHeight*0.4));
        btnVoltar.bVisible = false;

    }

    @Override
    public void restart() {}

    @Override
    public void stop() {}

    @Override
    public void loop() {
        //Botão de voltar para voltar ao menu, pois aqui não há botão de voltar implementado
        if(!clickBackbutton){
            if (AGInputManager.vrTouchEvents.backButtonClicked() || saiu) {
                //inicia o loop de saida e toca a musica da vergonha
                if(!saiu){
                    AGSoundManager.vrMusic.stop();
                    AGSoundManager.vrMusic.loadMusic("lose.mp3",false);
                    AGSoundManager.vrMusic.play();
                    cFinal = new AGTimer(7500);
                    saiu = true;
                }
                cFinal.update();
                if(cFinal.isTimeEnded()){
                    clickBackbutton = true;
                    //chama os botões do fim do jogo
                    mostraFim(true);
                }
            }
        }
        //Click das cartas, prevenindo o click caso o jogo esteja parado, se estiver só habilita os clicks dos botões
        if(!saiu){
            if (AGInputManager.vrTouchEvents.screenClicked() && cont < 2) {
                //pra não deixar clicar caso o jogo acabe
                if (cont < 2 && jogando) {
                    for (int pos = 0; pos < cartasLista.size(); pos++) {
                        //analiza qual posição foi clicada
                        if (cartasLista.get(pos).getVerso().collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                            auxIdentificador = pos;
                            //chama o metodo de checar se a carta esta virada
                            cartaVirada(auxIdentificador);
                        }
                    }
                }
            }
            //quando clica em duas cartas ele faz a verificação dos pares
            if (cont == 2 && !tocamSom) {
                temporizador.update();
                if (temporizador.isTimeEnded()) {
                    controladorJogo();
                    tocamSom = true;
                }
            }
            //toca o som  e espera parar
            if(tocamSom){
                cSom.update();
                if(cSom.isTimeEnded()){
                    tocamSom = false;
                    desvira();
                }
            }
            //Quando terminar o jogo entra aqui, com 10 acertos
            if (acertos == 10 && !tocamSom) {
                //AGSoundManager.vrMusic.stop();
                //tocar a musica da gloria
                if (jogando) {
                    //modifica o estado do jogo
                    jogando = false;
                    AGSoundManager.vrMusic.stop();
                    AGSoundManager.vrMusic.loadMusic("win.mp3",false);
                    AGSoundManager.vrMusic.play();
                    cFinal = new AGTimer(9500);
                }
                cFinal.update();
                if (cFinal.isTimeEnded()) {
                    saiu = true;
                    mostraFim(true);
                }
            }
        } else {
            //click dos botões reiniciar e voltar
            if(AGInputManager.vrTouchEvents.screenClicked()) {
                if (btnRecomeco.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                    mostraFim(false);
                    iniciaJogo(true);
                }
                if (btnVoltar.collide(AGInputManager.vrTouchEvents.getLastPosition())) {
                    //volta para a tela do menu
                    vrGameManager.setCurrentScene(1);
                    return;
                }
            }

        }
    }

    //Metodos controladores do jogo
    //inicia o jogo: permite o reinicio do jogo
    private void iniciaJogo(boolean reinicio){
        AGSoundManager.vrMusic.loadMusic("game.mp3",true);
        AGSoundManager.vrMusic.play();
        if(reinicio){
            for(Carta c: cartasLista){
                c.setClick(false);
                c.setCombine(false);
                c.getCorpo().bVisible = false;
                c.getVerso().bVisible = true;
                c.getCorpo().vrPosition.setXY(posWX,posHY);
            }
        } else {
            //cria a lista de cartas
            cartasLista = new ArrayList<>();
            //Cria as cartas e as sorteia
            carregarCartas();
        }
        //reembaralha
        Collections.shuffle(cartasLista);
        AGSoundManager.vrSoundEffects.play(Efeito.embaralhar);
        //LAÇO RENDERIZADOR
        //posição iniciai
        posHY = desHY;
        posWX = desWX;
        for (Carta cd : cartasLista) {
            cd.getCorpo().vrPosition.setXY(posWX, posHY);
            cd.getVerso().vrPosition.setXY(posWX, posHY);
            posWX += (desWX * 2);
            coluna++;
            if (coluna == linha) {
                posHY += (desHY * 2);
                posWX = (desWX);
                coluna = 0;
            }
        }
        //Identificador que irá auxiliar durante o jogo
        auxIdentificador = -1;
        jogando = true;
        acertos = 0;
        cont = 0;
        idFigura = new ArrayList<>();
        paramArray = new ArrayList<>();
        saiu = false;
        tocamSom = false;
        clickBackbutton = false;

    }

    //Carregar a lista de cartas
    private void carregarCartas() {
        //variaveis controladoreas
        int i, par, card, som;
        card = 1;
        som = 0;
        par = 0;

        for (i = 0; i < 20; i++) {
            //cria um novo sprite do verso
            auxVerso = createSprite(R.drawable.verso, 1, 1);
            auxVerso.setScreenPercent(cartaSizeX, cartaSizeY);
            auxVerso.bVisible = true;

            //Cria um novo objeto da carta, elementos em comum são adicionados primeiro
            carta = new Carta();
            carta.setVerso(auxVerso);
            carta.setClick(false);
            carta.setCombine(false);
            auxFrente = null;

            //Incrementa os ids a cada duas figuras
            if (par == 2) {
                card++;
                som++;
                par = 0;
            }
            par++;
            //chama de acordo com a posição, e adiciona no arraylists
            if (card == 1) {
                //Bolsonaro
                auxFrente = createSprite(R.drawable.c01, 1, 1);
            } else if (card == 2) {
                //haddad
                auxFrente = createSprite(R.drawable.c02, 1, 1);;
            } else if (card == 3) {
                //cangaciro
                auxFrente = createSprite(R.drawable.c03, 1, 1);
            } else if (card == 4) {
                //marina
                auxFrente = createSprite(R.drawable.c04, 1, 1);
            } else if (card == 5) {
                //boulos
                auxFrente = createSprite(R.drawable.c05, 1, 1);
            } else if (card == 6) {
                //temer
                auxFrente = createSprite(R.drawable.c06, 1, 1);
            } else if (card == 7) {
                //alckmin
                auxFrente = createSprite(R.drawable.c07, 1, 1);
            } else if (card == 8) {
                //daciolo
                auxFrente = createSprite(R.drawable.c08, 1, 1);
            } else if (card == 9) {
                //mourao
                auxFrente = createSprite(R.drawable.c09, 1, 1);
            } else if (card == 10) {
                //jean
                auxFrente = createSprite(R.drawable.c10, 1, 1);
            }
            auxFrente.setScreenPercent(cartaSizeX, cartaSizeY);
            auxFrente.bVisible = false;
            carta.setCorpo(auxFrente);
            carta.setId(card);
            cartasLista.add(carta);
        }
    }

    //Verificar carta virada
    private void cartaVirada(int cod) {
        //só faz a ação se a carta não estiver clicada
        if (!cartasLista.get(cod).isClick()) {
            AGSoundManager.vrSoundEffects.play(Efeito.viracarta);
            cartasLista.get(cod).setClick(true);
            cartasLista.get(cod).getVerso().bVisible = false;
            cartasLista.get(cod).getCorpo().bVisible = true;
            idFigura.add(cartasLista.get(cod).getId());
            paramArray.add(cod);
            cont++;
        }
        //reinicia o temporizador que mostra as cartas
        if (cont == 2)
            temporizador.restart();

    }

    //Metodo que vai controlar o jogo de maneira geral, chamando metodos auxiliares quando necessário
    private void controladorJogo() {
        //verifica se é os ids são iguais
        if (idFigura.get(0) == idFigura.get(1)) {
            somAcerto(idFigura.get(1));
            acertos += 1;
        }
        //se ele errar, vai vira e toca o som de erro da ultima figura
        else {
            somErro(idFigura.get(1));
        }

    }

    //habilita o plano de fundo do jogo
    private void mostraFim(boolean exibir){
        fundo.bVisible = exibir;
        btnRecomeco.bVisible = exibir;
        btnVoltar.bVisible = exibir;
        if(exibir && acertos < 10){
            for (Carta cartas: cartasLista) {
                cartas.getVerso().bVisible = false;
                cartas.getCorpo().bVisible = true;
            }
        }

    }

    //desvira as cartas
    private void desvira(){
        if (idFigura.get(0) == idFigura.get(1)) {
            cartasLista.get(paramArray.get(0)).setCombine(true);
            cartasLista.get(paramArray.get(1)).setCombine(true);
        }
        //se ele errar, vai vira e toca o som de erro da ultima figura
        else {
            cartasLista.get(paramArray.get(0)).getCorpo().bVisible = false;
            cartasLista.get(paramArray.get(0)).getVerso().bVisible = true;
            cartasLista.get(paramArray.get(0)).setClick(false);
            cartasLista.get(paramArray.get(1)).getCorpo().bVisible = false;
            cartasLista.get(paramArray.get(1)).getVerso().bVisible = true;
            cartasLista.get(paramArray.get(1)).setClick(false);

        }
        //zera os arrays
        idFigura.clear();
        paramArray.clear();
        cont = 0;
    }

    //solta o efeito sonoro de acerto
    private void somAcerto(int card){
        if(card == 1){
            AGSoundManager.vrSoundEffects.play(Efeito.bolsonaroAcerto);
            cSom.restart(Efeito.bolsonaroAcertoDur);
        }
        if(card == 2){
            AGSoundManager.vrSoundEffects.play(Efeito.haddadAcerto);
            cSom.restart(Efeito.haddadAcertoDur);
        }
        if(card == 3){
            AGSoundManager.vrSoundEffects.play(Efeito.ciroAcerto);
            cSom.restart(Efeito.ciroAcertoDur);
        }
        if(card == 4){
            AGSoundManager.vrSoundEffects.play(Efeito.marinaAcerto);
            cSom.restart(Efeito.marinaAcertoDur);
        }
        if(card == 5){
            AGSoundManager.vrSoundEffects.play(Efeito.boulosAcerto);
            cSom.restart(Efeito.boulosAcertoDur);
        }
        if(card == 6){
            AGSoundManager.vrSoundEffects.play(Efeito.temerAcerto);
            cSom.restart(Efeito.temerAcertoDur);
        }
        if(card == 7){
            AGSoundManager.vrSoundEffects.play(Efeito.alckminAcerto);
            cSom.restart(Efeito.alckminAcertoDur);
        }
        if(card == 8){
            AGSoundManager.vrSoundEffects.play(Efeito.dacioloAcerto);
            cSom.restart(Efeito.dacioloAcertoDur);
        }
        if(card == 9){
            AGSoundManager.vrSoundEffects.play(Efeito.mouraoAcerto);
            cSom.restart(Efeito.mouraoAcertoDur);
        }
        if(card == 10){
            AGSoundManager.vrSoundEffects.play(Efeito.jeanAcerto);
            cSom.restart(Efeito.jeanAcertoDur);
        }
    }

    //solta o efeito sonoro de erro
    private void somErro(int card){
        if(card == 1){
            AGSoundManager.vrSoundEffects.play(Efeito.bolsonaroErro);
            cSom.restart(Efeito.bolsonaroErroDur);
        }
        if(card == 2){
            AGSoundManager.vrSoundEffects.play(Efeito.haddadErro);
            cSom.restart(Efeito.haddadErroDur);
        }
        if(card == 3){
            AGSoundManager.vrSoundEffects.play(Efeito.ciroErro);
            cSom.restart(Efeito.ciroErroDur);
        }
        if(card == 4){
            AGSoundManager.vrSoundEffects.play(Efeito.marinaErro);
            cSom.restart(Efeito.marinaErroDur);
        }
        if(card == 5){
            AGSoundManager.vrSoundEffects.play(Efeito.boulosErro);
            cSom.restart(Efeito.boulosErroDur);
        }
        if(card == 6){
            AGSoundManager.vrSoundEffects.play(Efeito.temerErro);
            cSom.restart(Efeito.temerErroDur);
        }
        if(card == 7){
            AGSoundManager.vrSoundEffects.play(Efeito.alckminErro);
            cSom.restart(Efeito.alckminErroDur);
        }
        if(card == 8){
            AGSoundManager.vrSoundEffects.play(Efeito.dacioloErro);
            cSom.restart(Efeito.dacioloErroDur);
        }
        if(card == 9){
            AGSoundManager.vrSoundEffects.play(Efeito.mouraroErro);
            cSom.restart(Efeito.mouraroErroDur);
        }
        if(card == 10){
            AGSoundManager.vrSoundEffects.play(Efeito.jeanErro);
            cSom.restart(Efeito.jeanErroDur);
        }

    }

}