package com.example.jamilissokolowicz.tp8;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.FloatRange;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import org.cocos2d.actions.instant.CallFuncN;
import org.cocos2d.actions.interval.IntervalAction;
import org.cocos2d.actions.interval.JumpBy;
import org.cocos2d.actions.interval.MoveBy;
import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.actions.interval.ScaleBy;
import org.cocos2d.actions.interval.ScaleTo;
import org.cocos2d.actions.interval.Sequence;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.CocosNode;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCSize;

import java.util.Random;

public class clsJuego {

    CCGLSurfaceView _VistaDelJuego;
    CCSize PantallaDelDispositivo;

    Context _Contexto;

    Sprite wallpaper;
    Sprite nube;
    Sprite gota;
    Sprite sol;
    MenuItemImage botonComenzarJuego;

    Sprite palo1, paloo1, palo2, paloo2;

    boolean dejaDeLlover = false;
    int nivel = 0;
    MediaPlayer mpMusicaFondo;
    Menu menuBotones;

    Sprite miArcoiris;
    float ubicacionArcoirisXPRINCIPIO, ubicacionArcoirisYPRINCIPIO;
    boolean moverArcoiris;
    Sprite llegada;

    boolean yaJuega=false;


    public clsJuego(CCGLSurfaceView VistaDelJuego, Context contextop){
        _VistaDelJuego = VistaDelJuego;
        _Contexto = contextop;
    }

    public void ComenzarJuego(){
        Director.sharedDirector().attachInView(_VistaDelJuego);

        PantallaDelDispositivo = Director.sharedDirector().displaySize();

        Director.sharedDirector().runWithScene(EscenaDelJuego());
    }

    private Scene EscenaDelJuego(){
        Scene escenaDevolver = Scene.node();

        CapaFondoWallpaper miCapaWallpaper = new CapaFondoWallpaper();
        CapaFondoNubesLluvia miCapaFondoLluvia = new CapaFondoNubesLluvia();
        CapaJuego miCapaFrenteJuego = new CapaJuego();

        escenaDevolver.addChild(miCapaWallpaper, -20);
        escenaDevolver.addChild(miCapaFondoLluvia, -10);
        escenaDevolver.addChild(miCapaFrenteJuego, 10);

        return escenaDevolver;
    }

    class CapaFondoWallpaper extends Layer{
        public CapaFondoWallpaper() {
            PonerWallpaper();
            mpMusicaFondo = MediaPlayer.create(_Contexto, R.raw.lluvia);
            mpMusicaFondo.start();
            mpMusicaFondo.setVolume(0.5f, 0.5f);
            mpMusicaFondo.setLooping(true);
        }
        private void PonerWallpaper(){
            wallpaper = Sprite.sprite("fondoprincipal.jpg");
            wallpaper.setPosition(PantallaDelDispositivo.getWidth()/2, PantallaDelDispositivo.getHeight()/2);

            float factorAncho, factorAlto;
            factorAncho = PantallaDelDispositivo.width/wallpaper.getWidth();
            factorAlto = PantallaDelDispositivo.height/wallpaper.getHeight();
            wallpaper.runAction(ScaleBy.action(0.01f, factorAncho, factorAlto));

            super.addChild(wallpaper);
        }
    }

    class CapaFondoNubesLluvia extends Layer {
        public CapaFondoNubesLluvia() {
            PonerTodo();
        }
        public void PonerTodo(){
            Log.d("PonerTodo", "antes de llamar a lluvia por primera vez");
            Lluvia(3f);
            Log.d("PonerTodo", "antes del schudle");
            Random rand = new Random();
            int tiempoSchudle = rand.nextInt(1);

            super.schedule("Lluvia", tiempoSchudle+0.25f);

            nube = Sprite.sprite("nube.png");
            nube.setPosition(PantallaDelDispositivo.getWidth()/2, (PantallaDelDispositivo.getHeight()-(PantallaDelDispositivo.getHeight()/4)));
            super.removeChild(nube, true);
            super.addChild(nube);
        }
        public void Lluvia(float deltaTiempo){
            if(!dejaDeLlover){
                Random random = new Random();
                int tiempoEntreGotas = random.nextInt(2);
                tiempoEntreGotas+=1;

                random = new Random();
                int posX = random.nextInt(180);
                posX -= 90;
                posX += PantallaDelDispositivo.getWidth()/2;

                gota = Sprite.sprite("gota.png");
                gota.setPosition(posX, (PantallaDelDispositivo.getHeight()-(PantallaDelDispositivo.getHeight()/4))-50);
                super.addChild(gota);

                gota.runAction(MoveTo.action(tiempoEntreGotas, posX, -100));

            }
        }
    }

