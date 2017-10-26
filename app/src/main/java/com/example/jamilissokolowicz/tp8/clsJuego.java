package com.example.jamilissokolowicz.tp8;

import android.support.annotation.FloatRange;
import android.util.Log;
import android.widget.Button;

import org.cocos2d.actions.interval.MoveBy;
import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.actions.interval.ScaleBy;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCSize;

import java.util.Random;

public class clsJuego {

    CCGLSurfaceView _VistaDelJuego;
    CCSize PantallaDelDispositivo;

    Sprite wallpaper;
    Sprite nube;
    Sprite gota;
    MenuItemImage botonComenzarJuego;

    boolean dejaDeLlover = false;


    public clsJuego(CCGLSurfaceView VistaDelJuego){
        _VistaDelJuego = VistaDelJuego;
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
        CapaBoton miCapaFrenteBoton = new CapaBoton();

        escenaDevolver.addChild(miCapaWallpaper, -20);
        escenaDevolver.addChild(miCapaFondoLluvia, -10);
        escenaDevolver.addChild(miCapaFrenteBoton, 10);

        return escenaDevolver;
    }

    class CapaFondoWallpaper extends Layer{
        public CapaFondoWallpaper() {
            PonerWallpaper();
        }
        private void PonerWallpaper(){
            wallpaper = Sprite.sprite("fondonubes.jpg");
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

                nube = Sprite.sprite("nube.png");
                nube.setPosition(PantallaDelDispositivo.getWidth()/2, (PantallaDelDispositivo.getHeight()-(PantallaDelDispositivo.getHeight()/4)));
                super.addChild(nube);
            }
        }
    }

    class CapaBoton extends Layer {
        public CapaBoton() {
            PonerBoton();
            Menu menuBotones = Menu.menu(botonComenzarJuego);
            menuBotones.setPosition(PantallaDelDispositivo.getWidth()/2,PantallaDelDispositivo.getHeight()/2);
            super.addChild(menuBotones);
        }
        private void PonerBoton(){
            botonComenzarJuego = MenuItemImage.item("botoncomenzarjuego.png", "botoncomenzarjuegopresionado.png", this, "PresionaBotonComenzarJuego");

            float posicionBotonX, posicionBotonY;
            posicionBotonX = 0;
            posicionBotonY = 0;
            botonComenzarJuego.setPosition(posicionBotonX, posicionBotonY);
        }
        public void PresionaBotonComenzarJuego(){
            Log.d("PresionaBoton", "entro");
            dejaDeLlover = true;

           // nube.runAction(MoveTo.action(3, 10, 0));
        }

    }

}
