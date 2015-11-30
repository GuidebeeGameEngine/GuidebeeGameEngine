package com.guidebee.game.tutorial.actions;


import com.guidebee.game.ui.actions.Action;
import com.guidebee.game.ui.actions.MoveToAction;
import com.guidebee.math.Interpolation;

import static com.guidebee.game.ui.actions.Actions.*;

public class DemoActions {

    public static Action action1=sequence(

            moveTo(100,300,3f,Interpolation.swingIn),
            rotateTo(60),

            scaleTo(2.0f,2.0f,2f,Interpolation.bounce),
            delay(2f),

            scaleTo(1.0f, 1.0f),
            rotateTo(0),
            delay(2f),
            moveTo(100, 100,3f,Interpolation.elasticIn)


    );

    public static Action action2=sequence(

            moveTo(400,200,3f,Interpolation.swingIn),
            rotateTo(60),

            scaleTo(2.0f,2.0f,2f,Interpolation.bounce),
            delay(2f),

            scaleTo(1.0f, 1.0f),
            rotateTo(0),
            delay(2f),
            moveTo(200, 200,3f,Interpolation.elasticIn)


    );

    public static Action action3=sequence(parallel(

                    rotateTo(360, 3f, Interpolation.circle),
                    scaleTo(2.0f, 2.0f, 2f, Interpolation.bounce)),
            moveTo(300, 300),
            scaleTo(1f, 1f, 3f, Interpolation.bounceOut)

    );

    public static Action action4=forever(sequence(
                    moveTo(500, 400, 5f, Interpolation.swing),
                    moveTo(200, 400, 5f, Interpolation.swingOut)

            )

    );

    public static Action action5=repeat(4, sequence(
                    moveTo(600, 300, 2f, Interpolation.bounceOut),
                    moveTo(600, 100, 2f, Interpolation.bounceIn)

            )

    );
}