    class CapaJuego extends Layer {
        public CapaJuego() {
            PonerBoton();
            menuBotones = Menu.menu(botonComenzarJuego);
            menuBotones.setPosition(PantallaDelDispositivo.getWidth() / 2, PantallaDelDispositivo.getHeight() / 2);
            super.addChild(menuBotones);
            this.setIsTouchEnabled(true); //Habilito touch
        }

        private void PonerBoton() {
            botonComenzarJuego = MenuItemImage.item("botoncomenzarjuego.png", "botoncomenzarjuegopresionado.png", this, "PresionaBotonComenzarJuego");

            float posicionBotonX, posicionBotonY;
            posicionBotonX = 0;
            posicionBotonY = 0;
            botonComenzarJuego.setPosition(posicionBotonX, posicionBotonY);
        }

        public void PresionaBotonComenzarJuego() {
            Log.d("PresionaBoton", "entro");
            dejaDeLlover = true;
            yaJuega=true;

            MoveTo saleElSol, seVaAlCostado;
            ScaleTo seAchica;
            IntervalAction secuencia;

            nube.runAction(MoveTo.action(3, -150, -150));
            super.addChild(nube);

            super.removeChild(menuBotones, true);

            sol = Sprite.sprite("soll.png");
            sol.setPosition(800, 1000);
            super.addChild(sol);

            CallFuncN finDeSecuencia = CallFuncN.action(this, "FinDelTrayecto");

            saleElSol = MoveTo.action(2.5f, PantallaDelDispositivo.getWidth() / 2, PantallaDelDispositivo.getHeight() / 2);
            seAchica = ScaleTo.action(0.5f, 0.5f, 0.5f);
            seVaAlCostado = MoveTo.action(1.5f, 110, PantallaDelDispositivo.getHeight() - 110);

            secuencia = Sequence.actions(saleElSol, seAchica, seVaAlCostado, finDeSecuencia);
            sol.runAction(secuencia);
        }

        private void nivel1() {
            nivel = 1;
            palo1 = Sprite.sprite("paloNivel1.png");
            palo1.setPosition(PantallaDelDispositivo.getWidth() / 4 + 30, (palo1.getHeight() / 2));
            super.addChild(palo1);

            paloo1 = Sprite.sprite("paloNivel1.png");
            paloo1.setPosition(PantallaDelDispositivo.getWidth() / 4 + 30, ((paloo1.getHeight() / 4) * 3) + 100);
            super.addChild(paloo1);

            palo2 = Sprite.sprite("paloNivel1.png");
            palo2.setPosition(((PantallaDelDispositivo.getWidth() / 4)*3) + 30, (palo2.getHeight() / 2));
            super.addChild(palo2);

            paloo2 = Sprite.sprite("paloNivel1.png");
            paloo2.setPosition(((PantallaDelDispositivo.getWidth() / 4)*3) + 30, ((paloo2.getHeight() / 4) * 3) + 100);
            super.addChild(paloo2);

            Log.d("nivel1", "la posicion de paloo2 es x:"+paloo2.getPositionX()+" y:"+paloo2.getPositionY());
            Log.d("nivel1", "la posicion de paloo1 es x:"+paloo1.getPositionX()+" y:"+paloo1.getPositionY());

            ubicacionArcoirisXPRINCIPIO = ((palo2.getPositionX()-palo1.getPositionX())/2)+palo1.getPositionX();
            ubicacionArcoirisYPRINCIPIO = 100;
            ubicarArcoirisMasLlegada(ubicacionArcoirisXPRINCIPIO, ubicacionArcoirisYPRINCIPIO, "uno.png", "unoWIN.png");
        }

        private void ubicarArcoirisMasLlegada(Float x, float y, String nombreArchivoArcoiris, String nombreArchivoLlegada) {
            miArcoiris = Sprite.sprite(nombreArchivoArcoiris);
            miArcoiris.setPosition(x, y);
            super.addChild(miArcoiris);

            llegada = Sprite.sprite(nombreArchivoLlegada);
            llegada.setPosition(x, PantallaDelDispositivo.getHeight()-100);
            super.addChild(llegada);

            Log.d("nivel1", "la posicion de arco es x:"+miArcoiris.getPositionX()+" y:"+miArcoiris.getPositionY());
        }

        public void FinDelTrayecto(CocosNode objeto) {
            nivel1();
        }


