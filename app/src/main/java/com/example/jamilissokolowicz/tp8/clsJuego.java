package com.example.jamilissokolowicz.tp8;

import android.widget.Button;

import org.cocos2d.actions.interval.ScaleBy;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.Menu;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCSize;

public class clsJuego {

    CCGLSurfaceView _VistaDelJuego;
    CCSize PantallaDelDispositivo;

    Sprite wallpaper;
    Sprite nube;
    Sprite gota;
    MenuItemImage botonComenzarJuego;

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
        private void PonerTodo(){

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
            botonComenzarJuego.runAction(ScaleBy.action(0.01f, 2, 2));
            float posicionBotonX, posicionBotonY;
            posicionBotonX = 0;
            posicionBotonY = 0;
            botonComenzarJuego.setPosition(posicionBotonX, posicionBotonY);
        }
        public void PresionaBotonComenzarJuego(){

        }
    }

}
