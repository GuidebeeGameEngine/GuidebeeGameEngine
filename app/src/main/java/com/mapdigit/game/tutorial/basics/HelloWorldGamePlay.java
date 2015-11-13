package com.mapdigit.game.tutorial.basics;

import com.guidebee.game.GamePlay;
import com.guidebee.game.Screen;

/**
 * Created by root on 10/17/15.
 */
public  class HelloWorldGamePlay extends GamePlay {

    @Override
    public void create() {
       Screen main=new HelloWordScreen();
       setScreen(main);
    }

}
