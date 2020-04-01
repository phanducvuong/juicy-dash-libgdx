package com.ss.gameLogic.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.ss.core.util.GStage;

public class Config {

    public static float ratio = Gdx.graphics.getWidth() / 720;

    public static final Vector2 POS_BOT_0 = new Vector2(GStage.getWorldWidth()/2, GStage.getWorldHeight() - 200);
    public static final Vector2 POS_BOT_1 = new Vector2(GStage.getWorldWidth() - 250, GStage.getWorldHeight()/2 + 50);
    public static final Vector2 POS_BOT_2 = new Vector2(GStage.getWorldWidth() - 250, GStage.getWorldHeight()/2 - 350);
    public static final Vector2 POS_BOT_3 = new Vector2(GStage.getWorldWidth()/2 + 45, 160);
    public static final Vector2 POS_BOT_4 = new Vector2(120, POS_BOT_2.y);
    public static final Vector2 POS_BOT_5 = new Vector2(POS_BOT_4.x, POS_BOT_1.y);


}