        boolean EstoyDentroDeArcoiris(float miXdondeToco, float miyDondeToco){
            boolean estoyDentro = false;

            int SpriteIzquierda, SpriteDerecha, SpriteAbajo, SpriteArriba;
            SpriteIzquierda =(int) (miArcoiris.getPositionX() - miArcoiris.getWidth()/2);
            SpriteDerecha =(int) (miArcoiris.getPositionX() + miArcoiris.getWidth()/2);
            SpriteAbajo =(int) (miArcoiris.getPositionY() - miArcoiris.getHeight()/2);
            SpriteArriba =(int) (miArcoiris.getPositionY() + miArcoiris.getHeight()/2);

            if(EstaEntre(miXdondeToco, SpriteDerecha, SpriteIzquierda) && EstaEntre(miyDondeToco, SpriteArriba, SpriteAbajo)){
                estoyDentro = true;
            }

            return estoyDentro;
        }

        boolean EstaEntre (float numAComparar, int numMenor, int numMayor){
            boolean devolver;
            if(numMenor>numMayor){
                int aux = numMayor;
                numMayor = numMenor;
                numMenor = aux;
            }
            if(numAComparar >= numMenor && numAComparar <= numMayor) {
                devolver = true;
            }
            else{
                devolver = false;
            }
            return devolver;
        }

        void MoverMiSprite(float posX, float posY){
            if(moverArcoiris){
                miArcoiris.setPosition(posX, posY);
            }
        }

        boolean InterseccionEntreSprites (Sprite Sprite1, Sprite Sprite2) {
            boolean devolver = false;
            int Sprite1Izquierda, Sprite1Derecha, Sprite1Abajo, Sprite1Arriba;
            int Sprite2Izquierda, Sprite2Derecha, Sprite2Abajo, Sprite2Arriba;
            Sprite1Izquierda=(int) (Sprite1.getPositionX() - Sprite1.getWidth()/2);
            Sprite1Derecha =(int) (Sprite1.getPositionX() + Sprite1.getWidth()/2);
            Sprite1Abajo =(int) (Sprite1.getPositionY() - Sprite1.getHeight()/2);
            Sprite1Arriba =(int) (Sprite1.getPositionY() + Sprite1.getHeight()/2);
            Sprite2Izquierda =(int) (Sprite2.getPositionX() - Sprite2.getWidth()/2);
            Sprite2Derecha =(int) (Sprite2.getPositionX() + Sprite2.getWidth()/2);
            Sprite2Abajo =(int) (Sprite2.getPositionY() - Sprite2.getHeight()/2);
            Sprite2Arriba =(int) (Sprite2.getPositionY() + Sprite2.getHeight()/2);

            //Borde izq y borde inf de Sprite 1 está dentro de Sprite 2
            if (EstaEntre(Sprite1Izquierda, Sprite2Izquierda, Sprite2Derecha) && EstaEntre(Sprite1Abajo, Sprite2Abajo, Sprite2Arriba)) {
                devolver = true;
            }
            //Borde izq y borde sup de Sprite 1 está dentro de Sprite 2
            if (EstaEntre(Sprite1Izquierda, Sprite2Izquierda, Sprite2Derecha) && EstaEntre(Sprite1Arriba, Sprite2Abajo, Sprite2Arriba)) {
                devolver = true;
            }
            //Borde der y borde sup de Sprite 1 está dentro de Sprite 2
            if (EstaEntre(Sprite1Derecha, Sprite2Izquierda, Sprite2Derecha) && EstaEntre(Sprite1Arriba, Sprite2Abajo, Sprite2Arriba)) {
                devolver = true;
            }
            //Borde der y borde inf de Sprite 1 está dentro de Sprite 2
            if (EstaEntre(Sprite1Derecha, Sprite2Izquierda, Sprite2Derecha) && EstaEntre(Sprite1Abajo, Sprite2Abajo, Sprite2Arriba)) {
                devolver = true;
            }
            //Borde izq y borde inf de Sprite 2 está dentro de Sprite 1
            if (EstaEntre(Sprite2Izquierda, Sprite1Izquierda, Sprite1Derecha) && EstaEntre(Sprite2Abajo, Sprite1Abajo, Sprite1Arriba)) {
                devolver = true;
            }

            //Borde izq y borde sup de Sprite 1 está dentro de Sprite 1
            if (EstaEntre(Sprite2Izquierda, Sprite1Izquierda, Sprite1Derecha) && EstaEntre(Sprite2Arriba, Sprite1Abajo, Sprite1Arriba)) {
                devolver = true;
            }
            //Borde der y borde sup de Sprite 2 está dentro de Sprite 1
            if (EstaEntre(Sprite2Derecha, Sprite1Izquierda, Sprite1Derecha) && EstaEntre(Sprite2Arriba, Sprite1Abajo, Sprite1Arriba)) {
                devolver = true;
            }
            //Borde der y borde inf de Sprite 2 está dentro de Sprite 1
            if (EstaEntre(Sprite2Derecha, Sprite1Izquierda, Sprite1Derecha) && EstaEntre(Sprite2Abajo, Sprite1Abajo, Sprite1Arriba)) {
                devolver = true;
            }
            return devolver;
        }

