package com.guidebee.game.tutorial.actions;

import android.os.Bundle;

import com.guidebee.game.Configuration;
import com.guidebee.game.activity.GameActivity;

public class ActionGameActivity extends GameActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration config = new Configuration();

        config.useAccelerometer = false;
        config.useCompass = false;

        initialize(new ActionGamePlay(), config);
    }
}
