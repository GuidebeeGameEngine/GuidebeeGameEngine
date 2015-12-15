package com.guidebee.game.tutorial.drawing;

import android.os.Bundle;

import com.guidebee.game.Configuration;
import com.guidebee.game.activity.GameActivity;

/**
 * Created by James on 14/12/15.
 */
public class GraphicsGameActivity extends GameActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration config = new Configuration();

        config.useAccelerometer = false;
        config.useCompass = false;

        initialize(new GraphicsGamePlay(), config);
    }
}