        void rutinaPerdio(){
            MoveTo moverIzq, moverDer;
            IntervalAction secuencia;

            CallFuncN finDeSecuencia2 = CallFuncN.action(this, "FinDelTrayecto2");

            moverIzq = MoveTo.action(0.1f, miArcoiris.getPositionX()-200, miArcoiris.getPositionY());
            moverDer = MoveTo.action(0.1f, miArcoiris.getPositionX()+200, miArcoiris.getPositionY());

            secuencia = Sequence.actions(moverIzq, moverDer, moverDer, moverIzq, moverIzq, moverDer, moverDer, moverIzq, finDeSecuencia2);
            miArcoiris.runAction(secuencia);
        }

        public void FinDelTrayecto2(CocosNode objeto) {
            miArcoiris.runAction(MoveTo.action(0.8f, ubicacionArcoirisXPRINCIPIO, ubicacionArcoirisYPRINCIPIO));
        }




        @Override
        public boolean ccTouchesBegan (MotionEvent event){
            if(yaJuega)
            {
                Log.d("Toque comienza", "X: " + event.getX() + " - Y: " + event.getY());

                moverArcoiris = false;

                //Ver si estoy dentro de mi arcoiris
                moverArcoiris = EstoyDentroDeArcoiris(event.getX(), PantallaDelDispositivo.getHeight() - event.getY());
                Log.d("mover sprite", "sprite 1: " + moverArcoiris);
            }
            return true;
        }

        @Override
        public boolean ccTouchesMoved (MotionEvent event){

            MoverMiSprite(event.getX(), PantallaDelDispositivo.getHeight() - event.getY());

            boolean tocaPalo1 = InterseccionEntreSprites(miArcoiris, palo1);
            boolean tocaPaloo1 = InterseccionEntreSprites(miArcoiris, paloo1);
            boolean tocaPalo2 = InterseccionEntreSprites(miArcoiris, palo2);
            boolean tocaPaloo2 = InterseccionEntreSprites(miArcoiris, paloo2);

            if(tocaPalo1 || tocaPaloo1 || tocaPalo2 || tocaPaloo2){
                Log.d("nivel1", "PERDISTE AMIGO");

                //Hacer rurina para demostrarle que es un looser --> PERDIO
                rutinaPerdio();

            }
            else
            {
                boolean tocaLlegada = InterseccionEntreSprites(miArcoiris, llegada);
                if(tocaLlegada)
                {
                    Log.d("nivel1", "gano AMIGO");

                    cambiarDeNivel(nivel);
                    nivel++;
                    super.removeChild(miArcoiris, true);
                    super.removeChild(llegada, true);
                }
            }


            return true;
        }

        private void cambiarDeNivel(int nivel)
        {
            ubicacionArcoirisXPRINCIPIO = ((palo2.getPositionX()-palo1.getPositionX())/2)+palo1.getPositionX();
            ubicacionArcoirisYPRINCIPIO = 100;

            Log.d("cambiardenivel", "el nivel es "+nivel);

            switch (nivel)
            {
                case 1:
                    ubicarArcoirisMasLlegada(ubicacionArcoirisXPRINCIPIO, ubicacionArcoirisYPRINCIPIO, "dos.png", "dosWIN.png");
                    //nivel2();

                    break;
                case 2:
                    ubicarArcoirisMasLlegada(ubicacionArcoirisXPRINCIPIO, ubicacionArcoirisYPRINCIPIO, "tres.png", "tresWIN.png");
                    //nivel3();

                    break;
                case 3:
                    ubicarArcoirisMasLlegada(ubicacionArcoirisXPRINCIPIO, ubicacionArcoirisYPRINCIPIO, "cuatro.png", "cuatroWIN.png");
                    //nivel4();

                    break;
                case 4:
                    ubicarArcoirisMasLlegada(ubicacionArcoirisXPRINCIPIO, ubicacionArcoirisYPRINCIPIO, "cinco.png", "cincoWIN.png");
                    //nivel5();

                    break;
                case 5:
                    //gano();

                    break;

            }
        }

        @Override
        public boolean ccTouchesEnded (MotionEvent event){
            return true;
        }
    }
}


