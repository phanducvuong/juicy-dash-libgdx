package com.ss.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import static com.badlogic.gdx.math.Interpolation.*;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.FloatArray;
import com.platform.IPlatform;
import com.ss.GMain;
import com.ss.core.action.exAction.GTween;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GShapeTools;
import com.ss.core.util.GStage;
import com.ss.core.util.GTools;
import com.ss.core.util.GUI;
import com.ss.gameLogic.logic.Logic;
import com.ss.gameLogic.objects.Shape;
import com.ss.gameLogic.objects.ShapeLogic;

public class GameScene extends GScreen {

    private TextureAtlas textureAtlas = GMain.textureAtlas;
    private Group gMain = new Group();
    private Group gLogic = new Group();
    private Group gUI = new Group();
    private Image square, box;
    private IPlatform plf = GMain.platform;
    private Logic logic;
    private Stage s;
    private float gsWidth = GStage.getWorldWidth()/2;

    private ShapeLogic shaLogic;

    @Override
    public void dispose() {

    }

    @Override
    public void init() {
        s = GStage.getStage();
        GStage.addToLayer(GLayer.ui, gMain);
        gMain.addActor(gUI);
        gMain.addActor(gLogic);
        gUI.setSize(720, 1280);
        gUI.setPosition(GStage.getWorldWidth()/2, GStage.getWorldHeight()/2, Align.center);

        initAsset();
        initLogic();
        eventClick();

        createBox();
    }

    private void initLogic() {
        logic = Logic.getInstance();
        logic.degree = 45;
        logic.calRC(square);
    }

    private void initAsset() {
        Image bg = GUI.createImage(textureAtlas, "bg");
        assert bg != null;
        bg.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());
        gMain.addActor(bg);

        gUI.setZIndex(1000);
        gLogic.setZIndex(1000);

        square = GUI.createImage(textureAtlas, "square");
        assert square != null;
        square.setPosition(gUI.getWidth()/2, gUI.getHeight()/2, Align.center);
        square.setScale(.5f);
        gUI.addActor(square);
        square.setOrigin(Align.center);
    }

    private void eventClick() {
        stageClick();
    }

    private void stageClick() {
        s.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (x < gsWidth) {
                    square.addAction(rotateBy(-45, .25f, fastSlow));
                }
                else {

                    square.addAction(rotateBy(45, .25f, fastSlow));
                }
                Gdx.app.log("Degree", square.getRotation() + "");
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private void createBox() {
        box = GUI.createImage(textureAtlas, "box");
        assert box != null;
        box.setPosition(60, 338);
        box.setOrigin(Align.center);
        box.rotateBy(45);
        gUI.addActor(box);

        Vector2 center = new Vector2(box.getX()+box.getWidth()/2, box.getY()+box.getHeight()/2);
        float rr = (float) Math.sqrt(box.getWidth()/2 * box.getWidth()/2 + box.getHeight()/2 * box.getHeight()/2);

        Gdx.app.log("a", rr + " c: " + center.toString());
        float[] v = new float[] {
                center.x, center.y - rr,
                center.x + rr, center.y,
                center.x, center.y + rr,
                center.x - rr, center.y
        };

        shaLogic = new ShapeLogic(v);
        gLogic.addActor(shaLogic);
        shaLogic.Log();

        moveShape();
    }

    private void moveShape() {
        float[] v = new float[]{
                box.getX()+5,box.getY()+5,
                box.getX()+5+box.getWidth(),box.getY()+5,
                box.getX()+5+box.getWidth(),box.getY()+5+box.getHeight(),
                box.getX()+5,box.getY()+5+box.getHeight()
        };
        shaLogic.setVertices(v);
        GTween.action(box, moveBy(5, 5, .25f, linear), this::moveShape);
    }

    @Override
    public void run() {

    }
}
