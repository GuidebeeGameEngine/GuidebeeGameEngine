package com.mapdigit.game.tutorial.basics;

import android.os.Bundle;

import com.guidebee.game.Configuration;
import com.guidebee.game.activity.GameActivity;

/**
 * Created by root on 10/17/15.
 */
public class HelloWorldActivity extends GameActivity{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Configuration config = new Configuration();

        config.useAccelerometer = false;
        config.useCompass = false;

        initialize(new HelloWorldGamePlay(),config);
    }

}
