package com.mapdigit.game.tutorial.basics;

import android.os.Bundle;
import com.guidebee.drawing.Color;
import com.guidebee.drawing.Graphics2D;
import com.guidebee.drawing.Pen;
import com.guidebee.drawing.SolidBrush;
import com.guidebee.drawing.geometry.AffineTransform;
import com.guidebee.game.Configuration;
import com.guidebee.game.ScreenAdapter;
import com.guidebee.game.activity.GameActivity;
import com.guidebee.game.graphics.Pixmap;
import com.guidebee.game.graphics.Texture;
import com.guidebee.utils.Disposable;

import static com.guidebee.game.GameEngine.graphics;

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
