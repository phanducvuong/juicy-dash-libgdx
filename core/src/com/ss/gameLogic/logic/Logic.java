package com.ss.gameLogic.logic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Logic {
    private static Logic instance;
    private float r;
    private Vector2 c;
    public float degree;

    public static Logic getInstance() {
        return instance == null ? instance = new Logic() : instance;
    }

    public void calRC(Image img) {
        c = new Vector2(img.getX() + img.getWidth()/2, img.getY() + img.getHeight()/2);
        r = c.dst(img.getX(), img.getY());
    }

    public float vDomain(Image shape, Image img) {
        float deg = 90 - (shape.getRotation() + degree);
        Vector2 a = new Vector2(c.x + r* MathUtils.cosDeg(deg), c.y - r*MathUtils.sinDeg(deg));
        Vector2 b = new Vector2(c.x - r* MathUtils.cosDeg(deg), c.y + r*MathUtils.sinDeg(deg));

        return (img.getX() - a.x)*(b.y - a.y) - (img.getY() - a.y)*(b.x - a.x);
    }

    public float vDomain(Image shape, Vector2 v) {
        float deg = 90 - (shape.getRotation() + degree);
        Vector2 a = new Vector2(c.x + r* MathUtils.cosDeg(deg), c.y - r*MathUtils.sinDeg(deg));
        Vector2 b = new Vector2(c.x - r* MathUtils.cosDeg(deg), c.y + r*MathUtils.sinDeg(deg));

        return (v.x - a.x)*(b.y - a.y) - (v.y - a.y)*(b.x - a.x);
    }

    public float[] getVertices(Image box, float d, int quadrant) {
        float[] v = new float[]{};
        switch (quadrant) {
            case 1:
                v = new float[] {
                        box.getX() + d, box.getY() + d,
                        box.getX()+ d + box.getWidth(), box.getY() + d,
                        box.getX() + box.getWidth() + d, box.getY()+box.getHeight() + d,
                        box.getX() + d, box.getY()+box.getHeight() + d
                };
                break;
            case 2:
                v = new float[] {
                        box.getX() + box.getWidth()/2, box.getY() + d,
                        box.getX() + box.getWidth(), box.getY() + box.getHeight()/2 + d,
                        box.getX() + box.getWidth()/2, box.getY() + box.getHeight() + d,
                        box.getX(), box.getY() - box.getHeight()/2 + d
                };
                break;
            case 3:
                v = new float[] {
                        box.getX() - d, box.getY() + d,
                        box.getX() + box.getWidth() - d, box.getY() + d,
                        box.getX() + box.getWidth() - d, box.getY() + box.getHeight() + d,
                        box.getX() - d, box.getY() + box.getHeight() + d
                };
                break;
            case 4:

                break;
            case 5:

                break;
            case 6:

                break;
        }

        return v;
    }
}
