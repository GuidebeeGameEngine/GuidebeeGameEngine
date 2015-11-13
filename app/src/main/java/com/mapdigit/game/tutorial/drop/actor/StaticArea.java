package com.mapdigit.game.tutorial.drop.actor;


import com.guidebee.game.scene.Group;


public class StaticArea extends Group {
    private final EdgePlatform bottomLeftPlatform;
    private final EdgePlatform bottomRightPlatform;
    private final EdgePlatform LeftPlatform;
    private final EdgePlatform rightPlatform;


    private final EdgePlatform boxBottomPlatform;
    private final EdgePlatform boxLeftPlatform;
    private final EdgePlatform boxrightPlatform;


    public StaticArea(){
        bottomLeftPlatform=new EdgePlatform();
        bottomLeftPlatform.initEdgeBody(176f, 16f, 224f, 0f, 1.0f, 0f, 0.5f);

        bottomRightPlatform=new EdgePlatform();
        bottomRightPlatform.initEdgeBody(224f, 0f, 272f, 16f, 1.0f, 0f, 0.5f);

        LeftPlatform=new EdgePlatform();
        LeftPlatform.initEdgeBody(160f, 112f, 176f, 16f, 1.0f, 0f, 1f);

        rightPlatform=new EdgePlatform();
        rightPlatform.initEdgeBody(272f, 16f, 288f, 112f, 1.0f, 0f, 1f);

        boxBottomPlatform=new EdgePlatform();
        boxBottomPlatform.initEdgeBody(576f, 64f, 704f, 64f, 1.0f, 0f, 1f);

        boxLeftPlatform=new EdgePlatform();
        boxLeftPlatform.initEdgeBody(576f, 0f, 576f, 64f, 1.0f, 0f, 1f);

        boxrightPlatform=new EdgePlatform();
        boxrightPlatform.initEdgeBody(704f, 0f, 704f, 64f, 1.0f, 0f, 1f);
    }

}
