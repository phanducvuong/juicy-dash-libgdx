package com.ss.gameLogic.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ss.GMain;
import com.ss.core.util.GUI;

public class Shape {

    private Image shape;

    public Shape(String name) {
        this.shape = GUI.createImage(GMain.textureAtlas, name);
    }

    public void setPosition(float x, float y) {
        shape.setPosition(x, y);
    }

    public Vector2 getPosition() {
        return new Vector2(shape.getX(), shape.getY());
    }

    public Image getShape() {
        return shape;
    }
}
